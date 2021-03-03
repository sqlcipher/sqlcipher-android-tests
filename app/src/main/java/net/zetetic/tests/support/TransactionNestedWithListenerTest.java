package net.zetetic.tests.support;

import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteTransactionListener;
import net.zetetic.tests.SQLCipherTest;

import java.util.concurrent.atomic.AtomicInteger;

public class TransactionNestedWithListenerTest extends SQLCipherTest {

    private static final String TAG = TransactionNestedWithListenerTest.class.getSimpleName();

    @Override
    public boolean execute(SQLiteDatabase database) {
        CustomTransactionListener outer = new CustomTransactionListener();
        CustomTransactionListener inner = new CustomTransactionListener();

        database.beginTransactionWithListener(outer);
        database.execSQL("create table t1(a,b);");

        //Nested start
        database.beginTransactionWithListener(inner);
        database.execSQL("create table t2(a,b);");
        database.setTransactionSuccessful();
        database.endTransaction();
        //Nested end

        database.setTransactionSuccessful();
        database.endTransaction();

        if (!outer.isComplete() || !inner.isComplete()) {
            Log.e(TAG, "Outer listener started times - " + outer.startCalled.get()
                    + ", ended times - " + outer.endCalled.get()
            );
            Log.e(TAG, "Inner listener  started times - " + inner.startCalled.get()
                    + ", ended times - " + inner.endCalled.get()
            );
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "Nested transactions with listener";
    }

    private static class CustomTransactionListener implements SQLiteTransactionListener {

        private final AtomicInteger startCalled = new AtomicInteger();
        private final AtomicInteger endCalled = new AtomicInteger();

        @Override
        public void onBegin() {
            startCalled.incrementAndGet();
        }

        @Override
        public void onCommit() {
            endCalled.incrementAndGet();
        }

        @Override
        public void onRollback() {
            endCalled.incrementAndGet();
        }

        boolean isComplete() {
            return (startCalled.get() == endCalled.get()) && startCalled.get() == 1;
        }

    }

}
