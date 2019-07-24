package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;
import net.zetetic.tests.SQLCipherTest;

public class DisableWriteAheadLoggingTest extends SupportTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    boolean result = database.enableWriteAheadLogging();
    String currentMode = getJournalModeState(database);
    if(!result || !currentMode.equals("wal")) return false;
    database.disableWriteAheadLogging();
    currentMode = getJournalModeState(database);
    return currentMode.equals("delete");
  }

  private String getJournalModeState(SQLiteDatabase database){
    return QueryHelper.singleValueFromQuery(database, "PRAGMA journal_mode;");
  }

  @Override
  public String getName() {
    return "Disable WAL mode";
  }
}
