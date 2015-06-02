package net.zetetic.tests;

import android.util.Log;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import net.zetetic.ZeteticApplication;

import java.io.File;
import java.io.IOException;

public class CorruptDatabaseTest extends SQLCipherTest {

    @Override
    public TestResult run() {

        TestResult result = new TestResult(getName(), false);
        try {
            result.setResult(execute(null));
            SQLiteDatabase.releaseMemory();
        } catch (Exception e) {
            Log.v(ZeteticApplication.TAG, e.toString());
        }
        return result;
    }

    @Override
    public boolean execute(SQLiteDatabase null_database_ignored) {

        File unencryptedDatabase = ZeteticApplication.getInstance().getDatabasePath("corrupt.db");

        try {
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory("corrupt.db");

            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase, "", null);

            // NOTE: database not expected to be null, but check:
            if (database == null) {
                Log.e(TAG, "ERROR: got null database object");
                return false;
            }

            database.close();

            return true;
        } catch (Exception ex) {
            // Uncaught exception (not expected):
            Log.e(TAG, "UNEXPECTED EXCEPTION", ex);
            return false;
        }
        finally {
            unencryptedDatabase.delete();
        }
    }

    @Override
    public String getName() {
        return "Corrupt Database Test";
    }
}
