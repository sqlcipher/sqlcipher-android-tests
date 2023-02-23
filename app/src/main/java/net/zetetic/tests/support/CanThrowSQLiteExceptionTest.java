package net.zetetic.tests.support;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import net.zetetic.tests.SQLCipherTest;

public class CanThrowSQLiteExceptionTest extends SupportTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        try{
            throw new SQLiteException();
        }catch (SQLiteException ex){
            return true;
        }
    }

    @Override
    public String getName() {
        return "SQLiteException Test";
    }
}
