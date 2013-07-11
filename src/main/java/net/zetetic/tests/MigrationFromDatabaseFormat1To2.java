package net.zetetic.tests;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.zetetic.ZeteticApplication;

import java.io.*;

public class MigrationFromDatabaseFormat1To2 extends SQLCipherTest {

    @Override
    protected SQLiteDatabase createDatabase(File databasePath) {
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
            public void preKey(SQLiteDatabase database) {}
            public void postKey(SQLiteDatabase database) {
                database.execSQL("PRAGMA cipher_default_kdf_iter = 4000;");
            }
        };
        return SQLiteDatabase.openOrCreateDatabase(databasePath,
                ZeteticApplication.DATABASE_PASSWORD, null, hook);
    }

    @Override
    public boolean execute(SQLiteDatabase database) {

        try {
            String password = ZeteticApplication.DATABASE_PASSWORD;
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory(ZeteticApplication.ONE_X_DATABASE);
            File sourceDatabase = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.ONE_X_DATABASE);
            SQLiteDatabase.upgradeDatabaseFormatFromVersion1To2(sourceDatabase, password);

            SQLiteDatabase source = SQLiteDatabase.openOrCreateDatabase(sourceDatabase, password, null);
            Cursor result = source.rawQuery("select * from t1", new String[]{});
            if(result != null){
                result.moveToFirst();
                String a = result.getString(0);
                String b = result.getString(1);
                result.close();
                source.close();
                return a.equals("one for the money") &&
                       b.equals("two for the show");
            }
            return false;
            
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected void tearDown(SQLiteDatabase database) {
        database.execSQL("PRAGMA cipher_default_kdf_iter = 64000;");
    }

    @Override
    public String getName() {
        return "Database 1.x to 2 Migration Test";
    }
}
