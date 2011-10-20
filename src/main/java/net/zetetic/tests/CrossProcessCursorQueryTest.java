package net.zetetic.tests;

import android.app.Activity;
import android.net.Uri;
import info.guardianproject.database.sqlcipher.SQLiteDatabase;
import net.zetetic.ZeteticApplication;
import net.zetetic.ZeteticContentProvider;

public class CrossProcessCursorQueryTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {

        Activity activity = ZeteticApplication.getInstance().getCurrentActivity();
        Uri providerUri = ZeteticContentProvider.CONTENT_URI;
        android.database.Cursor cursor = activity.managedQuery(providerUri, null, null, null, null);
        StringBuilder buffer = new StringBuilder();
        while(cursor.moveToNext()){
            buffer.append(cursor.getString(0));
            buffer.append(cursor.getString(1));
        }
        return buffer.toString().length() > 0;
    }
    
    @Override
    public String getName() {
        return "Cross Process Cursor";
    }
}
