package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class TextAsIntegerTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.execSQL("create table t1(a TEXT);");
        database.execSQL("insert into t1(a) values(15);");
        Cursor cursor = database.rawQuery("select * from t1;", new String[]{});
        if(cursor != null){
            cursor.moveToFirst();
            int value = cursor.getInt(0);
            return value == 15;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Text as Integer Test";
    }
}
