package net.zetetic.tests;

import android.content.ContentValues;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

public class QueryLimitTest extends SQLCipherTest {

  @Override
  public boolean execute(SQLiteDatabase database) {

    int query1FirstId = -1;
    int query1RowCount = 0;
    int limit = 20;
    int offset = 5;
    int recordsToInsertInSourceTable = 30;
    String create = "CREATE TABLE source(id INTEGER(20) PRIMARY KEY, name VARCHAR(50));";
    String other = "CREATE TABLE destination(id INTEGER(20) PRIMARY KEY, name VARCHAR(50));";
    String insert = String.format("INSERT INTO destination(id, name) SELECT * FROM source ORDER BY ID ASC LIMIT %d OFFSET %d;",
                                  limit, offset);
    String query1 = "SELECT * FROM destination ORDER BY id ASC;";

    database.rawExecSQL(create);
    database.beginTransaction();
    for(int index = 0; index < recordsToInsertInSourceTable; index++){
      ContentValues values = new ContentValues();
      values.put("id", String.valueOf(index));
      values.put("name", String.format("name%d", index));
      database.insert("source", null, values);
    }
    database.setTransactionSuccessful();
    database.endTransaction();

    database.execSQL(other);
    database.execSQL(insert);

    Cursor cursor = database.rawQuery(query1, null);
    if(cursor != null){
      while (cursor.moveToNext()){
        if(query1FirstId == -1) {
          query1FirstId = cursor.getInt(cursor.getColumnIndex("id"));
        }
        query1RowCount++;
        log(String.format("id:%d name:%s",
            cursor.getInt(cursor.getColumnIndex("id")),
            cursor.getString(cursor.getColumnIndex("name"))));
      }
      cursor.close();
    }
    return query1FirstId == offset && query1RowCount == limit;
  }

  @Override
  public String getName() {
    return "Query Limit Test";
  }
}
