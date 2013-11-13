package net.zetetic;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;


public class QueryHelper {

    public static String singleValueFromQuery(SQLiteDatabase database, String query){
        Cursor cursor = database.rawQuery(query, new String[]{});
        String value = "";
        if(cursor != null){
            cursor.moveToFirst();
            value = cursor.getString(0);
            cursor.close();
        }
        return value;
    }
}
