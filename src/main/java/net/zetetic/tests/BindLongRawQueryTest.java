package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class BindLongRawQueryTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.execSQL("create table t1(a,b);");
    database.execSQL("insert into t1(a,b) values(?, ?);", new Object[]{"one for the money", 2L});
    Cursor cursor = database.rawQuery("select * from t1 where b = ?;", new Object[]{2L});
    if(cursor != null){
      if(cursor.moveToFirst()) {
        String a = cursor.getString(0);
        long b = cursor.getLong(1);
        cursor.close();
        return a.equals("one for the money") && b == 2L;
      }
    }
    return false;
  }

  @Override
  public String getName() {
    return "Bind Long for RawQuery Test";
  }
}
