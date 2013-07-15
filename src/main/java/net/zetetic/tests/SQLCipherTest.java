package net.zetetic.tests;

import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;

public abstract class SQLCipherTest {

    public abstract boolean execute(SQLiteDatabase database);
    public abstract String getName();
    public String TAG = getClass().getSimpleName();
    private TestResult result;

    private SQLiteDatabase database;

    protected void internalSetUp() {
        ZeteticApplication.getInstance().prepareDatabaseEnvironment();
        File databasePath = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
        database = createDatabase(databasePath);
        setUp();
    }

    public TestResult run() {

        result = new TestResult(getName(), false);
        try {
            internalSetUp();
            result.setResult(execute(database));
            internalTearDown();
        } catch (Exception e) {
            Log.v(ZeteticApplication.TAG, e.toString());
        }
        return result;
    }

    protected void setMessage(String message){
        result.setMessage(message);
    }

    private void internalTearDown(){
        SQLiteDatabase.releaseMemory();
        tearDown(database);
        database.close();
    }
    
    protected SQLiteDatabase createDatabase(File databasePath){
        return ZeteticApplication.getInstance().createDatabase(databasePath);
    }

    protected void setUp(){};
    protected void tearDown(SQLiteDatabase database){};
}
