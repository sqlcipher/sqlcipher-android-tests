package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class VerifyOnUpgradeIsCalledTest implements ISupportTest {
  @Override
  public TestResult run() {
    TestResult result = new TestResult(getName(), false);

    byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
    SupportFactory factory = new SupportFactory(passphrase);
    TestCallback callback = new TestCallback(1);
    SupportSQLiteOpenHelper.Configuration cfg =
      SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
        .name(ZeteticApplication.DATABASE_NAME)
        .callback(callback)
        .build();
    SupportSQLiteOpenHelper helper = factory.create(cfg);

    helper.getWritableDatabase();

    if (callback.onUpgradeCalled) {
      result.setResult(false);
      return result;
    }

    helper.close();

    passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
    factory = new SupportFactory(passphrase);
    callback = new TestCallback(2);
    cfg =
      SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
        .name(ZeteticApplication.DATABASE_NAME)
        .callback(callback)
        .build();
    helper = factory.create(cfg);

    helper.getWritableDatabase();
    result.setResult(callback.onUpgradeCalled);
    helper.close();

    return result;
  }

  @Override
  public String getName() {
    return "Verify onUpgrade Is Called Test";
  }

  private static class TestCallback extends SupportSQLiteOpenHelper.Callback {
    boolean onUpgradeCalled = false;

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
      onUpgradeCalled = true;
    }
  }
}
