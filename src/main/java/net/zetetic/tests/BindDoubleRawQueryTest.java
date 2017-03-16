package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class BindDoubleRawQueryTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.execSQL("create table t1(a,b);");
    database.execSQL("insert into t1(a,b) values(?, ?);", new Object[]{"one for the money", 2.0d});
    Cursor cursor = database.rawQuery("select * from t1 where b = ?;", new Object[]{2.0d});
    if(cursor != null){
      if(cursor.moveToFirst()) {
        String a = cursor.getString(0);
        Double b = cursor.getDouble(1);
        cursor.close();
        return a.equals("one for the money") && b == 2.0d;
      }
    }
    return false;
  }

  @Override
  public String getName() {
    return "Bind Double for RawQuery Test";
  }
}
