package net.zetetic.tests;

import android.content.Context;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class ReadableWritableAccessTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        database.close();
        ZeteticApplication.getInstance().deleteDatabaseFileAndSiblings(ZeteticApplication.DATABASE_NAME);
        String databasesFolderPath = ZeteticApplication.getInstance()
                                                   .getDatabasePath(ZeteticApplication.DATABASE_NAME).getParent();
        File databasesFolder = new File(databasesFolderPath);
        databasesFolder.delete();

        DatabaseHelper databaseHelper = new DatabaseHelper(ZeteticApplication.getInstance());

        SQLiteDatabase writableDatabase = databaseHelper.getWritableDatabase(ZeteticApplication.DATABASE_PASSWORD);
        writableDatabase.beginTransaction();
        SQLiteDatabase readableDatabase = databaseHelper.getReadableDatabase(ZeteticApplication.DATABASE_PASSWORD);

        Cursor results = readableDatabase.rawQuery("select count(*) from t1", new String[]{});
        results.moveToFirst();
        int rowCount = results.getInt(0);

        results.close();
        writableDatabase.endTransaction();
        readableDatabase.close();
        writableDatabase.close();

        return rowCount == 1;
    }

    @Override
    public String getName() {
        return "Readable/Writable Access Test";
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, ZeteticApplication.DATABASE_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL("create table t1(a,b)");
            database.execSQL("insert into t1(a,b) values(?, ?)", new Object[]{"one for the money",
                                                                              "two for the show"});
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}
    }
}
