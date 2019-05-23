package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;
import net.zetetic.QueryHelper;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.TestResult;
import java.io.File;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

public class RawRekeyTest implements ISupportTest {

    String password = "x\'2DD29CA851E7B56E4697B0E1F08507293D761A05CE4D1B628663F411A8086D99\'";
    String rekeyCommand = String.format("PRAGMA rekey  = \"%s\";", password);
    File databaseFile = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);

    @Override
    public TestResult run() {
        TestResult result = new TestResult(getName(), false);
        byte[] passphrase = SQLiteDatabase.getBytes(ZeteticApplication.DATABASE_PASSWORD.toCharArray());
        SupportFactory factory = new SupportFactory(passphrase);
        SupportSQLiteOpenHelper.Configuration cfg =
          SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
            .name(ZeteticApplication.DATABASE_NAME)
            .callback(new SupportSQLiteOpenHelper.Callback(1) {
                @Override
                public void onCreate(SupportSQLiteDatabase db) {
                    // unused
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

        database.execSQL("create table t1(a,b);");
        database.execSQL("insert into t1(a,b) values(?,?)", new Object[]{"one for the money", "two for the show"});
        database.execSQL(rekeyCommand);
        helper.close();

        passphrase = SQLiteDatabase.getBytes(password.toCharArray());
        factory = new SupportFactory(passphrase);
        cfg =
          SupportSQLiteOpenHelper.Configuration.builder(ZeteticApplication.getInstance())
            .name(ZeteticApplication.DATABASE_NAME)
            .callback(new SupportSQLiteOpenHelper.Callback(1) {
                @Override
                public void onCreate(SupportSQLiteDatabase db) {
                    // unused
                }

                @Override
                public void onUpgrade(SupportSQLiteDatabase db, int oldVersion,
                                      int newVersion) {
                    // unused
                }
            })
            .build();
        helper = factory.create(cfg);
        database = helper.getWritableDatabase();

        int count = QueryHelper.singleIntegerValueFromQuery(database, "select count(*) from t1;");
        result.setResult(count == 1);
        helper.close();
        return result;
    }

    @Override
    public String getName() {
        return "Raw Rekey Test";
    }
}
