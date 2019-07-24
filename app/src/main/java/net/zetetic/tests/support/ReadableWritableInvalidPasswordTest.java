package net.zetetic.tests.support;

import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class ReadableWritableInvalidPasswordTest extends SupportTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE tt(data);");
        database.execSQL("INSERT INTO tt VALUES(?)", new Object[]{"test data"});
        database.close();

        try {
            SupportSQLiteDatabase db = open("invalid password");
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened writable encrypted database with invalid password");
            db.close();
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening writable encrypted database with invalid password OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: other exception when opening writable encrypted database with invalid password", e);
            return false;
        }

        try {
            SupportSQLiteDatabase db = open("");
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened writable encrypted database with blank password String");
            db.close();
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening writable encrypted database with blank password String OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: other exception when opening writable encrypted database with blank password String", e);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "Readable/Writable Invalid Password Test";
    }

    private SupportSQLiteDatabase open(String password) {
        byte[] passphrase = SQLiteDatabase.getBytes(password.toCharArray());
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
        return helper.getWritableDatabase();
    }
}
