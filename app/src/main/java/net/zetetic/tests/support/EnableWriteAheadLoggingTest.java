package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;
import net.zetetic.tests.SQLCipherTest;

public class EnableWriteAheadLoggingTest extends SupportTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    boolean result = database.enableWriteAheadLogging();
    String currentMode = QueryHelper.singleValueFromQuery(database, "PRAGMA journal_mode;");
    return result && currentMode.equals("wal");
  }

  @Override
  public String getName() {
    return "Enable WAL mode";
  }
}
