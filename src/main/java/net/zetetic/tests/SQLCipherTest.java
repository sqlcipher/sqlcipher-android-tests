package net.zetetic.tests;

import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

public abstract class SQLCipherTest {

    public abstract boolean execute(SQLiteDatabase database);
    public abstract String getName();

    private SQLiteDatabase database;

    protected void internalSetUp() {
        database = ZeteticApplication.getInstance().createDatabase();
        setUp();
    }

    public TestResult run() {

        TestResult result = new TestResult(getName(), false);
        try {
            internalSetUp();
            result.setResult(execute(database));
            internalTearDown();
        } catch (Exception e) {
            Log.v(ZeteticApplication.TAG, e.toString());
        }
        return result;
    }

    private void internalTearDown(){
        database.close();
        SQLiteDatabase.releaseMemory();
        tearDown();
    }

    protected void setUp(){};
    protected void tearDown(){};
}
