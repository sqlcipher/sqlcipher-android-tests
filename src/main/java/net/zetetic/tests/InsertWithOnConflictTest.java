package net.zetetic.tests;

import android.content.ContentValues;
import net.sqlcipher.database.SQLiteDatabase;

public class InsertWithOnConflictTest extends SQLCipherTest {
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
