package net.zetetic.tests;

import android.util.Log;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultiThreadReadWriteTest extends SQLCipherTest {

    final File databaseFile = ZeteticApplication.getInstance().getDatabasePath(ZeteticApplication.DATABASE_NAME);
    final String password = ZeteticApplication.DATABASE_PASSWORD;
    private static SQLiteDatabase instance;
    ThreadExecutor executor;
    int recordsPerThread = 20;
    int threadCount = 40;

    @Override
    public boolean execute(SQLiteDatabase database) {
        database.close();
        executor = new ThreadExecutor(threadCount, recordsPerThread, DatabaseAccessType.Singleton);
        return executor.run();
    }

    synchronized SQLiteDatabase getDatabase(DatabaseAccessType accessType) {
        if (accessType == DatabaseAccessType.InstancePerRequest) {
            return SQLiteDatabase.openOrCreateDatabase(databaseFile, password, null);
        } else if (accessType == DatabaseAccessType.Singleton) {
            if (instance == null) {
                instance = SQLiteDatabase.openOrCreateDatabase(databaseFile, password, null);
            }
            return instance;
        }
        return null;
    }

    synchronized void closeDatabase(SQLiteDatabase database, DatabaseAccessType accessType){
        if(accessType == DatabaseAccessType.InstancePerRequest){
            database.close();
        }
    }

    private class Writer implements Runnable {

        int id;
        int size;
        private DatabaseAccessType accessType;

        public Writer(int id, int size, DatabaseAccessType accessType) {
            this.id = id;
            this.size = size;
            this.accessType = accessType;
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            Log.i(TAG, String.format("writer thread %d beginning", id));
            SQLiteDatabase writer = getDatabase(accessType);
            writer.execSQL("create table if not exists t1(a,b)");
            for (int index = 0; index < size; index++) {
                Log.i(TAG, String.format("writer thread %d - insert data for row:%d", id, index));
                writer.execSQL("insert into t1(a,b) values(?, ?)",
                        new Object[]{"one for the money", "two for the show"});
            }
            closeDatabase(writer, accessType);
            Log.i(TAG, String.format("writer thread %d terminating", id));
        }
    }

    private class Reader implements Runnable {

        int id;
        private DatabaseAccessType accessType;

        public Reader(int id, DatabaseAccessType accessType) {
            this.id = id;
            this.accessType = accessType;
        }

        @Override
        public void run() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            Log.i(TAG, String.format("reader thread %d beginning", id));
            SQLiteDatabase reader = getDatabase(accessType);
            int currentCount = 0;
            int updatedCount = getCurrentTableCount(reader);
            while (currentCount == 0 || currentCount != updatedCount) {
                logRecordsBetween(reader, currentCount, updatedCount);
                currentCount = updatedCount;
                updatedCount = getCurrentTableCount(reader);
            }
            closeDatabase(reader, accessType);
            Log.i(TAG, String.format("reader thread %d terminating", id));
        }

        void logRecordsBetween(SQLiteDatabase reader, int start, int end) {
            Cursor results = reader.rawQuery("select rowid, * from t1 where rowid between ? and ?",
                    new String[]{String.valueOf(start), String.valueOf(end)});
            if (results != null) {
                Log.i(TAG, String.format("reader thread %d - writing results %d to %d", id, start, end));
                while (results.moveToNext()) {
                    Log.i(TAG, String.format("reader thread %d - record:%d, a:%s b:%s",
                            id, results.getInt(0), results.getString(1), results.getString(2)));
                }
                results.close();
            }
        }

        int getCurrentTableCount(SQLiteDatabase database) {
            int count = 0;
            Cursor cursor = database.rawQuery("select count(*) from t1;", new String[]{});
            if (cursor != null) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
                cursor.close();
            }
            return count;
        }
    }

    private class ThreadExecutor {

        private int threadCount;
        private int recordsPerThread;
        private DatabaseAccessType accessType;
        List<Thread> readerThreads;
        List<Thread> writerThreads;
        boolean status = false;

        public ThreadExecutor(int threads, int recordsPerThread, DatabaseAccessType accessType) {

            if (threads % 2 != 0) {
                throw new IllegalArgumentException("Threads must be split evenly between readers and writers");
            }
            this.threadCount = threads / 2;
            this.recordsPerThread = recordsPerThread;
            this.accessType = accessType;
            readerThreads = new ArrayList<Thread>();
            writerThreads = new ArrayList<Thread>();
        }

        public boolean run() {

            for (int index = 0; index < threadCount; index++) {
                readerThreads.add(new Thread(new Reader(index, accessType)));
                writerThreads.add(new Thread(new Writer(index, recordsPerThread, accessType)));
            }
            for (int index = 0; index < threadCount; index++) {
                Log.i(TAG, String.format("Starting writer thread %d", index));
                writerThreads.get(index).start();
            }
            try {
                for (int index = 0; index < threadCount; index++) {
                    Log.i(TAG, String.format("Starting reader thread %d", index));
                    readerThreads.get(index).start();
                }
                Log.i(TAG, String.format("Request reader thread %d to join", 0));
                readerThreads.get(threadCount - 1).join();
                status = true;
            } catch (InterruptedException e) {
                Log.i(TAG, "Thread join failure", e);
            }
            if(accessType == DatabaseAccessType.Singleton){
                getDatabase(accessType).close();
            }
            return status;
        }
    }


    private enum DatabaseAccessType {
        Singleton,
        InstancePerRequest
    }

    @Override
    public String getName() {
        return "Multi-threaded read/write test";
    }
}