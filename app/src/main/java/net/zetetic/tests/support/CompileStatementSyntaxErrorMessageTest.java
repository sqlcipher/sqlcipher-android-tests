package net.zetetic.tests.support;

import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteException;
import net.sqlcipher.database.SQLiteStatement;
import net.zetetic.ZeteticApplication;
import net.zetetic.tests.SQLCipherTest;

public class CompileStatementSyntaxErrorMessageTest extends SupportTest {

    @Override
    public boolean execute(SQLiteDatabase database) {
        try {
            SQLiteStatement ignored = database.compileStatement("INSERT INTO mytable (mydata) VALUES");
        } catch (SQLiteException e) {
            Log.v(ZeteticApplication.TAG, "EXPECTED RESULT: DID throw SQLiteException", e);
            String message = e.getMessage();
            setMessage(message);
            // TBD missing error code etc.
            if (!message.matches("incomplete input: , while compiling: INSERT INTO mytable \\(mydata\\) VALUES")) {
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
