package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class MUTF8ToUTF8WithNullMigrationTest extends SQLCipherTest {

    int keyCount = 0;
    String filename = "mutf8.db";
    char[] password = new char[]{'t', 'e', 's', 't', '\u0000', '1', '2', '3'};
    SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
        public void preKey(SQLiteDatabase database) {
            keyCount++;
        }
        public void postKey(SQLiteDatabase database) {
            database.execSQL("PRAGMA kdf_iter = 64000;");
            database.execSQL("PRAGMA cipher_page_size = 1024;");
            database.execSQL("PRAGMA cipher_hmac_algorithm = HMAC_SHA1;");
            database.execSQL("PRAGMA cipher_kdf_algorithm = PBKDF2_HMAC_SHA1;");
        }
    };

    @Override
    public boolean execute(SQLiteDatabase database) {

        boolean status = false;
        database.close();
        try {
            File databaseFile = ZeteticApplication.getInstance().getDatabasePath(filename);
            if(databaseFile.exists()){
                databaseFile.delete();
            }
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory(filename);
            database = SQLiteDatabase.openDatabase(databaseFile.getAbsolutePath(), password, null,
                    SQLiteDatabase.OPEN_READWRITE, hook);
            if (keyCount != 2) {
                return status;
            }
            database.close();
            keyCount = 0;
            database = SQLiteDatabase.openDatabase(databaseFile.getAbsolutePath(), password, null,
                    SQLiteDatabase.OPEN_READWRITE, hook);
            status = keyCount == 1;
        } catch (Exception e) {
            log(String.format("Error during modified UTF-8 to UTF-8 migration:%s", e.toString()));
        }
        return status;
    }

    @Override
    public String getName() {
        return "Migrate from modified UTF-8 to UTF-8 with null test";
    }
}
