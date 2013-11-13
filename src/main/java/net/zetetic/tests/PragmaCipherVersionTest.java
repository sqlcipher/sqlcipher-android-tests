package net.zetetic.tests;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class PragmaCipherVersionTest extends SQLCipherTest {

    private final String CURRENT_CIPHER_VERSION = "3.0.0";

    @Override
    public boolean execute(SQLiteDatabase database) {

        Cursor cursor = database.rawQuery("PRAGMA cipher_version", new String[]{});
        if(cursor != null){
            cursor.moveToNext();
            String cipherVersion = cursor.getString(0);
            cursor.close();
            return cipherVersion.equals(CURRENT_CIPHER_VERSION);
        }
        return false;
    }

    @Override
    public String getName() {
        return "PRAGMA cipher_version Test";
    }
}