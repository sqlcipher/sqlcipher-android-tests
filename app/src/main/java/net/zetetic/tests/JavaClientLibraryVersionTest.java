package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class
JavaClientLibraryVersionTest extends SQLCipherTest {

  private final String EXPECTED_SQLCIPHER_ANDROID_VERSION = "4.4.3";

  @Override
  public boolean execute(SQLiteDatabase database) {
    setMessage(String.format("Report:%s", SQLiteDatabase.SQLCIPHER_ANDROID_VERSION));
    return SQLiteDatabase.SQLCIPHER_ANDROID_VERSION.equals(EXPECTED_SQLCIPHER_ANDROID_VERSION);
  }

  @Override
  public String getName() {
    return "Java Client Library Version Test";
  }
}
