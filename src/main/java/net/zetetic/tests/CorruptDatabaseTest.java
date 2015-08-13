package net.zetetic.tests;

import android.util.Log;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseCorruptException;

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

            Cursor cursor = database.rawQuery("select * from sqlite_master;", null);

            if (cursor == null) {
                Log.e(TAG, "NOT EXPECTED: database.rawQuery() returned null cursor");
                return false;
            }

            // *Should* corrupt the database file that is already open:
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory("corrupt.db");

            try {
                // Attempt to write to corrupt database file *should* fail:
                database.execSQL("CREATE TABLE t1(a,b);");

                // NOT EXPECTED to get here:
                Log.e(TAG, "NOT EXPECTED: CREATE TABLE succeeded ");
                return false;
            } catch (SQLiteDatabaseCorruptException ex) {
                Log.v(TAG, "Caught SQLiteDatabaseCorruptException as expected OK");
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
