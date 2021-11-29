package net.zetetic.tests.support;

import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteTransactionListener;
import net.zetetic.tests.SQLCipherTest;

import java.util.concurrent.atomic.AtomicInteger;

public class TransactionNestedWithListenerTest2 extends SQLCipherTest {

    private static final String TAG = TransactionNestedWithListenerTest2.class.getSimpleName();

    @Override
    public boolean execute(SQLiteDatabase database) {
        CustomTransactionListener listener = new CustomTransactionListener();

        database.beginTransactionWithListener(listener);
        database.execSQL("create table t11(a,b);");

        //Nested start
        database.beginTransactionWithListener(listener);
        database.execSQL("create table t22(a,b);");
        database.setTransactionSuccessful();
        database.endTransaction();
        //Nested end

        database.setTransactionSuccessful();
        database.endTransaction();

        if (!listener.isComplete()) {
            Log.e(TAG, "Listener didn't receive end, started times - "
                    + listener.startCalled.get() + ", ended times - " + listener.endCalled.get()
            );
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return "Nested transactions with listener 2";
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
            return (startCalled.get() == endCalled.get());
        }

    }

}
