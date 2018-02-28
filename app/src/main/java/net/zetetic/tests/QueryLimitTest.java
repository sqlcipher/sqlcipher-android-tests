package net.zetetic.tests;

import android.content.ContentValues;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

public class QueryLimitTest extends SQLCipherTest {

  @Override
  public boolean execute(SQLiteDatabase database) {

    String create = "CREATE TABLE t1(id ID INTEGER(20) PRIMARY KEY, name VARCHAR(50));";
    String query1 = "SELECT id, name FROM t1 ORDER BY ID ASC LIMIT 0, 20;";
    String query2 = "SELECT id, name FROM t1 ORDER BY ID ASC LIMIT 20, 20;";
    database.rawExecSQL(create);
    database.beginTransaction();
    for(int index = 0; index < 30; index++){
      ContentValues values = new ContentValues();
      values.put("id", String.valueOf(index));
      values.put("name", String.format("name%d", index));
      database.insert("t1", null, values);
    }
    database.setTransactionSuccessful();
    database.endTransaction();

    int query1FirstId = -1;
    int query2FirstId = -1;
    int query1RowCount = 0;
    int query2RowCount = 0;

    Cursor cursor1 = database.rawQuery(query1, null);
    if(cursor1 != null){
      while (cursor1.moveToNext()){
        if(query1RowCount == 0){
          query1FirstId = cursor1.getInt(cursor1.getColumnIndex("id"));
        }
        query1RowCount++;
      }
      cursor1.close();
    }
    Cursor cursor2 = database.rawQuery(query2, null);
    if(cursor2 != null){
      while(cursor2.moveToNext()){
        if(query2RowCount == 0){
          query2FirstId = cursor2.getInt(cursor2.getColumnIndex("id"));
        }
        query2RowCount++;
      }
      cursor2.close();
    }
    return query1FirstId == 0 && query1RowCount == 20 &&
        query2FirstId == 20 && query2RowCount == 10;
  }

  @Override
  public String getName() {
    return "Query Limit Test";
  }
}
