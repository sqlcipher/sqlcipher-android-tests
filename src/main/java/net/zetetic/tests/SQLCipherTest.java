package net.zetetic.tests;

import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;

public abstract class SQLCipherTest {

    public abstract boolean execute(SQLiteDatabase database);
    public abstract String getName();
    public String TAG = getClass().getSimpleName();

    private SQLiteDatabase database;

    protected void internalSetUp() {
        ZeteticApplication.getInstance().prepareDatabaseEnvironment();
        File databasePath = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
        database = createDatabase(databasePath);
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
    
    protected SQLiteDatabase createDatabase(File databasePath){
        return ZeteticApplication.getInstance().createDatabase(databasePath);
    }

    protected void setUp(){};
    protected void tearDown(){};
}
