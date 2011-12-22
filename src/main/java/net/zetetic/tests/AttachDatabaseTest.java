package net.zetetic.tests;

import info.guardianproject.database.sqlcipher.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class AttachDatabaseTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase encryptedDatabase) {

        encryptedDatabase.execSQL("create table t1(a,b)");
        encryptedDatabase.execSQL("insert into t1(a,b) values(?, ?)", new Object[]{"one", "two"});

        String newKey = "foo";
        File newDatabasePath = ZeteticApplication.getInstance().getDatabasePath("normal.db");
        String attachCommand = "ATTACH DATABASE ? as encrypted KEY ?";
        String createCommand = "create table encrypted.t1(a,b)";
        String insertCommand = "insert into encrypted.t1 SELECT * from t1";
        String detachCommand = "DETACH DATABASE encrypted";
        encryptedDatabase.execSQL(attachCommand, new Object[]{newDatabasePath.getAbsolutePath(), newKey});
        encryptedDatabase.execSQL(createCommand);
        encryptedDatabase.execSQL(insertCommand);
        encryptedDatabase.execSQL(detachCommand);
        
        return true;
    }

    @Override
    protected void tearDown() {
        File newDatabasePath = ZeteticApplication.getInstance().getDatabasePath("normal.db");
        newDatabasePath.delete();
    }

    @Override
    public String getName() {
        return "Attach Database Test";
    }
}
