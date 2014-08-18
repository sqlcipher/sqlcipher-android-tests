package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.QueryHelper;

public class ComputeKDFTest extends SQLCipherTest {

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
