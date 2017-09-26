package net.zetetic.tests;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import net.zetetic.ZeteticApplication;

import android.util.Log;

public class RawQuerySyntaxErrorMessageTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        try {
            Cursor ignored = database.rawQuery("SLCT 1", null);
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: DID throw SQLiteException", e);
            String message = e.getMessage();
            setMessage(message);
            // TBD missing error code etc.
            if (!message.matches("near \"SLCT\": syntax error: .*\\, while compiling: SLCT 1")) {
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
        return "rawQuery syntax error message Test";
    }
}
