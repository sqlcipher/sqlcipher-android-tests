package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class ExternalStorageTest extends SQLCipherTest {

    private SQLiteDatabase database;
    private File databasePath;

    @Override
    protected void setUp() {
        super.setUp();
        databasePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test/encrypt_coresuite.db");
        databasePath.getParentFile().mkdirs();
        databasePath.mkdirs();
        databasePath.delete();
        Log.i(TAG, "Before createDatabase");
        database = createDatabase(databasePath);
        Log.i(TAG, "after createDatabase");
    }

    @Override
    protected void tearDown(SQLiteDatabase ignored) {
        super.tearDown(ignored);
        SQLiteDatabase.releaseMemory();
        this.database.close();
        if (databasePath != null && databasePath.exists()) {
            databasePath.delete();
        }
    }

    @Override
    public boolean execute(SQLiteDatabase ignored) {
        try {
            database.execSQL("create table t1(a, b)");
            database.execSQL("insert into t1(a, b) values(?, ?)", new Object[]{"one for the money", "two for the show"});
            Cursor c = database.rawQuery("select * from t1;", null);
            return c.getCount() == 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String getName() {
        return "Text external path";
    }
}
