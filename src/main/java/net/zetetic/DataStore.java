package net.zetetic;

import android.content.Context;
import info.guardianproject.database.sqlcipher.SQLiteDatabase;
import info.guardianproject.database.sqlcipher.SQLiteOpenHelper;


public class DataStore {

    private Context context;
    private static DataStore instance;
    private SchemaMigration schemaMigration;
    private SQLiteDatabase database;

    private DataStore(Context context) {
        this.context = context;
    }

    public static synchronized DataStore getInstance(Context context) {
        if (instance == null) {
            instance = new DataStore(context);
        }
        return instance;
    }

    public DataStore open(String password) {
        schemaMigration = new SchemaMigration(context);
        database = schemaMigration.getWritableDatabase(password);
        return this;
    }

    public void createEntry(String first, String second){
        database.execSQL("insert into t1(a,b) values (?, ?)", new Object[]{first, second});
    }

    private static class SchemaMigration extends SQLiteOpenHelper {

        private static final String DB_NAME = "test.db";
        private static final int DB_VERSION = 1;
        private static final String SCHEMA = "create table t1(a,b);";

        SchemaMigration(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(SCHEMA);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int previousVersion, int newVersion) {

        }
    }

}



