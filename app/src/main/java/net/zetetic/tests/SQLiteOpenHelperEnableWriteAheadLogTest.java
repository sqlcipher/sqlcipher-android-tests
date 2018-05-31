package net.zetetic.tests;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.util.UUID;

public class SQLiteOpenHelperEnableWriteAheadLogTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {

    database.close();
    UUID name = UUID.randomUUID();
    File databasePath = ZeteticApplication.getInstance().getDatabasePath(name.toString());
    DatabaseHelper helper = new DatabaseHelper(ZeteticApplication.getInstance(),
        databasePath.getAbsolutePath());
    database = helper.getWritableDatabase("foo");
    helper.setWriteAheadLoggingEnabled(true);
    return database.isWriteAheadLoggingEnabled();
  }

  @Override
  public String getName() {
    return "SQLiteOpenHelper Enable WAL";
  }

  class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String databasePath) {
      super(context, ZeteticApplication.DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) { }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){}
  }

}
