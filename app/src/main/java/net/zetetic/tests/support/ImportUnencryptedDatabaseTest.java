package net.zetetic.tests.support;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;
import net.zetetic.tests.TestResult;
import java.io.File;
import java.io.IOException;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class ImportUnencryptedDatabaseTest implements ISupportTest {
    @Override
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);

        File unencryptedDatabase = ZeteticApplication.getInstance().getDatabasePath("unencrypted.db");
        File encryptedDatabase = ZeteticApplication.getInstance().getDatabasePath("encrypted.db");

        try {
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory("unencrypted.db");
            byte[] passphrase = new byte[0];
            SupportFactory factory = new SupportFactory(passphrase);
            SupportSQLiteOpenHelper.Configuration cfg =
              SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
                .name(unencryptedDatabase.getAbsolutePath())
                .callback(new SupportSQLiteOpenHelper.Callback(1) {
                    @Override
                    public void onCreate(SupportSQLiteDatabase db) {
                        // unused
                    }

                    @Override
                    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion,
                                          int newVersion) {
                        // unused
                    }
                })
                .build();
            SupportSQLiteOpenHelper helper = factory.create(cfg);
            SupportSQLiteDatabase database = helper.getWritableDatabase();

            database.execSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s'",
                                encryptedDatabase.getAbsolutePath(), ZeteticApplication.DATABASE_PASSWORD));
            // database.execSQL("select sqlcipher_export('encrypted')");
          ((SQLiteDatabase)database).rawExecSQL("select sqlcipher_export('encrypted')");
            database.execSQL("DETACH DATABASE encrypted");
            helper.close();

            passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());

            factory = new SupportFactory(passphrase);
            cfg =
              SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
                .name(encryptedDatabase.getAbsolutePath())
                .callback(new SupportSQLiteOpenHelper.Callback(1) {
                    @Override
                    public void onCreate(SupportSQLiteDatabase db) {
                        // unused
                    }

                    @Override
                    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion,
                                          int newVersion) {
                        // unused
                    }
                })
                .build();
            helper = factory.create(cfg);
            database = helper.getWritableDatabase();

            Cursor cursor = database.query("select * from t1", new String[]{});
            cursor.moveToFirst();
            String a = cursor.getString(0);
            String b = cursor.getString(1);
            cursor.close();
            helper.close();

            result.setResult(a.equals("one for the money") &&
                   b.equals("two for the show"));
            return result;
        } catch (IOException e) {
            result.setResult(false);

            return result;
        }
        finally {
            unencryptedDatabase.delete();
            encryptedDatabase.delete();
        }
    }

    @Override
    public String getName() {
        return "Import Unencrypted Database Test";
    }
}
