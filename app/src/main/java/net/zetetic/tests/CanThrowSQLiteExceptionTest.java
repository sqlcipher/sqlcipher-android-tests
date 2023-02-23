package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class CanThrowSQLiteExceptionTest extends SQLCipherTest {

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
