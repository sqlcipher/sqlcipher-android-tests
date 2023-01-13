package net.zetetic.tests;

import android.content.ContentValues;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

class PreventUpdateWithNullWhereArgsFromThrowingExceptionTest extends SQLCipherTest {

   @Override
   public boolean execute(SQLiteDatabase database) {
      try {
         database.execSQL("create table t1(a,b);");
         database.execSQL("insert into t1(a,b) values(?, ?)", new Object[]{"foo", "bar"});
         ContentValues values = new ContentValues();
         values.put("b", "baz");
         database.update("t1", SQLiteDatabase.CONFLICT_ABORT, values, null, null);
         long count = 0;
         Cursor cursor = database.query("select count(*) from t1 where b = ?;", new Object[]{"baz"});
         if(cursor != null && cursor.moveToNext()){
            count = cursor.getLong(0);
         }
         return count == 1;
      }
      catch (Exception ex){
         return false;
      }
   }

   @Override
   public String getName() {
      return "Prevent update with null where args from throwing";
   }
}
