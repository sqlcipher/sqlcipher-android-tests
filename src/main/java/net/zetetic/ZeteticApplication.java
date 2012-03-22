package net.zetetic;

import android.app.Activity;
import android.app.Application;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;

public class ZeteticApplication extends Application {

    public static String DATABASE_NAME = "test.db";
    public static String DATABASE_PASSWORD = "test";
    private static ZeteticApplication instance;
    private Activity activity;
    public static final String TAG = "Zetetic";

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
}
