package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class TransactionNonExclusiveTest extends SupportTest {

  @Override
  public boolean execute(SQLiteDatabase database) {
    database.beginTransactionNonExclusive();
    database.execSQL("create table t1(a,b);");
    database.setTransactionSuccessful();
    database.endTransaction();
    return true;
  }

  @Override
  public String getName() {
    return "Transaction Immediate Mode";
  }
}
