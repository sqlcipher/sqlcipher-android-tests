package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class NullRawQueryTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.execSQL("create table t1(a,b);");
    database.execSQL("insert into t1(a,b) values(?, ?);", new Object[]{"one for the money", "two for the show"});
    Cursor cursor = database.rawQuery("select * from t1;", null);
    if(cursor != null){
      if(cursor.moveToFirst()) {
        String a = cursor.getString(0);
        String b = cursor.getString(1);
        cursor.close();
        return a.equals("one for the money") && b.equals("two for the show");
      }
    }


    return false;
  }

  @Override
  public String getName() {
    return "Bind Null Raw Query Test";
  }
}
