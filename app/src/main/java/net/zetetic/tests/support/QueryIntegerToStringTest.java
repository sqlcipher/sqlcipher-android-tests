package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;
import net.zetetic.tests.SQLCipherTest;

public class QueryIntegerToStringTest extends SupportTest {
    @Override
    public boolean execute(SQLiteDatabase database) {
        String value = QueryHelper.singleValueFromQuery(database, "SELECT 123;");
        return value.equals("123");
    }

    @Override
    public String getName() {
        return "Query Integer to String";
    }
}
