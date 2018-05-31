package net.zetetic.tests;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.util.UUID;

public class SQLiteOpenHelperConfigureTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {

    UUID name = UUID.randomUUID();
    File databasePath = ZeteticApplication.getInstance().getDatabasePath(name.toString());
    ConfigureHelper helper = new ConfigureHelper(ZeteticApplication.getInstance(),
        databasePath.getAbsolutePath());
    helper.getWritableDatabase("foo");
    return helper.onConfigureCalled;
  }

  @Override
  public String getName() {
    return "SQLiteOpenHelper Configure Test";
  }

  class ConfigureHelper extends SQLiteOpenHelper {

    public boolean onConfigureCalled = false;

    public ConfigureHelper(Context context, String databasePath) {
      super(context, ZeteticApplication.DATABASE_NAME, null, 1);
    }

    public void onConfigure(SQLiteDatabase database){
      onConfigureCalled = true;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
  }

}
