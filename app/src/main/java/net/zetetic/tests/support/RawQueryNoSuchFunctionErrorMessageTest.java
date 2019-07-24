package net.zetetic.tests.support;

import android.database.Cursor;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;

public class RawQueryNoSuchFunctionErrorMessageTest extends SupportTest {

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
