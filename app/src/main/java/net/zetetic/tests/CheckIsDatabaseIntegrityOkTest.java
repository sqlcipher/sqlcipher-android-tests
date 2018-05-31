package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class CheckIsDatabaseIntegrityOkTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    return database.isDatabaseIntegrityOk();
  }

  @Override
  public String getName() {
    return "Check Database Integrity";
  }
}
