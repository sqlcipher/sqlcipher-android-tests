package net.zetetic.tests.support;

import net.sqlcipher.Cursor;
import net.sqlcipher.CursorWindow;
import net.sqlcipher.CursorWindowAllocation;
import net.sqlcipher.CustomCursorWindowAllocation;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.RowColumnValueBuilder;
import net.zetetic.tests.SQLCipherTest;

public class FixedCursorWindowAllocationTest extends SupportTest {

  @Override
  public boolean execute(SQLiteDatabase database) {
    try {
      int rowCount = 0;
      int rows = 100;
      long allocationSize = 1024;
      final int dataSize = 491;
      CursorWindowAllocation fixedAllocation = new CustomCursorWindowAllocation(allocationSize, 0, allocationSize);
      CursorWindow.setCursorWindowAllocation(fixedAllocation);
      buildDatabase(database, rows, 1, new RowColumnValueBuilder() {
        @Override
        public Object buildRowColumnValue(String[] columns, int row, int column) {
          return generateRandomByteArray(dataSize);
        }
      });

      Cursor cursor = database.rawQuery("SELECT * FROM t1;", new Object[]{});
      if(cursor == null) return false;
      while(cursor.moveToNext()){
        byte[] data = cursor.getBlob(0);
        if(data.length != dataSize) {
          cursor.close();
          return false;
        }
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
    return "Small Cursor Window Allocation Test";
  }
}
