package net.zetetic;

import android.app.Activity;
import android.app.Application;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.*;

public class ZeteticApplication extends Application {

    public static String DATABASE_NAME = "test.db";
    public static String DATABASE_PASSWORD = "test";
    private static ZeteticApplication instance;
    private Activity activity;
    public static final String TAG = "Zetetic";
    public static final String ONE_X_DATABASE = "1x.db";
    public static final String UNENCRYPTED_DATABASE = "unencrypted.db";

    public ZeteticApplication(){
        instance = this;
    }

    public static ZeteticApplication getInstance(){
        return instance;
    }

    public void setCurrentActivity(Activity activity){
        this.activity = activity;
    }

    public Activity getCurrentActivity(){
        return activity;
    }

    public void prepareDatabaseEnvironment(){
        File databaseFile = getDatabasePath(DATABASE_NAME);
        databaseFile.mkdirs();
        databaseFile.delete();
    }

    public SQLiteDatabase createDatabase(File databaseFile){
        return SQLiteDatabase.openOrCreateDatabase(databaseFile, DATABASE_PASSWORD, null);
    }

    public void extractAssetToDatabaseDirectory(String fileName) throws IOException {

        int length;
        InputStream sourceDatabase = ZeteticApplication.getInstance().getAssets().open(fileName);
        File destinationPath = ZeteticApplication.getInstance().getDatabasePath(fileName);
        OutputStream destination = new FileOutputStream(destinationPath);

        byte[] buffer = new byte[4096];
        while((length = sourceDatabase.read(buffer)) > 0){
            destination.write(buffer, 0, length);
        }
        sourceDatabase.close();
        destination.flush();
        destination.close();
    }

    public void delete1xDatabase() {
        
        File databaseFile = getInstance().getDatabasePath(ONE_X_DATABASE);
        databaseFile.delete();
    }

    public void deleteDatabaseFileAndSiblings(String databaseName){

        File databaseFile = ZeteticApplication.getInstance().getDatabasePath(databaseName);
        File databasesDirectory = new File(databaseFile.getParent());
        for(File file : databasesDirectory.listFiles()){
            file.delete();
        }
    }
}
