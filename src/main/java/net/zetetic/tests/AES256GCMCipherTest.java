package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class AES256GCMCipherTest extends SQLCipherTest {

  SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
    public void preKey(SQLiteDatabase db) {}
    public void postKey(SQLiteDatabase db) {
      db.rawExecSQL("PRAGMA cipher = 'aes-256-gcm';");
    }
  };

  @Override
  public boolean execute(SQLiteDatabase database) {
    boolean status = false;
    database.close();
    File databaseFile = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
    databaseFile.delete();
    database = SQLiteDatabase.openOrCreateDatabase(databaseFile, ZeteticApplication.DATABASE_PASSWORD, null, hook);
    database.execSQL("create table t1(a,b);");
    database.execSQL("insert into t1(a,b) values(?, ?);", new Object[]{"one for the money", "two for the show"});
    database.close();

    database = SQLiteDatabase.openDatabase(databaseFile.getAbsolutePath(), ZeteticApplication.DATABASE_PASSWORD,
        null, SQLiteDatabase.OPEN_READWRITE, hook);
    if(database != null){
      Cursor cursor = database.rawQuery("select * from t1;", new String[]{});
      if(cursor != null){
        cursor.moveToFirst();
        String a = cursor.getString(0);
        String b = cursor.getString(1);
        status = "one for the money".equals(a) && "two for the show".equals(b);
        cursor.close();
      }
    }
    return status;
  }

  @Override
  public String getName() {
    return "AES 256 GCM Cipher Test";
  }
}
