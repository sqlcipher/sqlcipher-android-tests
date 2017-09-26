package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class BeginTransactionTest extends SQLCipherTest {
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
