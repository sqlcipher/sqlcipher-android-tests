package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;

public class QueryIntegerToStringTest extends SQLCipherTest {
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
