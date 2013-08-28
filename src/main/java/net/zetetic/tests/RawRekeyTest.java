package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class RawRekeyTest extends SQLCipherTest {

    String password = "x\'2DD29CA851E7B56E4697B0E1F08507293D761A05CE4D1B628663F411A8086D99\'";
    String rekeyCommand = String.format("PRAGMA rekey  = \"%s\";", password);
    File databaseFile = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.rawExecSQL(rekeyCommand);
        database.close();
        database = SQLiteDatabase.openOrCreateDatabase(databaseFile, password, null);
        return database != null;
    }

    @Override
    public String getName() {
        return "Raw Rekey Test";
    }
}
