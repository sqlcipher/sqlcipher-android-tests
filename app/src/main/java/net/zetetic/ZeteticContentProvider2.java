package net.zetetic;

import java.io.File;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteQueryBuilder;

public class ZeteticContentProvider2 extends ContentProvider {

    public static String DATABASE_NAME = "interprocess_test.db";
    public static final Uri CONTENT_URI =
            Uri.parse("content://net.zetetic.sqlcipher.zeteticprovider2");

    private SQLiteDatabase database;

    public ZeteticContentProvider2() {}

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase.loadLibs(ZeteticApplication.getInstance());
        File databasePath = ZeteticApplication.getInstance().getDatabasePath(DATABASE_NAME);
        database = ZeteticApplication.getInstance().createDatabase(databasePath);

        createDatabaseWithData(database);
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

    private void createDatabaseWithData(SQLiteDatabase database) {
        database.execSQL("create table if not exists t1(a varchar, b blob);");
        ContentValues values = new ContentValues();
        values.put("a", "one for the money");
        values.put("b", "two for the shownwp".getBytes());
        database.insert("t1", null, values);
    }
}
