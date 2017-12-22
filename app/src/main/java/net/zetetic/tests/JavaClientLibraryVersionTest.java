package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class JavaClientLibraryVersionTest extends SQLCipherTest {

  String expectedClientLibraryVersion = "3.5.8";

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