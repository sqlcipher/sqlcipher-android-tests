package net.zetetic.tests.support;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;
import net.zetetic.tests.TestResult;
import java.io.File;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class ExportToUnencryptedDatabase implements ISupportTest {

    File unencryptedFile;

    @Override
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);

        ZeteticApplication.getInstance().deleteDatabase(ZeteticApplication.DATABASE_NAME);

        byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
        SupportFactory
          factory = new SupportFactory(passphrase);
        SupportSQLiteOpenHelper.Configuration cfg =
          SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
            .name(ZeteticApplication.DATABASE_NAME)
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

        database.execSQL("create table t1(a,b);");
        database.execSQL("insert into t1(a,b) values(?, ?);", new Object[]{"one for the money", "two for the show"});
        unencryptedFile = ZeteticApplication.getInstance().getDatabasePath("plaintext.db");
        ZeteticApplication.getInstance().deleteDatabase("plaintext.db");
        database.execSQL(String.format("ATTACH DATABASE '%s' as plaintext KEY '';",
                unencryptedFile.getAbsolutePath()));
        ((SQLiteDatabase)database).rawExecSQL("SELECT sqlcipher_export('plaintext');");
        database.execSQL("DETACH DATABASE plaintext;");
        helper.close();

        passphrase = new byte[0];
        factory = new SupportFactory(passphrase);
        cfg =
          SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
            .name(unencryptedFile.getAbsolutePath())
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
        SupportSQLiteDatabase unencryptedDatabase = helper.getWritableDatabase();

        Cursor cursor = unencryptedDatabase.query("select * from t1;", new String[]{});
        String a = "";
        String b = "";
        while(cursor.moveToNext()){
            a = cursor.getString(0);
            b = cursor.getString(1);
        }
        cursor.close();
        helper.close();

        result.setResult(a.equals("one for the money") &&
               b.equals("two for the show"));

        unencryptedFile.delete();

        return result;
    }

    @Override
    public String getName() {
        return "Export to Unencrypted Database";
    }
}
