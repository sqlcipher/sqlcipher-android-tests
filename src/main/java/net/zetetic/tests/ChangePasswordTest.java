package net.zetetic.tests;

import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import net.zetetic.QueryHelper;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class ChangePasswordTest extends SQLCipherTest {

    File databaseFile = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
    String secondPassword = "second password";

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE t1(a,b);");
        database.execSQL("INSERT INTO t1(a,b) VALUES(?,?)", new Object[]{"one for the money", "two for the show"});
        database.changePassword(secondPassword);
        database.close();

        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, secondPassword, null);
        int count = QueryHelper.singleIntegerValueFromQuery(database, "SELECT COUNT(*) FROM t1;");
        database.close();

        if (count != 1) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: incorrect record count");
            return false;
        }

        // Verify that these will not cause a crash:
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, secondPassword, null);
        String nullPasswordString = null;
        database.changePassword(nullPasswordString);
        try {
            char[] nullPassword = null;
            database.changePassword(nullPassword);

            Log.e(ZeteticApplication.TAG, "BEHAVIOR CHANGED please update this test");
            return false;
        } catch (NullPointerException e) {
            Log.v(ZeteticApplication.TAG, "IGNORED: null pointer exception when calling SQLiteDatabase.changePassword with null password String", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception", e);
            return false;
        }
        database.close();

        return true;
    }

    @Override
    public String getName() {
        return "Change Password Test";
    }
}
