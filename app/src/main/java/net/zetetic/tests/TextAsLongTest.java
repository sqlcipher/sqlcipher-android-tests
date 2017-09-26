package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class TextAsLongTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.execSQL("create table t1(a TEXT, b TEXT);");
        database.execSQL("insert into t1(a,b) values(500, 500);");
        Cursor cursor = database.rawQuery("select * from t1;", new String[]{});
        if(cursor != null){
            cursor.moveToFirst();
            long value = cursor.getLong(0);
            return value == 500;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Text as Long Test";
    }
}
