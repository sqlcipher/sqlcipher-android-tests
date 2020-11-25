package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import java.util.Locale;

// Provided via GitHub Issue: https://github.com/sqlcipher/android-database-sqlcipher/issues/526
public class SummingStepTest extends SQLCipherTest {
  @Override
  public boolean execute(SQLiteDatabase database) {
    database.execSQL("create table sample_session(_id , start_time TEXT , client_id TEXT, type_id TEXT, value TEXT);");
    database.execSQL("create table sample_point(_id ,start_time TEXT , client_id TEXT, type_id TEXT, value TEXT);");
    database.execSQL("insert into sample_session(_id , start_time ,client_id, type_id, value) values(1, 1000,5 ,2, 0);");
    database.execSQL("insert into sample_point(_id , start_time ,client_id, type_id, value) values(1, 1000,5 ,2, 46);");
    database.execSQL("insert into sample_point(_id , start_time ,client_id, type_id, value) values(2, 1000,6 ,2, 46);");
    String query = "select sample_session._id,sample_session.start_time,sample_session.type_id, sample_session.client_id," +
        "sum ( case sample_point.type_id when 2 then sample_point.value else 0 end) as step, sum ( case sample_point.type_id when 4 then sample_point.value else 0 end) as calorie, sum ( case sample_point.type_id when 3 then sample_point.value else 0 end) as distance, " +
        "sum ( case sample_point.type_id when 5 then sample_point.value else 0 end) as altitude_offset from sample_session INNER JOIN sample_point ON sample_session.start_time = sample_point.start_time and sample_session.client_id = sample_point.client_id " +
        "where sample_session.client_id =? and sample_point.client_id =?  and sample_point.type_id in ( ?,?,?,? ) " +
        "group by sample_session.start_time ORDER BY sample_session.start_time DESC  limit 0,1000";
    Cursor cursor = database.rawQuery(query, new String[]{"5","5","2","4","3","5"});
    if(cursor != null){
      cursor.moveToFirst();
      int index = cursor.getColumnIndex("step");
      long value = cursor.getInt(index);
      setMessage(String.format(Locale.getDefault(), "Expected:%d Returned:%d", 46, value));
      return value == 46;
    }
    return false;
  }

  @Override
  public String getName() {
    return "SummingStepTest Test";
  }
}
