package net.zetetic.tests.support;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class RawExecSQLTest extends SupportTest {
    @Override
    public boolean execute(SQLiteDatabase database) {

        String actual = "";
        String value = "hey";
        database.rawExecSQL("create table t1(a)");
        database.execSQL("insert into t1(a) values (?)", new Object[]{value});
        Cursor result = database.rawQuery("select * from t1", new String[]{});
        if(result != null){
            result.moveToFirst();
            actual = result.getString(0);
            result.close();
        }
        return actual.equals(value);
    }

    @Override
    public String getName() {
        return "rawExecSQL Test";
    }
}
