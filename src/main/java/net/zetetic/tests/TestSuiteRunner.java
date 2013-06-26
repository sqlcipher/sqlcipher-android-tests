package net.zetetic.tests;

import android.os.AsyncTask;
import android.util.Log;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.util.ArrayList;
import java.util.List;

public class TestSuiteRunner extends AsyncTask<ResultNotifier, TestResult, Void> {

    private ResultNotifier notifier;

    @Override
    protected Void doInBackground(ResultNotifier... resultNotifiers) {
        this.notifier = resultNotifiers[0];
        runSuite();
        return null;
    }

    @Override
    protected void onProgressUpdate(TestResult... values) {
        notifier.send(values[0]);
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        notifier.complete();
    }

    private void runSuite(){

        SQLiteDatabase.loadLibs(ZeteticApplication.getInstance());
        for(SQLCipherTest test : getTestsToRun()){
            Log.i(ZeteticApplication.TAG, "Running test:" + test.getName());
            publishProgress(test.run());
        }
    }

    private List<SQLCipherTest> getTestsToRun(){
        List<SQLCipherTest> tests = new ArrayList<SQLCipherTest>();
        tests.add(new InvalidPasswordTest());
        tests.add(new NullQueryResultTest());
        tests.add(new CrossProcessCursorQueryTest());
        tests.add(new LoopingQueryTest());
        tests.add(new LoopingCountQueryTest());
        tests.add(new AttachNewDatabaseTest());
        tests.add(new AttachExistingDatabaseTest());
        tests.add(new CanThrowSQLiteExceptionTest());
        tests.add(new RawExecSQLTest());
        tests.add(new RawExecSQLExceptionTest());
        tests.add(new CompiledSQLUpdateTest());
        tests.add(new AES128CipherTest());
        tests.add(new MigrationFromDatabaseFormat1To2());
        tests.add(new StatusMemoryUsedTest());
        tests.add(new PragmaCipherVersionTest());
        tests.add(new ImportUnencryptedDatabaseTest());
        tests.add(new FullTextSearchTest());
        tests.add(new ReadableDatabaseTest());
        tests.add(new AutoVacuumOverReadTest());
        tests.add(new ReadableWritableAccessTest());
        tests.add(new CursorAccessTest());
        tests.add(new VerifyOnUpgradeIsCalledTest());
        tests.add(new MigrationUserVersion());
        tests.add(new ExportToUnencryptedDatabase());
        tests.add(new QueryNonEncryptedDatabaseTest());
        return tests;
    }
}
