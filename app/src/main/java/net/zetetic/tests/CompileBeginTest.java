package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class CompileBeginTest extends SQLCipherTest {

  @Override
  public boolean execute(SQLiteDatabase database) {
    try {
      database.compileStatement("begin").execute();
      return true;
    }catch (Exception e){
      return false;
    }
  }

  @Override
  public String getName() {
    return "Compile Begin Test";
  }
}
