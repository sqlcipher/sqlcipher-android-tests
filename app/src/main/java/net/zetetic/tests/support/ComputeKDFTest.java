package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;
import net.zetetic.tests.SQLCipherTest;

public class ComputeKDFTest extends SupportTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.rawExecSQL("PRAGMA cipher_kdf_compute;");
        String kdf = QueryHelper.singleValueFromQuery(database, "PRAGMA kdf_iter;");
        setMessage(String.format("Computed KDF:%s", kdf));
        return true;
    }

    @Override
    public String getName() {
        return "Compute KDF Test";
    }
}
