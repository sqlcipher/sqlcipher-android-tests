package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class WriteAheadLoggingWithInMemoryDatabaseTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.close();
    database = SQLiteDatabase.openDatabase(SQLiteDatabase.MEMORY, "",
        null, SQLiteDatabase.OPEN_READWRITE);
    boolean result = database.enableWriteAheadLogging();
    return result == false;
  }

  @Override
  public String getName() {
    return "Disallow WAL Mode with in memory DB";
  }
}
