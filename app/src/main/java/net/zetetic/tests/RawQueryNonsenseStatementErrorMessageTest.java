package net.zetetic.tests;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;

import net.zetetic.ZeteticApplication;

import android.util.Log;

import java.util.regex.Pattern;

public class RawQueryNonsenseStatementErrorMessageTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        try {
            Cursor ignored = database.rawQuery("101", null);
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: DID throw SQLiteException", e);
            String message = e.getMessage();
            setMessage(message);
            // TBD missing error code etc.
            if (!message.matches("near \"101\": syntax error: .*\\, while compiling: 101")) {
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
        return "rawQuery nonsense statement error message Test";
    }
}
