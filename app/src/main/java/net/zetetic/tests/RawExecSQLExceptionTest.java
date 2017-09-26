package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import net.zetetic.ZeteticApplication;

import android.util.Log;

public class RawExecSQLExceptionTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {

        try {
            database.rawExecSQL("select foo from bar");
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: DID throw SQLiteException", e);
            String message = e.getMessage();
            setMessage(message);
            if (!message.matches("no such table: bar")) {
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
        return "rawExecSQL Exception Test";
    }
}
