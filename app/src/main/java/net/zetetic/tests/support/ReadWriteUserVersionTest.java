package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.tests.SQLCipherTest;

public class ReadWriteUserVersionTest extends SupportTest {
    @Override
    public boolean execute(SQLiteDatabase database) {
        int version = 4;
        database.setVersion(version);
        int readVersion = database.getVersion();
        return version == readVersion;
    }

    @Override
    public String getName() {
        return "Read/write user_version";
    }
}
