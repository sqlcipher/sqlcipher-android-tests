package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;

public class VerifyCipherProviderVersionTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        String provider = QueryHelper.singleValueFromQuery(database,
                "PRAGMA cipher_provider_version;");
        setMessage(String.format("Reported:%s", provider));
        return provider.contains("OpenSSL 1.1.0g") ||
            provider.contains("OpenSSL 1.0.2m-fips");
    }

    @Override
    public String getName() {
        return "Verify Cipher Provider Version";
    }
}
