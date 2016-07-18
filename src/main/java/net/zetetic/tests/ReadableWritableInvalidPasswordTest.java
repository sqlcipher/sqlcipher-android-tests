package net.zetetic.tests;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteOpenHelper;

import net.zetetic.ZeteticApplication;

import android.util.Log;

import java.io.File;

public class ReadableWritableInvalidPasswordTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE tt(data);");
        database.execSQL("INSERT INTO tt VALUES(?)", new Object[]{"test data"});
        database.close();

        MyHelper myHelper = new MyHelper();

        try {
            myHelper.getWritableDatabase("invalid password");
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened writable encrypted database with invalid password");
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening writable encrypted database with invalid password OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: other exception when opening writable encrypted database with invalid password", e);
            return false;
        }

        try {
            myHelper.getReadableDatabase("invalid password");
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened readable encrypted database with invalid password");
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening readable encrypted database with invalid password OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: other exception when opening readable encrypted database with invalid password", e);
            return false;
        }

        try {
            myHelper.getWritableDatabase("");
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened writable encrypted database with blank password String");
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening writable encrypted database with blank password String OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: other exception when opening writable encrypted database with blank password String", e);
            return false;
        }

        try {
            myHelper.getReadableDatabase("");
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened readable encrypted database with blank password String");
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening readable encrypted database with blank password String OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: other exception when opening readable encrypted database with blank password String", e);
            return false;
        }

        try {
            myHelper.getWritableDatabase(new char[0]);
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened writable encrypted database with blank password char array");
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening writable encrypted database with blank password char array OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: other exception when opening writable encrypted database with blank password char array", e);
            return false;
        }

        try {
            myHelper.getReadableDatabase(new char[0]);
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened readable encrypted database with blank password char array");
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening readable encrypted database with blank password char array OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: other exception when opening readable encrypted database with blank password char array", e);
            return false;
        }

        char[] nullPassword = null;

        try {
            myHelper.getWritableDatabase(nullPassword);
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened writable encrypted database with null password char array");
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening writable encrypted database with null password char array OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: other exception when opening writable encrypted database with null password char array", e);
            return false;
        }

        try {
            myHelper.getReadableDatabase(nullPassword);
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened readable encrypted database with null password char array");
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening readable encrypted database with null password char array OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: other exception when opening readable encrypted database with null password char array", e);
            return false;
        }

        String nullPasswordString = null;

        try {
            myHelper.getWritableDatabase(nullPasswordString);

            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened writable encrypted database with null password String");
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening writable encrypted database with null password String OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception when opening writable database with null password String", e);
            return false;
        }

        try {
            myHelper.getReadableDatabase(nullPasswordString);

            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: successfully opened readable encrypted database with null password String");
            return false;
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: SQLiteException when opening readable encrypted database with null password String OK", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception when opening readable encrypted with null password String", e);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "Readable/Writable Invalid Password Test";
    }

    private class MyHelper extends SQLiteOpenHelper {
        public MyHelper() {
            super(ZeteticApplication.getInstance(), ZeteticApplication.DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            Log.v(ZeteticApplication.TAG, "Do nothing in MyHelper.onCreate()");
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            Log.v(ZeteticApplication.TAG, "Do nothing in MyHelper.onUpgrade()");
        }
    }
}
