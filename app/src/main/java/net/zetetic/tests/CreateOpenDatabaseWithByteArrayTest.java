package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class CreateOpenDatabaseWithByteArrayTest extends SQLCipherTest {

  private String databaseName = "foo.db";

  @Override
  public boolean execute(SQLiteDatabase database) {
    boolean status = false;
    database.close();
    byte[] key = generateRandomByteArray(32);
    File newDatabasePath = ZeteticApplication.getInstance().getDatabasePath(databaseName);
    newDatabasePath.delete();
    database = SQLiteDatabase.openOrCreateDatabase(newDatabasePath.getPath(), key, null);
    database.execSQL("create table t1(a,b);");
    database.execSQL("insert into t1(a,b) values(?, ?);", new Object[]{1, 2});
    database.close();
    database = SQLiteDatabase.openOrCreateDatabase(newDatabasePath.getPath(), key, null);
    Cursor cursor = database.rawQuery("select * from t1;", null);
    if (cursor != null) {
      cursor.moveToNext();
      int a = cursor.getInt(0);
      int b = cursor.getInt(1);
      cursor.close();
      status = a == 1 && b == 2;
    }
    return status;
  }

  @Override
  public String getName() {
    return "Create/Open with Byte Array Test";
  }

  @Override
  protected void tearDown(SQLiteDatabase database) {
    super.tearDown(database);
    File newDatabasePath = ZeteticApplication.getInstance().getDatabasePath(databaseName);
    newDatabasePath.delete();
  }
}
