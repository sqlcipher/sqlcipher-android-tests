package net.zetetic.tests;

import android.database.CharArrayBuffer;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class CopyStringToBufferNullTest extends SQLCipherTest {

    private CharArrayBuffer charArrayBuffer = null;

    @Override
    public boolean execute(SQLiteDatabase database) {
        boolean result = false;
        try {
            database.execSQL("create table t1(a TEXT, b TEXT);");
            database.execSQL("insert into t1(a,b) values(500, 500);");
            Cursor cursor = database.rawQuery("select * from t1;", new String[]{});
            if(cursor != null){
                cursor.moveToFirst();
                cursor.copyStringToBuffer(0, charArrayBuffer);
                String actualValue = new String(charArrayBuffer.data, 0, charArrayBuffer.sizeCopied);
                "500".equals(actualValue);
            }
        } catch (IllegalArgumentException ex){
            result = true;
        } finally {
            return result;
        }

    }

    @Override
    public String getName() {
        return "Copy String To Buffer Null Test";
    }
}
