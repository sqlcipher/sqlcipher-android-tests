package net.zetetic.tests;

import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteException;

import net.zetetic.ZeteticApplication;

import java.io.File;
import java.util.UUID;

public class InvalidPasswordTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {
        database.rawExecSQL("create table t1(a,b);");
        database.close();

        File databaseFile = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);

        try {
            SQLiteDatabase.openOrCreateDatabase(databaseFile, UUID.randomUUID().toString(), null);

            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with invalid password did not fail");
            return false;
        } catch (SQLiteException e){
            Log.v(ZeteticApplication.TAG,
                "SQLiteDatabase.openOrCreateDatabase() with invalid password did throw a SQLiteException as expected OK", e);
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with invalid password did throw an unexpected exception", e);
            return false;
        }

        try {
            String noPassword = "";
            SQLiteDatabase.openOrCreateDatabase(databaseFile, noPassword, null);

            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with no password did NOT fail on an encrypted database");
            return false;
        } catch (SQLiteException e){
            Log.v(ZeteticApplication.TAG,
                "SQLiteDatabase.openOrCreateDatabase() with no password did throw a SQLiteException as expected OK", e);
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG,
                "NOT EXPECTED: SQLiteDatabase.openOrCreateDatabase() with no password did throw an unexpected exception type", e);
            return false;
        }

        try {
            database = SQLiteDatabase.openOrCreateDatabase(databaseFile, ZeteticApplication.DATABASE_PASSWORD, null);
            database.execSQL("insert into t1(a,b) values(?, ?)", new Object[]{"testing", "123"});
        } catch (Exception e){
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: attempt to access database with correct password did throw an unexpected exception", e);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "Invalid Password Test";
    }
}
