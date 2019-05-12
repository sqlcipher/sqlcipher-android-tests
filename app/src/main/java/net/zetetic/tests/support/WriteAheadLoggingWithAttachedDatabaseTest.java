package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;
import java.io.File;
import java.util.UUID;

public class WriteAheadLoggingWithAttachedDatabaseTest extends SupportTest {
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
