package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.security.SecureRandom;
import java.util.Arrays;

public class BindByteArrayRawQueryTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {

    SecureRandom random = new SecureRandom();
    byte[] randomData = new byte[20];
    random.nextBytes(randomData);
    database.execSQL("create table t1(a,b);");
    database.execSQL("insert into t1(a,b) values(?, ?);", new Object[]{"one for the money", randomData});
    Cursor cursor = database.rawQuery("select * from t1 where b = ?;", new Object[]{randomData});
    if(cursor != null){
      if(cursor.moveToFirst()) {
        String a = cursor.getString(0);
        byte[] b = cursor.getBlob(1);
        cursor.close();
        return a.equals("one for the money") && Arrays.equals(randomData, b);
      }
    }
    return false;
  }

  @Override
  public String getName() {
    return "Bind Byte Array for RawQuery Test";
  }
}
