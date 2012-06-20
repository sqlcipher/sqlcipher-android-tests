package net.zetetic.tests;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.zetetic.ZeteticApplication;

public class VerifyOnUpgradeIsCalledTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        ZeteticApplication.getInstance().deleteDatabaseFileAndSiblings(ZeteticApplication.DATABASE_NAME);
        DatabaseHelper firstRun = new DatabaseHelper(ZeteticApplication.getInstance(), 1);
        SQLiteDatabase db = firstRun.getWritableDatabase(ZeteticApplication.DATABASE_PASSWORD);
        db.close();
        DatabaseHelper secondRun = new DatabaseHelper(ZeteticApplication.getInstance(), 2);
        SQLiteDatabase db2 = secondRun.getWritableDatabase(ZeteticApplication.DATABASE_PASSWORD);
        db2.close();
        return secondRun.OnUpgradeCalled;
    }

    @Override
    public String getName() {
        return "Verify onUpgrade Is Called Test";
    }

    class DatabaseHelper extends SQLiteOpenHelper {

        public boolean OnUpgradeCalled;

        public DatabaseHelper(Context context, int version) {
            super(context, ZeteticApplication.DATABASE_NAME, null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL("create table t1(a,b)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            OnUpgradeCalled = true;
        }
    }
}
