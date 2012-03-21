package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;

public class RawExecSQLExceptionTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        try{
            database.rawExecSQL("select foo from bar");
        }catch (Exception e){
            return true;
        }
        return false;
    }

    @Override
    public String getName() {
        return "rawExecSQL Exception Test";
    }
}
