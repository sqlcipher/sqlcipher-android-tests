package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteTransactionListener;

public class TransactionWithListenerTest extends SQLCipherTest {

  boolean beginCalled = false;

  @Override
  public boolean execute(SQLiteDatabase database) {
    database.beginTransactionWithListener(listener);
    database.execSQL("create table t1(a,b);");
    database.setTransactionSuccessful();
    database.endTransaction();
    return beginCalled;
  }

  @Override
  public String getName() {
    return "Transaction Exclusive Mode";
  }

  SQLiteTransactionListener listener = new SQLiteTransactionListener() {
    @Override
    public void onBegin() {
      beginCalled = true;
    }

    @Override
    public void onCommit() {

    }

    @Override
    public void onRollback() {

    }
  };
}
