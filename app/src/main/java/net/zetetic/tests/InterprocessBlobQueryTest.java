package net.zetetic.tests;

import android.app.Activity;
import android.net.Uri;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;
import net.zetetic.ZeteticContentProvider2;

public class InterprocessBlobQueryTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        Activity activity = ZeteticApplication.getInstance().getCurrentActivity();
        Uri providerUri = ZeteticContentProvider2.CONTENT_URI;
        android.database.Cursor cursor = activity.getContentResolver().query(providerUri, null, null, null, null);
        StringBuilder buffer = new StringBuilder();
        while (cursor.moveToNext()) {
            buffer.append(cursor.getString(0));
            buffer.append(new String(cursor.getBlob(1)));
        }
        cursor.close();
        return buffer.toString().length() > 0;
    }



    @Override
    public String getName() {
        return "Custom Inter-Process Blob Test";
    }
}
