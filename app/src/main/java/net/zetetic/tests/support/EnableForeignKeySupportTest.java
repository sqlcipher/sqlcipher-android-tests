package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;
import net.zetetic.tests.SQLCipherTest;

public class EnableForeignKeySupportTest extends SupportTest {
    @Override
    public boolean execute(SQLiteDatabase database) {
        String defaultValue = QueryHelper.singleValueFromQuery(database, "PRAGMA foreign_keys");
        database.rawExecSQL("PRAGMA foreign_keys = ON;");
        String updatedValue = QueryHelper.singleValueFromQuery(database, "PRAGMA foreign_keys");
        return defaultValue.equals("0") && updatedValue.equals("1");
    }

    @Override
    public String getName() {
        return "Enable Foreign Key Support Test";
    }
}
