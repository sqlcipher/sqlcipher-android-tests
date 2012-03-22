package net.zetetic.tests;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.*;

public class MigrationFromDatabaseFormat1To2 extends SQLCipherTest {
    
    private String password = "test";
    private String sourceDatabaseName = "1x.db";
    
    @Override
    public boolean execute(SQLiteDatabase database) {

        try {
            extract1xDatabaseToDatabaseDirectory();
            File sourceDatabase = ZeteticApplication.getInstance().getDatabasePath(sourceDatabaseName);
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

    private void extract1xDatabaseToDatabaseDirectory() throws IOException {

        int length;
        InputStream sourceDatabase = ZeteticApplication.getInstance().getAssets().open(sourceDatabaseName);
        File destinationPath = ZeteticApplication.getInstance().getDatabasePath(sourceDatabaseName);
        OutputStream destination = new FileOutputStream(destinationPath);

        byte[] buffer = new byte[4096];
        while((length = sourceDatabase.read(buffer)) > 0){
            destination.write(buffer, 0, length);
        }
        sourceDatabase.close();
        destination.flush();
        destination.close();
    }

    @Override
    public String getName() {
        return "Database 1.x to 2 Migration Test";
    }
}
