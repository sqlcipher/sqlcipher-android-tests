package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class SQLiteOpenHelperConfigureTest implements ISupportTest {
  @Override
  public TestResult run() {
    TestResult result = new TestResult(getName(), false);

    byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
    SupportFactory factory = new SupportFactory(passphrase);
    TestCallback callback = new TestCallback(2);
    SupportSQLiteOpenHelper.Configuration cfg =
      SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
        .name(ZeteticApplication.DATABASE_NAME)
        .callback(callback)
        .build();
    SupportSQLiteOpenHelper helper = factory.create(cfg);

    helper.getWritableDatabase();
    result.setResult(callback.onConfigureCalled);
    helper.close();

    return result;
  }

  @Override
  public String getName() {
    return "SQLiteOpenHelper Configure Test";
  }

  private static class TestCallback extends SupportSQLiteOpenHelper.Callback {
    boolean onConfigureCalled = false;

    TestCallback(int version) {
      super(version);
    }

    @Override
    public void onCreate(SupportSQLiteDatabase db) {
      // unused
    }

    @Override
    public void onUpgrade(SupportSQLiteDatabase db, int oldVersion,
                          int newVersion) {
      // unused
    }

    @Override
    public void onConfigure(SupportSQLiteDatabase db) {
      onConfigureCalled = true;
    }
  }
}
