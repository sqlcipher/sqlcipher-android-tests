package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class AccessDatabaseTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.close();
    try {
      int rows = 0;
      String filename = "encrypted.db";
      ZeteticApplication.getInstance().extractAssetToDatabaseDirectory(filename);
      File databasePath = ZeteticApplication.getInstance().getDatabasePath(filename);
      database = SQLiteDatabase.openDatabase(databasePath.getAbsolutePath(), "test", null, SQLiteDatabase.OPEN_READWRITE);
      if(database != null){
        Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM sqlite_master;", new Object[]{});
        if(cursor != null){
          cursor.moveToFirst();
          rows = cursor.getInt(0);
          cursor.close();
        }
      }
      return rows > 0;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public String getName() {
    return "Access Database File Test";
  }
}
