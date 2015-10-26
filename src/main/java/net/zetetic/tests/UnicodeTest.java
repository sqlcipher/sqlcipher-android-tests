package net.zetetic.tests;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;
import java.io.UnsupportedEncodingException;

public class UnicodeTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {
        String expected = "КАКОЙ-ТО КИРИЛЛИЧЕСКИЙ ТЕКСТ"; // SOME Cyrillic TEXT
        String actual = "";

        Cursor result = database.rawQuery("select UPPER('Какой-то кириллический текст') as u1", new String[]{});
        if (result != null) {
            result.moveToFirst();
            actual = result.getString(0);
            result.close();
        }
        if (!actual.equals(expected)) return false;

        if (android.os.Build.VERSION.SDK_INT >= 23) { // Android M
            // This will crash on Android releases 1.X-5.X due the following Android bug:
            // https://code.google.com/p/android/issues/detail?id=81341
            SQLiteStatement st = database.compileStatement("SELECT '\uD83D\uDE03'"); // SMILING FACE (MOUTH OPEN)
            String res = st.simpleQueryForString();
            if (!res.equals("\uD83D\uDE03")) return false;
        }

        // all ok:
        return true;
    }

    @Override
    public String getName() {
        return "Unicode (ICU) Test";
    }
}
