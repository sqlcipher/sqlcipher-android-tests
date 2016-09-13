package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class DeleteFunctionTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {

        boolean status = false;
        database.rawExecSQL("create table t1(itemId TEXT UNIQUE, userId INTEGER);");
        database.execSQL("insert into t1(itemId, userId) values(?, ?);", new Object[]{"123", 456});
        int currentCount = getCurrentCount(database, "123", 456);
        if(currentCount != 1) {
            setMessage("Initial insert failed");
            return status;
        }
        database.delete("t1", "itemId = ? and userId = ?", new String[]{"123", String.valueOf(456)});
        currentCount = getCurrentCount(database, "123", 456);
        status = currentCount == 0;
        return status;
    }

    private int getCurrentCount(SQLiteDatabase database, String itemId, int userId) {
        int count = 0;
        try {
            Cursor cursor = database.rawQuery("select count(*) from t1 where itemId = ? and userId = ?;",
                    new String[]{itemId, String.valueOf(userId)});
            if(cursor != null){
                cursor.moveToFirst();
                count = cursor.getInt(0);
                cursor.close();
            }
        }
        catch (Exception ex){}
        finally {
            return count;
        }
    }

    @Override
    public String getName() {
        return "Delete Function Test";
    }
}
