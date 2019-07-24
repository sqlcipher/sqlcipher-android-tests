package net.zetetic.tests.support;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;
import java.io.File;
import java.util.UUID;

public class SQLiteOpenHelperGetNameTest extends SupportTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    UUID name = UUID.randomUUID();
    File databasePath = ZeteticApplication.getInstance().getDatabasePath(name.toString());
    DatabaseHelper helper = new DatabaseHelper(ZeteticApplication.getInstance(),
        databasePath.getAbsolutePath());
    return databasePath.getAbsolutePath().equals(helper.getDatabaseName());
  }

  @Override
  public String getName() {
    return "SQLiteOpenHelper GetName Test";
  }

  class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String databasePath) {
      super(context, databasePath, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) { }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){}
  }

}
