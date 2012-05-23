package net.zetetic.tests;

import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.io.IOException;

public class ImportUnencryptedDatabaseTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        File unencryptedDatabase = ZeteticApplication.getInstance().getDatabasePath("unencrypted.db");
        File encryptedDatabase = ZeteticApplication.getInstance().getDatabasePath("encrypted.db");

        try {
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory("unencrypted.db");

            database.close();
            database = SQLiteDatabase.openOrCreateDatabase(unencryptedDatabase, "", null);
            database.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s'",
                                encryptedDatabase.getAbsolutePath(), ZeteticApplication.DATABASE_PASSWORD));
            database.rawExecSQL("select sqlcipher_export('encrypted')");
            database.rawExecSQL("DETACH DATABASE encrypted");
            database.close();

            database = SQLiteDatabase.openOrCreateDatabase(encryptedDatabase, ZeteticApplication.DATABASE_PASSWORD,
                                                           null);
            Cursor cursor = database.rawQuery("select * from t1", new String[]{});
            cursor.moveToFirst();
            String a = cursor.getString(0);
            String b = cursor.getString(1);
            cursor.close();
            database.close();

            return a.equals("one for the money") &&
                   b.equals("two for the show");

        } catch (IOException e) {
            return false;
        }
        finally {
            unencryptedDatabase.delete();
            encryptedDatabase.delete();
        }
    }

    @Override
    public String getName() {
        return "Import Unencrypted Database Test";
    }
}
