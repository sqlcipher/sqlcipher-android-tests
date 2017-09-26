package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;

public class SoundexTest extends SQLCipherTest {

    String SQLCIPHER_SOUNDEX = "S421";

    @Override
    public boolean execute(SQLiteDatabase database) {
        String soundex = QueryHelper.singleValueFromQuery(database, "SELECT soundex('sqlcipher');");
        return SQLCIPHER_SOUNDEX.equals(soundex);
    }

    @Override
    public String getName() {
        return "Soundex Test";
    }
}
