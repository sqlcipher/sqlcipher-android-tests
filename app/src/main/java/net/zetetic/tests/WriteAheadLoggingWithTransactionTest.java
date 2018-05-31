package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class WriteAheadLoggingWithTransactionTest extends SQLCipherTest {

  @Override
  public boolean execute(SQLiteDatabase database) {
    database.beginTransaction();
    try {
      database.enableWriteAheadLogging();
    } catch (IllegalStateException ex){
      if(ex.getMessage().equals("Write Ahead Logging cannot be enabled while in a transaction")) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getName() {
    return "Disallow WAL Mode while in transaction";
  }
}
