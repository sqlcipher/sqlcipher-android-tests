package net.zetetic.tests;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

public class TimeQueryExecutionTest extends SQLCipherTest {
    @Override
    public boolean execute(SQLiteDatabase database) {

        int ROWS = 1000;
        long beforeTotal = System.nanoTime();
        database.rawExecSQL("CREATE TABLE t1(a);");
        for(int index = 0; index < ROWS; index++){
            database.execSQL("INSERT INTO t1(a) values(?);", new Object[]{String.valueOf(index)});
        }
        long before = System.nanoTime();
        Cursor cursor = database.rawQuery("SELECT * from t1;", new String[]{});
        long after = System.nanoTime();
        log(String.format("SELECT * from t1; took %d ms",
                toMilliseconds(before, after)));
        if(cursor != null){
            before = System.nanoTime();
            cursor.moveToFirst();
            after = System.nanoTime();
            log(String.format("moveToFirst() took %d ms",
                    toMilliseconds(before, after)));
            long beforeCursorMove = System.nanoTime();
            while(cursor.moveToNext()){
                before = System.nanoTime();
                String value = cursor.getString(0);
                after = System.nanoTime();
                log(String.format("getInt(0) returned:%s took %d ms\n",
                        value, toMilliseconds(before, after)));
            }
            long afterCursorMove = System.nanoTime();
            log(String.format("Complete cursor operation time:%d ms",
                    toMilliseconds(beforeCursorMove, afterCursorMove)));
            cursor.close();
        }
        database.close();
        long totalRuntime = toMilliseconds(beforeTotal, System.nanoTime());
        String message = String.format("Total runtime:%d ms\n", totalRuntime);
        log(message);
        setMessage(message);
        return true;
    }

    private long toMilliseconds(long before, long after){
        return (after - before)/1000000L;
    }

    @Override
    public String getName() {
        return "Time Query Execution Test";
    }
}
