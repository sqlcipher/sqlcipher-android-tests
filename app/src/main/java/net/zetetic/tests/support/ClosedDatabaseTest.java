package net.zetetic.tests.support;

import android.util.Log;
import net.sqlcipher.DatabaseErrorHandler;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import java.io.File;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class ClosedDatabaseTest implements ISupportTest {
    private static final String TAG = "ClosedDatabaseTest";

    @Override
    public TestResult run() {

        TestResult result = new TestResult(getName(), false);
        try {
            result.setResult(execute());
            SQLiteDatabase.releaseMemory();
        } catch (Exception e) {
            Log.v(ZeteticApplication.TAG, e.toString());
        }
        return result;
    }

    public boolean execute() {

        File closedDatabasePath = ZeteticApplication.getInstance().getDatabasePath("closed-db-test.db");

        boolean status = false;

        try {
            byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
            SupportFactory factory = new SupportFactory(passphrase);
            SupportSQLiteOpenHelper.Configuration cfg =
              SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
                .name(closedDatabasePath.getAbsolutePath())
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

            database.close();

            status = execute_closed_database_tests(database);
        } catch (Exception e) {
            // Uncaught [unexpected] exception:
            Log.e(ZeteticApplication.TAG, "Unexpected exception", e);
            return false;
        }
        finally {
            closedDatabasePath.delete();
        }

        return status;
    }

    @SuppressWarnings("deprecation")
    boolean execute_closed_database_tests(SupportSQLiteDatabase database) {
        try {
            /* operations that check if db is closed (and throw IllegalStateException): */
            try {
                // should throw IllegalStateException:
                database.beginTransaction();

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.beginTransaction() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.beginTransaction() did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.endTransaction();

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.endTransaction() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.endTransaction() did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.setTransactionSuccessful();

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.setTransactionSuccessful() did NOT throw throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.setTransactionSuccessful() did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.getVersion();

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.getVersion() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.getVersion() did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.setVersion(111);

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.setVersion() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.setVersion() did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.getMaximumSize();

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.getMaximumSize() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.getMaximumSize() did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.setMaximumSize(111);

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.setMaximumSize() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.setMaximumSize() did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.getPageSize();

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.getPageSize() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.getPageSize() did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.setPageSize(111);

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.setPageSize() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.setPageSize() did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.compileStatement("SELECT 1;");

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.compileStatement() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.compileStatement() did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.query("t1", new String[]{"a", "b"});

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.query() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.query() did throw exception on closed database OK", e);
            }

            // TODO: cover more query functions

            try {
                // should throw IllegalStateException:
                database.execSQL("SELECT 1;");

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.execSQL(String) did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.execSQL(String) did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.execSQL("SELECT 1;", new Object[1]);

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.execSQL(String, Object[]) did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.execSQL(String, Object[]) did throw exception on closed database OK", e);
            }

            try {
                // should throw IllegalStateException:
                database.execSQL("SELECT 1;");

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.rawExecSQL() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.rawExecSQL() did throw exception on closed database OK", e);
            }

            /* operations that do not explicitly check if db is closed
             * ([should] throw SQLiteException on a closed database): */

//            try {
//                // should throw IllegalStateException:
//                database.setLocale(Locale.getDefault());
//
//                // should not get here:
//                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.setLocale() did NOT throw exception on closed database");
//                return false;
//            } catch (SQLiteException e) {
//                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.setLocale() did throw exception on closed database OK", e);
//            }

            try {
                // should throw IllegalStateException [since it calls getVersion()]:
                database.needUpgrade(111);

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.needUpgrade() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.needUpgrade() did throw exception on closed database OK", e);
            }

            /* operations that are NOT expected to throw an exception if the database is closed ([should] not crash) */


            /* XXX TODO: these functions should check the db state,
             * TBD either throw or simply return false if the db is closed */
            database.yieldIfContendedSafely();
            database.yieldIfContendedSafely(100);

            database.inTransaction();
            database.isDbLockedByCurrentThread();

            database.close();

            database.isReadOnly();
            database.isOpen();

            try {
                // should throw IllegalStateException:
                database.setMaxSqlCacheSize(111);

                // should not get here:
                Log.e(ZeteticApplication.TAG, "SQLiteDatabase.setMaxSqlCacheSize() did NOT throw exception on closed database");
                return false;
            } catch (IllegalStateException e) {
                Log.v(ZeteticApplication.TAG, "SQLiteDatabase.setMaxSqlCacheSize() did throw exception on closed database OK", e);
            }

        } catch (Exception e) {
            // Uncaught [unexpected] exception:
            Log.e(ZeteticApplication.TAG, "Unexpected exception", e);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "Closed Database Test";
    }
}
