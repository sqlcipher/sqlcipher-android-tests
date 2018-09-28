package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class ReadWriteUserVersionTest extends SQLCipherTest {
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
