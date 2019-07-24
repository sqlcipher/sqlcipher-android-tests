package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class JavaClientLibraryVersionTest extends SupportTest {

  String expectedClientLibraryVersion = "4.2.0";

  @Override
  public boolean execute(SQLiteDatabase database) {
    setMessage(String.format("Report:%s", SQLiteDatabase.SQLCIPHER_ANDROID_VERSION));
    return SQLiteDatabase.SQLCIPHER_ANDROID_VERSION.equals(expectedClientLibraryVersion);
  }

  @Override
  public String getName() {
    return "Java Client Library Version Test";
  }
}