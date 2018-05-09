package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class RTreeTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    int id = 0;
    String create = "CREATE VIRTUAL TABLE demo_index USING rtree(id, minX, maxX, minY, maxY);";
    String insert = "INSERT INTO demo_index VALUES(?, ?, ?, ?, ?);";
    database.execSQL(create);
    database.execSQL(insert, new Object[]{1, -80.7749, -80.7747, 35.3776, 35.3778});
    Cursor cursor = database.rawQuery("SELECT * FROM demo_index WHERE maxY < ?;",
        new Object[]{36});
    if(cursor != null){
      cursor.moveToNext();
      id = cursor.getInt(0);
      cursor.close();
    }
    return id == 1;
  }

  @Override
  public String getName() {
    return "RTree Test";
  }
}
