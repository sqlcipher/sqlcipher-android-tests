package net.zetetic.tests;

import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

public class LoopingInsertTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.execSQL("CREATE TABLE some_table(name TEXT, surname TEXT);");
    long startTime = System.currentTimeMillis();
    database.execSQL("begin;");
    for(int index = 0; index < 10000; index++){
      database.execSQL("insert into some_table(name, surname) values(?, ?)",
          new Object[]{"one for the money", "two for the show"});
    }
    database.execSQL("commit;");
    long diff = System.currentTimeMillis() - startTime;
    Log.e(TAG, String.format("Inserted in: %d ms", diff));
    return true;
  }

  @Override
  public String getName() {
    return "Looping Insert Test";
  }
}
