package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class OpenReadOnlyDatabaseTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.close();
        File databasePath = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
        database = SQLiteDatabase.openDatabase(databasePath.getAbsolutePath(), ZeteticApplication.DATABASE_PASSWORD,
                                                null, SQLiteDatabase.OPEN_READONLY);
        boolean opened = database.isOpen();
        database.close();
        return opened;
    }

    @Override
    public String getName() {
        return "Open Read Only Database Test";
    }
}
