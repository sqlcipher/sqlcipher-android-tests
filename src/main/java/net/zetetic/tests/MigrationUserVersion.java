package net.zetetic.tests;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteOpenHelper;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class MigrationUserVersion extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        try {
            String password = ZeteticApplication.DATABASE_PASSWORD;
            ZeteticApplication.getInstance().extractAssetToDatabaseDirectory(ZeteticApplication.ONE_X_USER_VERSION_DATABASE);
            File sourceDatabase = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.ONE_X_USER_VERSION_DATABASE);
            SQLiteDatabase originalDatabase = SQLiteDatabase.openOrCreateDatabase(sourceDatabase, password, null, new SQLiteDatabaseHook() {
                public void preKey(SQLiteDatabase sqLiteDatabase) {
                    sqLiteDatabase.rawExecSQL("PRAGMA cipher_default_use_hmac=off;");
                }
                public void postKey(SQLiteDatabase sqLiteDatabase) {}
            });
            int userVersion = originalDatabase.getVersion();
            originalDatabase.close();
            SQLiteDatabase.upgradeDatabaseFormatFromVersion1To2(sourceDatabase, password);
            SQLiteDatabase migratedDatabase = SQLiteDatabase.openOrCreateDatabase(sourceDatabase, password, null, null);
            migratedDatabase.setVersion(userVersion);
            return migratedDatabase.getVersion() == userVersion;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return "Database 1.x - 2 Migration with user_version";
    }
}
