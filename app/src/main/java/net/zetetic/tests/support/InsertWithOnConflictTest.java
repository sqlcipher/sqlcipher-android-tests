package net.zetetic.tests.support;

import android.content.ContentValues;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class InsertWithOnConflictTest extends SupportTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.execSQL("create table user(_id integer primary key autoincrement, email text unique not null);");
    ContentValues values = new ContentValues();
    values.put("email", "foo@bar.com");
    long id = database.insertWithOnConflict("user", null, values,
        SQLiteDatabase.CONFLICT_IGNORE);
    long error = database.insertWithOnConflict("user", null, values,
        SQLiteDatabase.CONFLICT_IGNORE);
    return id == 1 && error == -1;
  }

  @Override
  public String getName() {
    return "Insert with OnConflict";
  }
}
