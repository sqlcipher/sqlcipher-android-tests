package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.util.UUID;

public class WriteAheadLoggingWithAttachedDatabaseTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    UUID name = UUID.randomUUID();
    File databasePath = ZeteticApplication.getInstance().getDatabasePath(name.toString());
    database.execSQL("ATTACH DATABASE ? as foo;", new Object[]{databasePath.getAbsolutePath()});
    boolean result = database.enableWriteAheadLogging();
    return result == false;
  }

  @Override
  public String getName() {
    return "Disallow WAL Mode with Attached DB";
  }
}
