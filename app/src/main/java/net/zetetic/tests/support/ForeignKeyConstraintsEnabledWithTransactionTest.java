package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class ForeignKeyConstraintsEnabledWithTransactionTest extends SupportTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.beginTransaction();
    try {
      database.setForeignKeyConstraintsEnabled(true);
    }catch (IllegalStateException ex){
      if(ex.getMessage().equals("Foreign key constraints may not be changed while in a transaction")) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getName() {
    return "Disallow foreign key constraints in transaction";
  }
}
