package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class TransactionNonExclusiveTest extends SQLCipherTest {

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
