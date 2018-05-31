package net.zetetic.tests;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.util.UUID;

public class SQLiteOpenHelperOnDowngradeTest extends SQLCipherTest {

  @Override
  public boolean execute(SQLiteDatabase database) {

    UUID name = UUID.randomUUID();
    String password = "foo";
    File databasePath = ZeteticApplication.getInstance().getDatabasePath(name.toString());
    DatabaseHelper helper = new DatabaseHelper(ZeteticApplication.getInstance(),
        databasePath.getAbsolutePath(), 2);
    helper.getWritableDatabase(password);
    if(helper.onDowngradeCalled) return false;
    helper.close();
    helper = new DatabaseHelper(ZeteticApplication.getInstance(), databasePath.getAbsolutePath(), 1);
    helper.getWritableDatabase(password);
    return helper.onDowngradeCalled;
  }

  @Override
  public String getName() {
    return "SQLiteOpenHelper OnDowngrade Test";
  }

  class DatabaseHelper extends SQLiteOpenHelper {

    public boolean onDowngradeCalled = false;

    public DatabaseHelper(Context context, String databasePath, int version) {
      super(context, ZeteticApplication.DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) { }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){}

    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion){
      onDowngradeCalled = true;
    }
  }
}
