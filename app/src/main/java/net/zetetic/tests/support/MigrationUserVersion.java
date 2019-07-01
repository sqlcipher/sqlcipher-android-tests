package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;
import net.zetetic.tests.TestResult;
import java.io.File;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class MigrationUserVersion implements ISupportTest {

    @Override
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);

        try {
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory(ZeteticApplication.ONE_X_USER_VERSION_DATABASE);

            File sourceDatabase = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.ONE_X_USER_VERSION_DATABASE);
            byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
            SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
                @Override
                public void preKey(SQLiteDatabase sqLiteDatabase) {

                }

                @Override
                public void postKey(SQLiteDatabase sqLiteDatabase) {
                    sqLiteDatabase.rawExecSQL("PRAGMA cipher_migrate;");
                }
            };
            SupportFactory factory = new SupportFactory(passphrase, hook);
            SupportSQLiteOpenHelper.Configuration cfg =
              SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
                .name(sourceDatabase.getAbsolutePath())
                .callback(new SupportSQLiteOpenHelper.Callback(5) {
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

            boolean status = database.getVersion() > 0;
            helper.close();
            result.setResult(status);

            return result;

        } catch (Exception e) {
            result.setResult(false);
            result.setMessage(e.getMessage());

            return result;
        }
    }

    @Override
    public String getName() {
        return "Migrate Database 1.x to Current with user_version";
    }
}
