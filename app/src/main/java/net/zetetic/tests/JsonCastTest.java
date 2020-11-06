package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class JsonCastTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    String name = "Bob Smith", queryName = "";
    String query = String.format("select cast(json_extract('{\"user\":\"%s\"}','$.user') as TEXT);", name);
    Cursor cursor = database.rawQuery(query, new Object[]{});
    if(cursor != null && cursor.moveToFirst()){
      queryName = cursor.getString(0);
      cursor.close();
    }
    return name.equals(queryName);
  }

  @Override
  public String getName() {
    return "JSON cast test";
  }
}
