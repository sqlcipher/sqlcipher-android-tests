package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class DeleteTableWithNullWhereArgsTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    String[] args = null;
    int rowsDeleted = 0;
    database.rawExecSQL("create table t1(a,b);");
    database.execSQL("insert into t1(a,b) values(?, ?);", new Object[]{1, 2});
    rowsDeleted = database.delete("t1", "a = 1", args);
    return rowsDeleted == 1;
  }

  @Override
  public String getName() {
    return "Delete Table Test";
  }
}
