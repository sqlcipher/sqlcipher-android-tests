package net.zetetic.tests;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import net.zetetic.ZeteticApplication;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import java.util.UUID;

public class CreateNonEncryptedDatabaseTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        database.close();

        String unencryptedDatabaseName = "unencrypted-" + UUID.randomUUID().toString() + ".db";
        File unencryptedDatabase = ZeteticApplication.getInstance().getDatabasePath(unencryptedDatabaseName);

        try {
            String noPasswordString = "";
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase, noPasswordString, null);
            database.execSQL("CREATE TABLE test_table(test_column);");
            database.execSQL("INSERT INTO test_table VALUES(?);", new Object[]{"test value"});
            database.close();
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception", e);
            return false;
        }

        try {
            char[] nullPassword = null;
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase.getPath(), nullPassword, null, null);
            Cursor cursor = database.rawQuery("SELECT * FROM test_table;", new String[]{});
            cursor.moveToFirst();
            String a = cursor.getString(0);
            cursor.close();
            database.close();
            if (a == null || !a.equals("test value")) {
                Log.e(ZeteticApplication.TAG, "NOT EXPECTED: INCORRECT or MISSING test value");
                return false;
            }
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception when reading database with zero-length password", e);
            return false;
        }

        try {
            char[] noPassword = new char[0];
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase.getPath(), noPassword, null, null);
            Cursor cursor = database.rawQuery("SELECT * FROM test_table;", new String[]{});
            cursor.moveToFirst();
            String a = cursor.getString(0);
            cursor.close();
            database.close();
            if (a == null || !a.equals("test value")) {
                Log.e(ZeteticApplication.TAG, "NOT EXPECTED: INCORRECT or MISSING test value");
                return false;
            }
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception when reading database with zero-length password", e);
            return false;
        }

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
            String nullPasswordString = null;
            SQLiteDatabase.create(null, nullPasswordString);
            Log.e(ZeteticApplication.TAG, "BEHAVIOR CHANGED please update this test");
            return false;
        } catch (NullPointerException e) {
            Log.v(ZeteticApplication.TAG, "IGNORED: null pointer exception when opening database with null String password", e);
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: exception", e);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "Create Non-Encrypted Database Test";
    }
}
