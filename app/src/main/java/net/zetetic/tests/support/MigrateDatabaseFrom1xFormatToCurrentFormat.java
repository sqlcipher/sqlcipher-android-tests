package net.zetetic.tests.support;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import java.io.File;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class MigrateDatabaseFrom1xFormatToCurrentFormat implements ISupportTest {
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);

        try {
            File sourceDatabase = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.ONE_X_DATABASE);
            byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory(ZeteticApplication.ONE_X_DATABASE);
            SupportFactory factory = new SupportFactory(passphrase, "PRAGMA cipher_migrate;");
            SupportSQLiteOpenHelper.Configuration cfg =
              SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
                .name(sourceDatabase.getAbsolutePath())
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
            Cursor c = database.query("select * from t1", new String[]{});
            if(c != null){
                c.moveToFirst();
                String a = c.getString(0);
                String b = c.getString(1);
                c.close();
                database.close();
                result.setResult(a.equals("one for the money") &&
                       b.equals("two for the show"));

                return result;
            }
            result.setResult(false);
            return result;
            
        } catch (Exception e) {
            result.setResult(false);
            return result;
        }
    }

    @Override
    public String getName() {
        return "Migrate Database 1.x to Current Test";
    }
}
