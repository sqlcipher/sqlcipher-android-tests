package net.zetetic.tests.support;

import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.QueryHelper;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;
import net.zetetic.tests.TestResult;
import java.io.File;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class CipherMigrateTest implements ISupportTest {

    File olderFormatDatabase = ZeteticApplication.getInstance().getDatabasePath("2x.db");

    @Override
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);

        final boolean[] status = {false};
        try {
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory("2x.db");
            SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
                public void preKey(SQLiteDatabase database) {}
                public void postKey(SQLiteDatabase database) {
                    String value = QueryHelper.singleValueFromQuery(database, "PRAGMA cipher_migrate");
                    status[0] = Integer.valueOf(value) == 0;
                }
            };
            byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
            SupportFactory factory = new SupportFactory(passphrase, hook);
            SupportSQLiteOpenHelper.Configuration cfg =
              SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
                .name(olderFormatDatabase.getAbsolutePath())
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

            if(database != null){
                database.close();
            }
        } catch (Exception e) {
            Log.i("CipherMigrateTest", "error", e);
        }
        result.setResult(status[0]);

        return result;
    }

    @Override
    public String getName() {
        return "Cipher Migrate Test";
    }
}
