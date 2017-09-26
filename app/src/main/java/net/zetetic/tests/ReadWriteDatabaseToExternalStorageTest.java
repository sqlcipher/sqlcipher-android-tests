package net.zetetic.tests;

import android.os.Environment;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class ReadWriteDatabaseToExternalStorageTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {

    File externalDatabaseFile = null;
    try {
      if(isExternalStorageReadable() && isExternalStorageWritable()) {
        database.close();
        File databases = ZeteticApplication.getInstance().getExternalFilesDir("databases");
        externalDatabaseFile = new File(databases, "test.db");
        database = SQLiteDatabase.openOrCreateDatabase(externalDatabaseFile, "test", null);
        if(database == null){
          setMessage("Unable to create database on external storage");
          return false;
        }
        database.execSQL("create table t1(a,b);");
        database.execSQL("insert into t1(a,b) values(?, ?);",
            new Object[]{"one for the money", "two for the show"});
        Cursor cursor = database.rawQuery("select * from t1;", null);
        if(cursor != null){
          cursor.moveToFirst();
          String a = cursor.getString(0);
          String b = cursor.getString(1);
          cursor.close();
          return "one for the money".equals(a) &&
              "two for the show".equals(b);
        }
        return false;
      } else {
        setMessage("External storage unavailable");
        return true;
      }
    }
    finally {
      if(externalDatabaseFile != null){
        externalDatabaseFile.delete();
      }
    }
  }

  @Override
  public String getName() {
    return "Read/Write to External Storage";
  }

  public boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state);
  }

  public boolean isExternalStorageReadable() {
    String state = Environment.getExternalStorageState();
    return Environment.MEDIA_MOUNTED.equals(state) ||
        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
  }

}
