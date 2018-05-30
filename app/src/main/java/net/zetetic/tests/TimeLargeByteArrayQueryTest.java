package net.zetetic.tests;

import android.util.Log;

import net.sqlcipher.Cursor;
import net.sqlcipher.CursorWindow;
import net.sqlcipher.CursorWindowAllocation;
import net.sqlcipher.CustomCursorWindowAllocation;
import net.sqlcipher.database.SQLiteDatabase;

import java.text.NumberFormat;
import java.util.Locale;

public class TimeLargeByteArrayQueryTest extends SQLCipherTest {

  long allocationSize = 1024 * 1024 * 4;
  CursorWindowAllocation allocation = new CustomCursorWindowAllocation(allocationSize, 0, allocationSize);

  @Override
  public boolean execute(SQLiteDatabase database) {

    buildDatabase(database, 200, 128, new RowColumnValueBuilder() {
      @Override
      public Object buildRowColumnValue(String[] columns, int row, int column) {
        return generateRandomByteArray(16);
      }
    });
    runSamplingScenario(database, 30, retrieveAllData);
    return true;
  }

  public interface CursorScenario {
    Stats run(Cursor cursor);
    String getName();
    CursorWindowAllocation getAllocation();
  }

  CursorScenario mixedRowAccess = new CursorScenario() {
    //int[] rowIndexes = new int[]{1, 500, 1000, 5000, 9999};
    //int[] rowIndexes = new int[]{1, 250, 750, 999};
    int[] rowIndexes = new int[]{18000, 9000, 4500, 1000};
    @Override
    public Stats run(Cursor cursor) {
      long totalBytes = 0;
      int columnCount = cursor.getColumnCount();
      for (int row = 0; row < rowIndexes.length; row++) {
        int index = rowIndexes[row];
        long start = System.nanoTime();
        cursor.moveToPosition(index);
        for (int column = 0; column < columnCount; column++) {
          byte[] data = cursor.getBlob(column);
          totalBytes += data.length;
        }
        long stop = System.nanoTime();
        Log.i(TAG, String.format("%s scenario read data for row:%d in %.2f seconds",
            getName(), index, toSeconds(start, stop)));
      }
      return new Stats(rowIndexes.length, totalBytes);
    }

    @Override
    public String getName() {
      return "Mixed row";
    }

    @Override
    public CursorWindowAllocation getAllocation() {
      return allocation;
    }
  };

    CursorScenario retrieveAllData = new CursorScenario() {
      @Override
      public Stats run(Cursor cursor) {
        long rows = 0;
        long totalBytes = 0;
        int columnCount = cursor.getColumnCount();
        while (cursor.moveToNext()) {
          rows++;
          byte[][] data = new byte[columnCount][];
          for (int column = 0; column < columnCount; column++) {
            data[column] = cursor.getBlob(column);
            totalBytes += data[column].length;
          }
        }
        return new Stats(rows, totalBytes);
      }

      @Override
      public String getName() {
        return "Full query";
      }

      @Override
      public CursorWindowAllocation getAllocation() {
        return allocation;
      }
    };

    private void runSamplingScenario(SQLiteDatabase database, int runs, CursorScenario scenario) {
      double totalQueryTime = 0;
      for (int run = 0; run < runs; run++) {
        Stats result = timeQuery(database, "SELECT * FROM t1;", scenario);
        String message = String.format(Locale.getDefault(),
            "%s sample:%d rows:%s time:%.2f seconds data:%s",
            scenario.getName(),
            run + 1,
            NumberFormat.getInstance().format(result.RowCount),
            result.QueryTime,
            bytesToString(result.TotalBytes));
        totalQueryTime += result.QueryTime;
        Log.i(TAG, message);
      }
      String message = String.format(Locale.getDefault(),
          "%s average cursor time:%.2f seconds for %d samples",
          scenario.getName(), totalQueryTime / runs, runs);
      Log.i(TAG, message);
      setMessage(message);
    }

    private Stats timeQuery(SQLiteDatabase database, String query, CursorScenario scenario) {
      long start = System.nanoTime();
      CursorWindow.setCursorWindowAllocation(scenario.getAllocation());
      Cursor cursor = database.rawQuery(query, new Object[]{});
      if (cursor == null) return new Stats();
      Stats stats = scenario.run(cursor);
      long stop = System.nanoTime();
      cursor.close();
      stats.QueryTime = toSeconds(start, stop);
      return stats;
    }

    private byte[] generateRandomByteArray(int size) {
      byte[] data = new byte[size];
      random.nextBytes(data);
      return data;
    }

    private String bytesToString(long bytes) {
      int unit = 1024;
      if (bytes < unit) return bytes + " B";
      int exp = (int) (Math.log(bytes) / Math.log(unit));
      String pre = String.valueOf("KMGTPE".charAt(exp - 1));
      return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    @Override
    public String getName() {
      return "Time Large Byte Array Query Test";
    }

    class Stats {
      public double QueryTime;
      public long RowCount;
      public long TotalBytes;

      public Stats(){}

      public Stats(long rowCount, long totalBytes){
        this.RowCount = rowCount;
        this.TotalBytes = totalBytes;
      }
    }
}