package net.zetetic.tests;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;
import net.sqlcipher.database.SQLiteConstraintException;

import net.zetetic.ZeteticApplication;

import android.util.Log;

public class ExecuteInsertConstraintErrorMessageTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.execSQL("CREATE TABLE tt(a UNIQUE, b)");
        database.execSQL("INSERT INTO tt VALUES (101, 'Alice')");
        try {
            SQLiteStatement insertStatement = database.compileStatement("INSERT INTO tt VALUES (101, 'Betty')");
            long ignored = insertStatement.executeInsert();
        } catch (SQLiteConstraintException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: DID throw SQLiteConstraintException", e);
            String message = e.getMessage();
            setMessage(message);
            if (!message.matches("error code 19: UNIQUE constraint failed: tt\\.a")) {
                Log.e(ZeteticApplication.TAG, "NOT EXPECTED: INCORRECT exception message: " + message);
                return false;
            }
            return true;
        } catch (Exception e) {
            Log.e(ZeteticApplication.TAG, "NOT EXPECTED: DID throw other exception", e);
            return false;
        }

        return false;
    }

    @Override
    public String getName() {
        return "Execute insert constraint error message Test";
    }
}
