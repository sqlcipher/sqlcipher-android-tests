package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class JavaClientLibraryVersionTest extends SupportTest {

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
