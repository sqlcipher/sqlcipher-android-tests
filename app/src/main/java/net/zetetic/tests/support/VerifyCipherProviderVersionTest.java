package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;
import net.zetetic.tests.SQLCipherTest;

public class VerifyCipherProviderVersionTest extends SupportTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        String provider = QueryHelper.singleValueFromQuery(database,
                "PRAGMA cipher_provider_version;");
        setMessage(String.format("Reported:%s", provider));
        return provider.contains("OpenSSL 1.1.1") ||
            provider.contains("OpenSSL 1.0.2p-fips");
    }

    @Override
    public String getName() {
        return "Verify Cipher Provider Version";
    }
}
