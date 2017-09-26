package net.zetetic.tests;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import net.zetetic.ZeteticApplication;

import android.util.Log;

import java.io.File;
import java.io.IOException;

public class QueryNonEncryptedDatabaseTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        database.close();
        File unencryptedDatabase = ZeteticApplication.getInstance().getDatabasePath("unencrypted.db");
        String nullPasswordString = null;

        try {
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory("unencrypted.db");
        } catch (IOException e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: caught IOException", e);
            return false;
        }

        boolean success = false;

        try {
            SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase, ZeteticApplication.DATABASE_PASSWORD, null);

            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() of unencrypted database with a password did not fail");
            return false;
        } catch (SQLiteException e){
            Log.v(ZeteticApplication.TAG,
                "SQLiteDatabase.openOrCreateDatabase() of unencrypted database with a password did throw a SQLiteException as expected OK", e);
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with invalid password did throw an unexpected exception", e);
            return false;
        }

        try {
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase, nullPasswordString, null);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception", e);
            return false;
        }

        try {
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase.getPath(), nullPasswordString, null, null, null);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception", e);
            return false;
        }

        try {
            database = SQLiteDatabase.openDatabase(unencryptedDatabase.getPath(), nullPasswordString, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception", e);
            return false;
        }

        try {
            database = SQLiteDatabase.openDatabase(unencryptedDatabase.getPath(), nullPasswordString, null, SQLiteDatabase.OPEN_READWRITE, null, null);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception", e);
            return false;
        }

        try {
            char[] nullPassword = null;
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase.getPath(), nullPassword, null, null);
            Cursor cursor = database.rawQuery("SELECT * FROM t1", new String[]{});
            cursor.moveToFirst();
            String a = cursor.getString(0);
            String b = cursor.getString(1);
            cursor.close();
            database.close();

            if (!a.equals("one for the money") || !b.equals("two for the show")) {
                Log.e(ZeteticApplication.TAG, "NOT EXPECTED: incorrect data from unencrypted database");
                return false;
            }
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception", e);
            return false;
        }

        try {
            char[] noPasswordChars = new char[0];
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase.getPath(), noPasswordChars, null, null);
            Cursor cursor = database.rawQuery("SELECT * FROM t1", new String[]{});
            cursor.moveToFirst();
            String a = cursor.getString(0);
            String b = cursor.getString(1);
            cursor.close();
            database.close();

            if (!a.equals("one for the money") || !b.equals("two for the show")) {
                Log.e(ZeteticApplication.TAG, "NOT EXPECTED: incorrect data from unencrypted database");
                return false;
            }
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception", e);
            return false;
        }

        try {
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase, "", null);
            Cursor cursor = database.rawQuery("select * from t1", new String[]{});
            cursor.moveToFirst();
            String a = cursor.getString(0);
            String b = cursor.getString(1);
            cursor.close();
            database.close();

            success = a.equals("one for the money") &&
                        b.equals("two for the show");
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception when reading database with blank password", e);
            return false;
        }

        return success;
    }

    @Override
    public String getName() {
        return "Query Non-Encrypted Database Test";
    }
}
