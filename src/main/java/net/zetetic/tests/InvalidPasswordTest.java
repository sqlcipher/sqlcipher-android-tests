package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.util.UUID;

public class InvalidPasswordTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {

        boolean status = false;
        database.close();
        File databaseFile = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
        String password = ZeteticApplication.DATABASE_PASSWORD;
        try{
            SQLiteDatabase.openOrCreateDatabase(databaseFile, UUID.randomUUID().toString(), null);
        } catch (Exception e){
            try {
                database = SQLiteDatabase.openOrCreateDatabase(databaseFile, password, null);
                database.rawExecSQL("create table t1(a,b);");
                database.execSQL("insert into t1(a,b) values(?, ?)", new Object[]{"testing", "123"});
                status = true;
            } catch (Exception ex){}
        }
        return status;
    }

    @Override
    public String getName() {
        return "Invalid Password Test";
    }
}
