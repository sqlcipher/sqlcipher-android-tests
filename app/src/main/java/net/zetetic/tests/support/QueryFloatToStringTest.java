package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;
import net.zetetic.tests.SQLCipherTest;

public class QueryFloatToStringTest extends SupportTest {
    @Override
    public boolean execute(SQLiteDatabase database) {
        String value = QueryHelper.singleValueFromQuery(database, "SELECT 42.09;");
        return value.equals("42.09");
    }

    @Override
    public String getName() {
        return "Query Float to String";
    }
}
