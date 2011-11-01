package net.zetetic.tests;

import android.util.Log;
import info.guardianproject.database.sqlcipher.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

public abstract class SQLCipherTest {

    public abstract boolean execute(SQLiteDatabase database);
    public abstract String getName();

    private SQLiteDatabase database;

    protected void setUp() {
        database = ZeteticApplication.getInstance().createDatabase();
    }

    public TestResult run() {

        TestResult result = new TestResult(getName(), false);
        try {
            setUp();
            result.setResult(execute(database));
            tearDown();
        } catch (Exception e) {
            Log.v(ZeteticApplication.TAG, e.toString());
        }
        return result;
    }

    private void tearDown(){
        database.close();
        SQLiteDatabase.releaseMemory();
    }
}
