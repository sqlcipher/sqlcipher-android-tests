package net.zetetic.tests;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class UnicodeTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {

        String expected = "КАКОЙ-ТО КИРИЛЛИЧЕСКИЙ ТЕКСТ"; // SOME Cyrillic TEXT
        String actual = "";
        Cursor result = database.rawQuery("select UPPER('Какой-то кириллический текст') as u1", new String[]{});
        if(result != null){
            result.moveToFirst();
            actual = result.getString(0);
            result.close();
        }
        return actual.equals(expected);
    }

    @Override
    public String getName() {
        return "Unicode (ICU) Test";
    }
}
