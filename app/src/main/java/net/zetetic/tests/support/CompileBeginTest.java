package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class CompileBeginTest extends SupportTest {

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
