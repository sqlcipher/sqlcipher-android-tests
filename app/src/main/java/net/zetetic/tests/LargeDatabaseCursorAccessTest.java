package net.zetetic.tests;

import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.CursorWindow;
import net.sqlcipher.CustomCursorWindowAllocation;
import net.sqlcipher.database.SQLiteDatabase;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LargeDatabaseCursorAccessTest extends SQLCipherTest {

  @Override
  public boolean execute(SQLiteDatabase database) {
    try {
      int rowCount = 1000;
      long windowAllocationSize = 1024 * 1024 / 20;
      buildDatabase(database, rowCount, 30, new RowColumnValueBuilder() {
        @Override
        public Object buildRowColumnValue(String[] columns, int row, int column) {
          try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            String columnName = columns[column];
            String value = String.format("%s%d", columnName, row);
            return digest.digest(value.getBytes("UTF-8"));
          } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
          }
        }
      });

      Integer[] randomRows = generateRandomNumbers(rowCount, rowCount);
      CursorWindow.setCursorWindowAllocation(new CustomCursorWindowAllocation(windowAllocationSize, 0, windowAllocationSize));
      Cursor cursor = database.rawQuery("SELECT * FROM t1;", null);
      MessageDigest digest = MessageDigest.getInstance("SHA-1");
      int row = 0;
      Log.i(TAG, "Walking cursor forward");
      while(cursor.moveToNext()){
        if (!CompareDigestForAllColumns(cursor, digest, row)) return false;
        row++;
      }
      Log.i(TAG, "Walking cursor backward");
      while(cursor.moveToPrevious()){
        row--;
        if (!CompareDigestForAllColumns(cursor, digest, row)) return false;
      }
      Log.i(TAG, "Walking cursor randomly");
      for(int randomRow : randomRows){
        cursor.moveToPosition(randomRow);
        if (!CompareDigestForAllColumns(cursor, digest, randomRow)) return false;
      }

    } catch (Exception e){
      return false;
    }
    return true;
  }

  @Override
  public String getName() {
    return "Large Database Cursor Access Test";
  }

  private boolean CompareDigestForAllColumns(Cursor cursor, MessageDigest digest, int row) throws UnsupportedEncodingException {
    int columnCount = cursor.getColumnCount();
    for(int column = 0; column < columnCount; column++){
      Log.i(TAG, String.format("Comparing SHA-1 digest for row:%d", row));
      String columnName = cursor.getColumnName(column);
      byte[] actual = cursor.getBlob(column);
      String value = String.format("%s%d", columnName, row);
      byte[] expected = digest.digest(value.getBytes("UTF-8"));
      if(!Arrays.equals(actual, expected)){
        Log.e(TAG, String.format("SHA-1 digest mismatch for row:%d column:%d", row, column));
        return false;
      }
    }
    return true;
  }

  private Integer[] generateRandomNumbers(int max, int times){
    SecureRandom random = new SecureRandom();
    List<Integer> numbers = new ArrayList<>();
    for(int index = 0; index < times; index++){
      boolean alreadyExists;
      do {
        int value = random.nextInt(max);
        alreadyExists = numbers.contains(value);
        if(!alreadyExists){
          numbers.add(value);
        }
      } while(alreadyExists);
    }
    return numbers.toArray(new Integer[0]);
  }
}
