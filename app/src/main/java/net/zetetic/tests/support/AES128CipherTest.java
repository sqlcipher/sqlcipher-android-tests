package net.zetetic.tests.support;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class AES128CipherTest implements ISupportTest {
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);

        byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
        SupportFactory factory = new SupportFactory(passphrase, "PRAGMA cipher = 'aes-128-cbc'");
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

        String actual = "";
        String value = "hey";
        database.execSQL("create table t1(a)");
        database.execSQL("insert into t1(a) values (?)", new Object[]{value});
        Cursor c = database.query("select * from t1", new String[]{});
        if(c != null){
            c.moveToFirst();
            actual = c.getString(0);
            c.close();
        }
        result.setResult(actual.equals(value));

        return result;
    }

    @Override
    public String getName() {
        return "AES-128 Bit Cipher Test";
    }
}
