package net.zetetic.tests.support;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class LoopingQueryTest extends SupportTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        int counter = 0;
        int iterations = 1000;
        database.execSQL("create table t1(a);");
        database.execSQL("insert into t1(a) values (?)", new Object[]{"foo"});
        StringBuilder buffer = new StringBuilder();
        while(counter < iterations){
            Cursor cursor = database.rawQuery("select * from t1", null);
            if(cursor != null){
                cursor.moveToFirst();
                buffer.append(cursor.getString(0));
                cursor.close();
            }
            counter++;
        }
        return buffer.toString().length() > 0;
    }

    @Override
    public String getName() {
        return "Looping Query Test";
    }
}
