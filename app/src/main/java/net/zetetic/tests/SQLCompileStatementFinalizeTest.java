package net.zetetic.tests;

import android.util.Log;

import androidx.annotation.NonNull;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SQLCompileStatementFinalizeTest extends SQLCipherTest {

    private final int count = 2;
    private final boolean isLocking;

    public SQLCompileStatementFinalizeTest(Boolean useLocking) {
        isLocking = useLocking;
    }

    @Override
    public boolean execute(final SQLiteDatabase database) {
        database.setLockingEnabled(isLocking);
        final CountDownLatch latchMain = new CountDownLatch(1);
        final CountDownLatch latchTransaction = new CountDownLatch(1);
        final CountDownLatch latchSQLRelease = new CountDownLatch(1);

        database.execSQL("CREATE TABLE TestTable(text_value TEXT);");

        Thread thread1 = new Thread(new Runnable() {
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
        });

        Thread thread2 = new Thread(new Runnable() {
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
        });

        thread1.start();
        thread2.start();

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
        return "SQLCompileStatement crash, locking=" + isLocking;
    }

}
