package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class CheckIsWriteAheadLoggingEnabledTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.enableWriteAheadLogging();
    boolean statusWhenEnabled = database.isWriteAheadLoggingEnabled();
    database.disableWriteAheadLogging();
    boolean statusWhenDisabled = database.isWriteAheadLoggingEnabled();
    return statusWhenEnabled && !statusWhenDisabled;
  }

  @Override
  public String getName() {
    return "Test isWriteAheadLoggingEnabled";
  }
}
