package net.zetetic.tests;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

class ResetQueryCacheFinalizesSqlStatementAndReleasesReferenceTest extends SQLCipherTest {
   @Override
   public boolean execute(SQLiteDatabase database) {
      int initialColumnCount = 0, updatedColumnCount = 0;
      int initialExpectedColumnCount = 2;
      int updatedColumnExpectedCount = 3;
      database.execSQL("create table t1(a, b);");
      database.execSQL("insert into t1(a,b) values(?, ?);",
        new Object[]{"foo", "bar"});
      Cursor cursor = database.query("t1", null,null,null,null,null,null);
      if(cursor != null && cursor.moveToNext()){
         initialColumnCount = cursor.getColumnCount();
         cursor.close();
      }
      database.resetCompiledSqlCache();
      database.execSQL("alter table t1 add c text null;");
      cursor = database.query("t1", null,null,null,null,null,null);
      if(cursor != null && cursor.moveToNext()){
         updatedColumnCount = cursor.getColumnCount();
         cursor.close();
      }
      return initialColumnCount == initialExpectedColumnCount
        && updatedColumnCount == updatedColumnExpectedCount;
   }

   @Override
   public String getName() {
      return "Reset Query cache";
   }
}
