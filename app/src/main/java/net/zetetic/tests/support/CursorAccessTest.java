package net.zetetic.tests.support;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import java.io.File;
import java.util.Random;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class CursorAccessTest implements ISupportTest {
    @Override
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);

        ZeteticApplication.getInstance().deleteDatabaseFileAndSiblings(ZeteticApplication.DATABASE_NAME);
        String databasesFolderPath = ZeteticApplication.getInstance()
                                                   .getDatabasePath(ZeteticApplication.DATABASE_NAME).getParent();
        File databasesFolder = new File(databasesFolderPath);
        databasesFolder.delete();

        byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
        SupportFactory factory = new SupportFactory(passphrase);
        SupportSQLiteOpenHelper.Configuration cfg =
          SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
            .name(ZeteticApplication.DATABASE_NAME)
            .callback(new SupportSQLiteOpenHelper.Callback(1) {
                @Override
                public void onCreate(SupportSQLiteDatabase db) {
                    db.execSQL("create table t1(a text, b integer, c text, d real, e blob)");
                    byte[] data = new byte[10];
                    new Random().nextBytes(data);
                    db.execSQL("insert into t1(a, b, c, d, e) values(?, ?, ?, ?, ?)", new Object[]{"test1", 100, null, 3.25, data});
                }

                @Override
                public void onUpgrade(SupportSQLiteDatabase db, int oldVersion,
                                      int newVersion) {
                    // unused
                }
            })
            .build();
        SupportSQLiteOpenHelper helper = factory.create(cfg);
        SupportSQLiteDatabase database = helper.getWritableDatabase();

        Cursor results = database.query("select * from t1", new String[]{});
        results.moveToFirst();
        int type_a = results.getType(0);
        int type_b = results.getType(1);
        int type_c = results.getType(2);
        int type_d = results.getType(3);
        int type_e = results.getType(4);

        results.close();
        helper.close();

        result.setResult(type_a == Cursor.FIELD_TYPE_STRING &&
                type_b == Cursor.FIELD_TYPE_INTEGER &&
                type_c == Cursor.FIELD_TYPE_NULL &&
                type_d == Cursor.FIELD_TYPE_FLOAT &&
                type_e == Cursor.FIELD_TYPE_BLOB);

        return result;
    }

    @Override
    public String getName() {
        return "Cursor Access Test";
    }
}
