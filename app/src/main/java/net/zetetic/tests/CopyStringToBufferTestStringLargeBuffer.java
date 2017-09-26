package net.zetetic.tests;

import android.database.CharArrayBuffer;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class CopyStringToBufferTestStringLargeBuffer extends SQLCipherTest {

    private CharArrayBuffer charArrayBuffer = new CharArrayBuffer(128);

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.execSQL("create table t1(a TEXT, b TEXT);");
        database.execSQL("insert into t1(a,b) values(500, 500);");
        Cursor cursor = database.rawQuery("select * from t1;", new String[]{});
        if(cursor != null){
            cursor.moveToFirst();
            cursor.copyStringToBuffer(0, charArrayBuffer);
            String actualValue = new String(charArrayBuffer.data, 0, charArrayBuffer.sizeCopied);
            return "500".equals(actualValue);
        }
        return false;
    }

    @Override
    public String getName() {
        return "Copy String To Buffer Test String Large Buffer";
    }
}
