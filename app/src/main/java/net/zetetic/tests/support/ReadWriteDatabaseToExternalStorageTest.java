package net.zetetic.tests.support;

import android.database.Cursor;
import android.os.Environment;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import java.io.File;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class ReadWriteDatabaseToExternalStorageTest implements ISupportTest {
  @Override
  public TestResult run() {
    TestResult result = new TestResult(getName(), false);

    File externalDatabaseFile = null;
    try {
      if(isExternalStorageReadable() && isExternalStorageWritable()) {
        File databases = ZeteticApplication.getInstance().getExternalFilesDir("databases");

        externalDatabaseFile = new File(databases, "test.db");

        byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
        SupportFactory factory = new SupportFactory(passphrase);
        SupportSQLiteOpenHelper.Configuration cfg =
          SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
            .name(externalDatabaseFile.getAbsolutePath())
            .callback(new SupportSQLiteOpenHelper.Callback(1) {
              @Override
              public void onCreate(SupportSQLiteDatabase db) {
                // unused
              }

              @Override
              public void onUpgrade(SupportSQLiteDatabase db, int oldVersion,
                                    int newVersion) {
                // unused
              }
            })
            .build();
        SupportSQLiteOpenHelper helper = factory.create(cfg);
        SupportSQLiteDatabase database = helper.getWritableDatabase();

        if (database == null){
          result.setMessage("Unable to create database on external storage");
          result.setResult(false);

          return result;
        }

        database.execSQL("create table t1(a,b);");
        database.execSQL("insert into t1(a,b) values(?, ?);",
            new Object[]{"one for the money", "two for the show"});

        Cursor cursor = database.query("select * from t1;", null);

        if (cursor != null){
          cursor.moveToFirst();
          String a = cursor.getString(0);
          String b = cursor.getString(1);
          cursor.close();
          result.setResult("one for the money".equals(a) &&
              "two for the show".equals(b));
        }

        return result;
      } else {
        result.setMessage("External storage unavailable");
        result.setResult(false);

        return result;
      }
    }
    finally {
      if (externalDatabaseFile != null){
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
