package net.zetetic.tests;

import android.content.Context;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class SQLiteOpenHelperWithByteArrayKeyTest extends SQLCipherTest {


  private String databaseName = "foo.db";

  @Override
  public boolean execute(SQLiteDatabase database) {
    boolean status = false;
    database.close();
    byte[] key = generateRandomByteArray(32);
    File databasePath = ZeteticApplication.getInstance().getDatabasePath(databaseName);
    SQLiteOpenHelper helper = new TestHelper(ZeteticApplication.getInstance(), databasePath.getPath());
    database = helper.getWritableDatabase(key);
    database.execSQL("insert into t1(a,b) values(?, ?);", new Object[]{1, 2});
    database.close();
    helper.close();

    helper = new TestHelper(ZeteticApplication.getInstance(), databasePath.getPath());
    database = helper.getWritableDatabase(key);
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
    return "SQLiteOpenHelper with Byte Array Key";
  }

  private class TestHelper extends SQLiteOpenHelper {

    private static final int version = 1;

    public TestHelper(Context context, String databasePath){
      super(context, databaseName, null, version, null);
    }

    public TestHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
      super(context, databaseName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
      database.execSQL("create table t1(a,b);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
  }

}
