package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class CheckIsDatabaseIntegrityOkTest extends SupportTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    return database.isDatabaseIntegrityOk();
  }

  @Override
  public String getName() {
    return "Check Database Integrity";
  }
}
