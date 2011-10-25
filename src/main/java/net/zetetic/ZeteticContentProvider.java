package net.zetetic;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import info.guardianproject.database.sqlcipher.SQLiteDatabase;
import info.guardianproject.database.sqlcipher.SQLiteQueryBuilder;

public class ZeteticContentProvider extends ContentProvider {

    public static final Uri CONTENT_URI =
            Uri.parse("content://net.zetetic.sqlcipher.zeteticprovider");

    private SQLiteDatabase database;

    public ZeteticContentProvider() {
        SQLiteDatabase.loadLibs(ZeteticApplication.getInstance());
        database = ZeteticApplication.getInstance().createDatabase();
        database.execSQL("create table t1(a, b);");
        database.execSQL("insert into t1(a, b) values('one for the money', 'two for the show');");
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //return database.rawQuery("select * from t1", null);
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("t1");
        return builder.query(database, new String[]{"a", "b"}, null, null, null, null, null);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
