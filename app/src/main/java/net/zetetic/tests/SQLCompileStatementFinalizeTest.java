package net.zetetic.tests;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SQLCompileStatementFinalizeTest extends SQLCipherTest {

    private final int count = 2;

    @Override
    public boolean execute(final SQLiteDatabase database) {

        final CountDownLatch latchMain = new CountDownLatch(1);
        final CountDownLatch latchTransaction = new CountDownLatch(1);
        final CountDownLatch latchSQLRelease = new CountDownLatch(1);

        database.execSQL("CREATE TABLE TestTable(text_value TEXT);");

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < count; i++) {
                    SQLiteStatement statement = database.compileStatement("DELETE FROM TestTable");
                    statement.executeUpdateDelete();
                }

                latchSQLRelease.countDown();

                try {
                    latchTransaction.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latchSQLRelease.await();

                    database.beginTransaction();

                    latchTransaction.countDown();

                    long start = System.currentTimeMillis();
                    while (System.currentTimeMillis() < start + TimeUnit.SECONDS.toMillis(40)) {
                        new Object();
                        System.gc();
                    }
                    database.setTransactionSuccessful();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    database.endTransaction();
                }

                latchMain.countDown();
            }
        }).start();


        try {
            latchMain.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "Finalize SQLComileStatement causes crash on API 24-25";
    }


}
