package net.zetetic.tests;

import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;
import net.sqlcipher.database.SQLiteException;

import net.zetetic.ZeteticApplication;

import android.util.Log;

public class CompileStatementSyntaxErrorMessageTest extends SQLCipherTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        try {
            SQLiteStatement ignored = database.compileStatement("INSERT INTO mytable (mydata) VALUES");
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: DID throw SQLiteException", e);
            String message = e.getMessage();
            setMessage(message);
            // TBD missing error code etc.
            if (!message.matches("near \"VALUES\": syntax error: .*\\, while compiling: INSERT INTO mytable \\(mydata\\) VALUES")) {
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
        return "Compile statement syntax error message Test";
    }
}
