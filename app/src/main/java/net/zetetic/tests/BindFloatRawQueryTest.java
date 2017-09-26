package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class BindFloatRawQueryTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.execSQL("create table t1(a,b);");
    database.execSQL("insert into t1(a,b) values(?, ?);", new Object[]{"one for the money", 2.25f});
    Cursor cursor = database.rawQuery("select * from t1 where b = ?;", new Object[]{2.25f});
    if(cursor != null){
      if(cursor.moveToFirst()) {
        String a = cursor.getString(0);
        float b = cursor.getFloat(1);
        cursor.close();
        return a.equals("one for the money") && b == 2.25f;
      }
    }
    return false;
  }

  @Override
  public String getName() {
    return "Bind Float for RawQuery Test";
  }
}
