package net.zetetic.tests.support;

import android.database.Cursor;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;

public class RawQuerySyntaxErrorMessageTest extends SupportTest {

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
