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
            try{
                Log.i(ZeteticApplication.TAG, "Running test:" + test.getName());
                publishProgress(test.run());
            }catch (Throwable e){
                publishProgress(new TestResult(test.getName(), false));
            }
        }
    }

    private List<SQLCipherTest> getTestsToRun(){
        List<SQLCipherTest> tests = new ArrayList<SQLCipherTest>();
        tests.add(new AttachDatabaseTest());
        tests.add(new CipherMigrateTest());
        tests.add(new GetTypeFromCrossProcessCursorWrapperTest());
        tests.add(new InvalidPasswordTest());
        tests.add(new NullQueryResultTest());
        tests.add(new CrossProcessCursorQueryTest());
        tests.add(new InterprocessBlobQueryTest());
        tests.add(new LoopingQueryTest());
        tests.add(new LoopingCountQueryTest());
        tests.add(new AttachNewDatabaseTest());
        tests.add(new AttachExistingDatabaseTest());
        tests.add(new CanThrowSQLiteExceptionTest());
        tests.add(new RawExecSQLTest());
        tests.add(new RawExecSQLExceptionTest());
        tests.add(new CompiledSQLUpdateTest());
        tests.add(new AES128CipherTest());
        tests.add(new MigrateDatabaseFrom1xFormatToCurrentFormat());
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
        tests.add(new EnableForeignKeySupportTest());
        tests.add(new AverageOpenTimeTest());
        tests.add(new NestedTransactionsTest());
        tests.add(new UnicodeTest());
        tests.add(new MultiThreadReadWriteTest());
        tests.add(new ComputeKDFTest());
        tests.add(new SoundexTest());
        tests.add(new RawQueryTest());
        tests.add(new OpenReadOnlyDatabaseTest());
        tests.add(new RawRekeyTest());
        return tests;
    }
}
