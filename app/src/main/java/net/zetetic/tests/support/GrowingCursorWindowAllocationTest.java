package net.zetetic.tests.support;

import net.sqlcipher.Cursor;
import net.sqlcipher.CursorWindow;
import net.sqlcipher.CursorWindowAllocation;
import net.sqlcipher.CustomCursorWindowAllocation;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.RowColumnValueBuilder;
import net.zetetic.tests.SQLCipherTest;

public class GrowingCursorWindowAllocationTest extends SupportTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    try {
      int rowCount = 0;
      int rows = 20000;
      long intialAllocationSize = 128 * 1024;
      long growthAllocationSize = 1024 * 1024;
      long maxAllocationSize = 4 * 1024 * 1024;
      final int dataSize = 2000;
      CursorWindowAllocation fixedAllocation =
          new CustomCursorWindowAllocation(intialAllocationSize, growthAllocationSize, maxAllocationSize);
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
    return "Growing Cursor Window Allocation Test";
  }
}
