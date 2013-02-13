package net.zetetic.tests;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.io.IOException;

public class QueryNonEncryptedDatabaseTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        boolean success = false;
        try {
            File unencryptedDatabase = ZeteticApplication.getInstance().getDatabasePath("unencrypted.db");
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory("unencrypted.db");
            database.close();
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase, "", null);
            Cursor cursor = database.rawQuery("select * from t1", new String[]{});
            cursor.moveToFirst();
            String a = cursor.getString(0);
            String b = cursor.getString(1);
            cursor.close();
            database.close();
            success = a.equals("one for the money") &&
                        b.equals("two for the show");
        } catch (IOException e) {}
        return success;
    }

    @Override
    public String getName() {
        return "Query Non-Encrypted Database Test";
    }
}
