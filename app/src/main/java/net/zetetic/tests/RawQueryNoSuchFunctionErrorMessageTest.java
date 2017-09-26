package net.zetetic.tests;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import net.zetetic.ZeteticApplication;

import android.util.Log;

public class RawQueryNoSuchFunctionErrorMessageTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        try {
            Cursor ignored = database.rawQuery("SELECT UPER('Test')", null);
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: DID throw SQLiteException", e);
            String message = e.getMessage();
            setMessage(message);
            // TBD missing error code etc.
            if (!message.matches("no such function: UPER: .*\\, while compiling: SELECT UPER\\('Test'\\)")) {
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
        return "rawQuery no such function error message Test";
    }
}
