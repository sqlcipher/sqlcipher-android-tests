package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class BeginTransactionTest extends SupportTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.beginTransaction();
    database.endTransaction();
    return true;
  }

  @Override
  public String getName() {
    return "Begin transaction test";
  }
}
