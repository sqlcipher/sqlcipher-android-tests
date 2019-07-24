package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;
import net.zetetic.tests.SQLCipherTest;

public class EnableForeignKeyConstraintsTest extends SupportTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    String initialState = getForeignKeyState(database);
    if(!initialState.equals("0")) return false;
    database.setForeignKeyConstraintsEnabled(true);
    String currentState = getForeignKeyState(database);
    if(!currentState.equals("1")) return false;
    return true;
  }

  private String getForeignKeyState(SQLiteDatabase database){
    return QueryHelper.singleValueFromQuery(database, "PRAGMA foreign_keys");
  }

  @Override
  public String getName() {
    return "Enable Foreign Key Constraints";
  }
}
