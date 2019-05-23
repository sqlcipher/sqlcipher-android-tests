package net.zetetic;

import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;


public class QueryHelper {

    public static String singleValueFromQuery(SupportSQLiteDatabase database, String query){
        Cursor cursor = database.query(query, new String[]{});
        String value = "";
        if(cursor != null){
            cursor.moveToFirst();
            value = cursor.getString(0);
            cursor.close();
        }
        return value;
    }

    public static int singleIntegerValueFromQuery(SupportSQLiteDatabase database, String query){
        Cursor cursor = database.query(query, new String[]{});
        int value = 0;
        if(cursor != null){
            cursor.moveToFirst();
            value = cursor.getInt(0);
            cursor.close();
        }
        return value;
    }
}
