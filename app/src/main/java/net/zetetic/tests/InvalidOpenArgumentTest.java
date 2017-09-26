package net.zetetic.tests;

import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import net.zetetic.ZeteticApplication;

import java.io.File;

public class InvalidOpenArgumentTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {
        database.close();

        try {
            String nullPathString = null;
            database = SQLiteDatabase.openOrCreateDatabase(nullPathString, ZeteticApplication.DATABASE_PASSWORD, null);
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with null path string did not fail");
            return false;
        } catch (IllegalArgumentException e){
            Log.v(ZeteticApplication.TAG,
                "SQLiteDatabase.openOrCreateDatabase() with null path string did throw an IllegalArgumentException as expected OK", e);
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with null path string did throw an unexpected exception type", e);
            return false;
        }

        try {
            String nullPathString = null;
            database = SQLiteDatabase.openOrCreateDatabase(nullPathString, ZeteticApplication.DATABASE_PASSWORD, null, null, null);
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with null path string did not fail");
            return false;
        } catch (IllegalArgumentException e){
            Log.v(ZeteticApplication.TAG,
                "SQLiteDatabase.openOrCreateDatabase() with null path string did throw an IllegalArgumentException as expected OK", e);
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with null path string did throw an unexpected exception type", e);
            return false;
        }

        try {
            File nullFile = null;
            database = SQLiteDatabase.openOrCreateDatabase(nullFile, ZeteticApplication.DATABASE_PASSWORD, null);
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with null file did not fail");
            return false;
        } catch (IllegalArgumentException e){
            Log.v(ZeteticApplication.TAG,
                "EXPECTED RESULT: SQLiteDatabase.openOrCreateDatabase() with null file did throw an IllegalArgumentException OK", e);
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with no password did throw an unexpected exception type", e);
            return false;
        }

        try {
            File nullFile = null;
            database = SQLiteDatabase.openOrCreateDatabase(nullFile, ZeteticApplication.DATABASE_PASSWORD, null, null, null);
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with null file did not fail");
            return false;
        } catch (IllegalArgumentException e){
            Log.v(ZeteticApplication.TAG,
                "EXPECTED RESULT: SQLiteDatabase.openOrCreateDatabase() with null file did throw an IllegalArgumentException OK", e);
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with null file did throw an unexpected exception type", e);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "Invalid Open Argument Test";
    }
}
