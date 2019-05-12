package net.zetetic.tests.support;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class NullQueryResultTest extends SupportTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        
        database.execSQL("create table t1(a,b);");
        database.execSQL("insert into t1(a,b) values (?, ?)", new Object[]{"foo", null});
        database.execSQL("insert into t1(a,b) values (?, ?)", new Object[]{"bar", null});
        Cursor cursor = database.rawQuery("select a from t1", null);
        StringBuilder buffer = new StringBuilder();
        while(cursor.moveToNext()){
            buffer.append(cursor.getString(0));
        }
        cursor.close();
        return buffer.toString().length() > 0;
    }

    @Override
    public String getName() {
        return "Null Query Test";
    }
}
