package net.zetetic.tests.support;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;
import net.zetetic.tests.TestResult;
import java.io.File;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class VerifyUTF8EncodingForKeyTest implements ISupportTest {
    @Override
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);

        try {
            String password = "hello";
            String invalidPassword = "ŨťŬŬů";
            SupportSQLiteDatabase sourceDatabase;
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory("hello.db");
            File sourceDatabaseFile = ZeteticApplication.getInstance().getDatabasePath("hello.db");

            byte[] passphrase = SQLiteDatabase.getBytes(invalidPassword.toCharArray());
            SupportFactory factory = new SupportFactory(passphrase);
            SupportSQLiteOpenHelper.Configuration cfg =
              SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
                .name(sourceDatabaseFile.getAbsolutePath())
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

            try {
                sourceDatabase = helper.getWritableDatabase();

                if(queryContent(sourceDatabase)){
                    sourceDatabase.close();
                    result.setMessage(String.format("Database should not open with password:%s", invalidPassword));
                    result.setResult(false);

                    return result;
                }
            } catch (SQLiteException ex){}

            passphrase = SQLiteDatabase.getBytes(password.toCharArray());
            factory = new SupportFactory(passphrase, "PRAGMA cipher_migrate;");
            cfg =
              SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
                .name(sourceDatabaseFile.getAbsolutePath())
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

            sourceDatabase = helper.getWritableDatabase();
            result.setResult(queryContent(sourceDatabase));
            helper.close();
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setResult(false);
        }

        return result;
    }

    private boolean queryContent(SupportSQLiteDatabase source){
        Cursor result = source.query("select * from t1", new String[]{});
        if(result != null){
            result.moveToFirst();
            String a = result.getString(0);
            String b = result.getString(1);
            result.close();
            return a.equals("one for the money") &&
                    b.equals("two for the show");
        }
        return false;
    }

    @Override
    public String getName() {
        return "Verify Only UTF-8 Key Test";
    }
}
