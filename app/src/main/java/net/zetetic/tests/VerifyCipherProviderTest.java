package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;

public class VerifyCipherProviderTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        String provider = QueryHelper.singleValueFromQuery(database,
                "PRAGMA cipher_provider;");
        return provider.contains("openssl");
    }

    @Override
    public String getName() {
        return "Verify Cipher Provider Test";
    }
}
