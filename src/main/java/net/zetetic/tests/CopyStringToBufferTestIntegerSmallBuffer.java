package net.zetetic.tests;

import android.database.CharArrayBuffer;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class CopyStringToBufferTestIntegerSmallBuffer extends SQLCipherTest {

    private CharArrayBuffer charArrayBuffer = new CharArrayBuffer(1);

    @Override
    public boolean execute(SQLiteDatabase database) {

        database.execSQL("create table t1(a INTEGER, b INTEGER);");
        database.execSQL("insert into t1(a,b) values(123, 456);");
        Cursor cursor = database.rawQuery("select * from t1;", new String[]{});
        if(cursor != null){
            cursor.moveToFirst();
            cursor.copyStringToBuffer(1, charArrayBuffer);
            String actualValue = new String(charArrayBuffer.data, 0, charArrayBuffer.sizeCopied);
            return "456".equals(actualValue);
        }
        return false;

    }

    @Override
    public String getName() {
        return "Copy String To Buffer Test Integer Small Buffer";
    }
}
