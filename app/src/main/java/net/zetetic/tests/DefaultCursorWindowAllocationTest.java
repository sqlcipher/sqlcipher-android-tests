package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class DefaultCursorWindowAllocationTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    try {
      int rowCount = 0;
      int rows = 10000;
      //final int dataSize = 2048;
      final int dataSize = 16384;
      buildDatabase(database, rows, 1, new RowColumnValueBuilder() {
        @Override
        public Object buildRowColumnValue(String[] columns, int row, int column) {
          return generateRandomByteArray(dataSize);
        }
      });
      rows = 1;
      Cursor cursor = database.rawQuery("SELECT count(length(a)) FROM t1;", new Object[]{});
      if(cursor == null) return false;
      while(cursor.moveToNext()){
        //byte[] data = cursor.getBlob(0);
        int size = cursor.getInt(0);
        cursor.close();
//        if(data.length != dataSize) {
//          cursor.close();
//          return false;
//        }
        rowCount++;
      }
      cursor.close();
      return rowCount == rows;
    } catch (Exception e){
      String message = String.format("Error:%s", e.getMessage());
      log(message);
      setMessage(message);
      return false;
    }
  }

  @Override
  public String getName() {
    return "Default cursor window allocation test";
  }
}
