package net.zetetic.tests.support;

import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import java.util.UUID;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class InvalidPasswordTest implements ISupportTest {
    @Override
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);
        byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
        SupportFactory factory = new SupportFactory(passphrase);
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
        helper.close();

        passphrase = SQLiteDatabase.getBytes(UUID.randomUUID().toString().toCharArray());
        factory = new SupportFactory(passphrase);

        try {
            helper = factory.create(cfg);
            database = helper.getWritableDatabase();

            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with invalid password did not fail");
            result.setResult(false);
            return result;
        } catch (SQLiteException e){
            Log.v(ZeteticApplication.TAG,
                "SQLiteDatabase.openOrCreateDatabase() with invalid password did throw a SQLiteException as expected OK", e);
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with invalid password did throw an unexpected exception", e);
            result.setResult(false);
            return result;
        }

        passphrase = SQLiteDatabase.getBytes("".toCharArray());
        factory = new SupportFactory(passphrase);

        try {
            helper = factory.create(cfg);
            database = helper.getWritableDatabase();

            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with no password did NOT fail on an encrypted database");
            result.setResult(false);
            return result;
        } catch (SQLiteException e){
            Log.v(ZeteticApplication.TAG,
                "SQLiteDatabase.openOrCreateDatabase() with no password did throw a SQLiteException as expected OK", e);
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with no password did throw an unexpected exception type", e);
            result.setResult(false);
            return result;
        }

        passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
        factory = new SupportFactory(passphrase);

        try {
            helper = factory.create(cfg);
            database = helper.getWritableDatabase();
            database.execSQL("insert into t1(a,b) values(?, ?)", new Object[]{"testing", "123"});
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: attempt to access database with correct password did throw an unexpected exception", e);
            result.setResult(false);
            return result;
        }

        result.setResult(true);
        return result;
    }

    @Override
    public String getName() {
        return "Invalid Password Test";
    }
}
