package net.zetetic.tests;

import android.database.Cursor;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;

public class PragmaCipherVersionTest extends SQLCipherTest {

    private final String CURRENT_CIPHER_VERSION = "4.4.0";

    @Override
    public boolean execute(SQLiteDatabase database) {

        Log.i(TAG, "Before rawQuery");
        Cursor cursor = database.rawQuery("PRAGMA cipher_version", new String[]{});
        Log.i(TAG, "After rawQuery");
        if(cursor != null){
            Log.i(TAG, "Before cursor.moveToNext()");
            cursor.moveToNext();
            Log.i(TAG, "Before cursor.getString(0)");
            String cipherVersion = cursor.getString(0);
            Log.i(TAG, "Before cursor.close");
            cursor.close();
            setMessage(String.format("Reported:%s", cipherVersion));
            return cipherVersion.contains(CURRENT_CIPHER_VERSION);
        }
        return false;
    }

    @Override
    public String getName() {
        return "PRAGMA cipher_version Test";
    }
}
