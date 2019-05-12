package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class CheckIsWriteAheadLoggingEnabledTest extends SupportTest {
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
