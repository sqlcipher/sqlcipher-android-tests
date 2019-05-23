package net.zetetic.tests.support;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import java.io.File;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class ReadableWritableAccessTest implements ISupportTest {
    @Override
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);

        File databaseFile = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
        File databasesDirectory = new File(databaseFile.getParent());
        for(File file : databasesDirectory.listFiles()){
            file.delete();
        }

        byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
        SupportFactory factory = new SupportFactory(passphrase);
        SupportSQLiteOpenHelper.Configuration cfg =
          SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
            .name(ZeteticApplication.DATABASE_NAME)
            .callback(new SupportSQLiteOpenHelper.Callback(1) {
                @Override
                public void onCreate(SupportSQLiteDatabase db) {
                    db.execSQL("create table t1(a,b)");
                    db.execSQL("insert into t1(a,b) values(?, ?)", new Object[]{"one for the money",
                      "two for the show"});
                }

                @Override
                public void onUpgrade(SupportSQLiteDatabase db, int oldVersion,
                                      int newVersion) {
                    // unused
                }
            })
            .build();
        SupportSQLiteOpenHelper helper = factory.create(cfg);
        SupportSQLiteDatabase writableDatabase = helper.getWritableDatabase();

        writableDatabase.beginTransaction();

        SupportSQLiteDatabase readableDatabase = helper.getReadableDatabase();
        Cursor results = readableDatabase.query("select count(*) from t1", new String[]{});
        results.moveToFirst();
        int rowCount = results.getInt(0);

        results.close();
        writableDatabase.endTransaction();
        helper.close();
        helper.close();
        result.setResult(rowCount == 1);

        return result;
    }

    @Override
    public String getName() {
        return "Readable/Writable Access Test";
    }
}
