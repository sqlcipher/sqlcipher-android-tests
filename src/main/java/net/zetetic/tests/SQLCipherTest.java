package net.zetetic.tests;

import android.content.Context;
import info.guardianproject.database.sqlcipher.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;

public abstract class SQLCipherTest {

    public abstract boolean execute(SQLiteDatabase database);
    public abstract String getName();

    private SQLiteDatabase database;

    protected void setUp() {
        Context context = ZeteticApplication.getInstance().getApplicationContext();
        File databaseFile = context.getDatabasePath("test.db");
        databaseFile.mkdirs();
        databaseFile.delete();
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, "test", null);
    }

    public TestResult run() {
        
        TestResult result = new TestResult(getName(), false);
        try {
            setUp();
            result.setResult(execute(database));
        } catch (Exception e) {}
        return result;
    }
}
