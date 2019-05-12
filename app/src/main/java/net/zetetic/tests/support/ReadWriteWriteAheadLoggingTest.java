package net.zetetic.tests.support;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class ReadWriteWriteAheadLoggingTest extends SupportTest {
  @Override
  public boolean execute(final SQLiteDatabase database) {

    try {
      final int[] a = new int[1];
      final int[] b = new int[1];
      database.setLockingEnabled(false);
      boolean walEnabled = database.enableWriteAheadLogging();
      if (!walEnabled) return false;

      //database.execSQL("PRAGMA read_uncommitted = 1;");

      database.execSQL("CREATE TABLE t1(a,b)");
      database.rawQuery("INSERT INTO t1(a,b) VALUES(?,?);", new Object[]{1, 2});
      database.beginTransaction();
      //database.beginTransactionNonExclusive();
      database.rawQuery("DELETE FROM t1 WHERE a = ?;", new Object[]{1});
      Thread t = new Thread(new Runnable() {
        @Override
        public void run() {
          Cursor cursor = database.rawQuery("SELECT COUNT(*) FROM t1 WHERE a = ?;", new Object[]{1});
          if (cursor != null && cursor.moveToFirst()) {
            a[0] = cursor.getInt(0);
            b[0] = cursor.getInt(0);
            log(String.format("Retrieved %d rows back", a[0]));
          }
        }
      });
      t.start();
      t.join();
      database.setTransactionSuccessful();
      database.endTransaction();
      //return a[0] == 1 && b[0] == 2;
      return a[0] == 0 && b[0] == 0;
    } catch (InterruptedException ex){
      return false;
    }
  }

  @Override
  public String getName() {
    return "Read/Write WAL Test";
  }
}
