package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class OpenSQLCipher3DatabaseTest extends SQLCipherTest {

  String databaseFile = "sqlcipher-3.0-testkey.db";

  @Override
  public boolean execute(SQLiteDatabase database) {
    boolean result = false;
    try {
      database.close();
      SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
        public void preKey(SQLiteDatabase database) {}
        public void postKey(SQLiteDatabase database) {
          database.execSQL("PRAGMA kdf_iter = 64000;");
          database.execSQL("PRAGMA cipher_page_size = 1024;");
          database.execSQL("PRAGMA cipher_hmac_algorithm = HMAC_SHA1;");
          database.execSQL("PRAGMA cipher_kdf_algorithm = PBKDF2_HMAC_SHA1;");
        }
      };
      ZeteticApplication.getInstance().extractAssetToDatabaseDirectory(databaseFile);
      File databasePath = ZeteticApplication.getInstance().getDatabasePath(databaseFile);
      database = SQLiteDatabase.openDatabase(databasePath.getAbsolutePath(), "testkey",
          null, SQLiteDatabase.OPEN_READWRITE, hook);
      Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM sqlite_master;", null);
      if(cursor != null){
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        result = count > 0;
      }
    } catch (Exception ex) {}
    return result;
  }

  @Override
  public String getName() {
    return "Open SQLCipher 3 Database";
  }
}
