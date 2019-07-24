package net.zetetic.tests.support;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;
import net.zetetic.tests.TestResult;
import java.io.File;
import java.util.UUID;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class SQLiteOpenHelperEnableWriteAheadLogAfterGetDatabaseTest implements ISupportTest {
  public TestResult run() {
    TestResult result = new TestResult(getName(), false);

    byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
    SupportFactory factory = new SupportFactory(passphrase);
    SupportSQLiteOpenHelper.Configuration cfg =
      SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
        .name(ZeteticApplication.DATABASE_NAME)
        .callback(new SupportSQLiteOpenHelper.Callback(1) {
          @Override
          public void onCreate(SupportSQLiteDatabase db) {
            // unused
          }

          @Override
          public void onUpgrade(SupportSQLiteDatabase db, int oldVersion,
                                int newVersion) {
            // unused
          }
        })
        .build();
    SupportSQLiteOpenHelper helper = factory.create(cfg);
    SupportSQLiteDatabase database = helper.getWritableDatabase();

    helper.setWriteAheadLoggingEnabled(true);
    result.setResult(database.isWriteAheadLoggingEnabled());
    helper.close();

    return result;
  }

  @Override
  public String getName() {
    return "SQLiteOpenHelper Enable WAL After Get DB";
  }
}
