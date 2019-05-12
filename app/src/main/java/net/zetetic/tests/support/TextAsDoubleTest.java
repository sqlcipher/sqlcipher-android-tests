package net.zetetic.tests.support;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class TextAsDoubleTest extends SupportTest {
    @Override
    public boolean execute(SQLiteDatabase database) {
        database.execSQL("create table t1(a TEXT);");
        database.execSQL("insert into t1(a) values(3.14159265359);");
        Cursor cursor = database.rawQuery("select * from t1;", new String[]{});
        if(cursor != null){
            cursor.moveToFirst();
            double value = cursor.getDouble(0);
            return value == 3.14159265359;
        }
        return false;
    }

    @Override
    public String getName() {
        return "Text As Double Test";
    }
}
