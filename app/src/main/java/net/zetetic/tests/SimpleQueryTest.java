package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;

import java.util.UUID;

public class SimpleQueryTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.execSQL("create table t1(a,b);");
    database.execSQL("insert into t1(a, b) values(?, ?);",
        new Object[]{UUID.randomUUID().toString(), "foo"});
    database.execSQL("insert into t1(a, b) values(?, ?);",
        new Object[]{UUID.randomUUID().toString(), "bar"});
    int count = 0;
    Cursor cursor = database.rawQuery("select * from t1 where a = ?1 or ?1 = -1;",
        new Object[]{-1});
    if(cursor != null){
      while (cursor.moveToNext()){
        count++;
      }
      cursor.close();
    }

//    Works
//    SQLiteStatement statement = database.compileStatement("select count(*) from t1 where a = ?1 or ?1 = -1;");
//    statement.
//    statement.bindLong(1, -1);
//    long count = statement.simpleQueryForLong();
//    statement.close();
    return count == 2;
  }

  @Override
  public String getName() {
    return "Simple Query Test";
  }
}
