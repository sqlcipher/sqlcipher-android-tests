package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class FTS5Test extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.execSQL("CREATE VIRTUAL TABLE email USING fts5(sender, title, body);");
    database.execSQL("insert into email(sender, title, body) values(?, ?, ?);",
        new Object[]{"foo@bar.com", "Test Email", "This is a test email message."});
    Cursor cursor = database.rawQuery("select * from email where email match ?;", new String[]{"test"});
    if(cursor != null){
      cursor.moveToFirst();
      return cursor.getString(cursor.getColumnIndex("sender")).equals("foo@bar.com");
    }
    return false;
  }

  @Override
  public String getName() {
    return "FTS5 Test";
  }
}
