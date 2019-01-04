package net.zetetic.tests;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.WindowManager;

import net.sqlcipher.CursorWindow;
import net.sqlcipher.CursorWindowAllocation;
import net.sqlcipher.database.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.util.ArrayList;
import java.util.List;

public class TestSuiteRunner extends AsyncTask<ResultNotifier, TestResult, Void> {

  String TAG = getClass().getSimpleName();
  private ResultNotifier notifier;
  private Activity activity;

  public TestSuiteRunner(Activity activity) {
    this.activity = activity;
    if (this.activity != null) {
      this.activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
  }

  @Override
  protected Void doInBackground(ResultNotifier... resultNotifiers) {
    this.notifier = resultNotifiers[0];
    Log.i(ZeteticApplication.TAG, String.format("Running test suite on %s platform", Build.CPU_ABI));
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
    if (this.activity != null) {
      this.activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
  }

  private void runSuite() {

    SQLiteDatabase.loadLibs(ZeteticApplication.getInstance());
    CursorWindowAllocation defaultAllocation = CursorWindow.getCursorWindowAllocation();
    for (SQLCipherTest test : getTestsToRun()) {
      try {
        CursorWindow.setCursorWindowAllocation(defaultAllocation);
        Log.i(ZeteticApplication.TAG, "Running test:" + test.getName());
        TestResult result = test.run();
        publishProgress(result);

      } catch (Throwable e) {
        Log.i(ZeteticApplication.TAG, e.toString());
        publishProgress(new TestResult(test.getName(), false, e.toString()));
      }
      finally {
        CursorWindow.setCursorWindowAllocation(defaultAllocation);
      }
    }
  }

  private List<SQLCipherTest> getTestsToRun() {
    List<SQLCipherTest> tests = new ArrayList<>();
    tests.add(new LoopingInsertTest());
//    tests.add(new FIPSTest());
//    tests.add(new PragmaCipherVersionTest());
//    tests.add(new VerifyCipherProviderTest());
//    tests.add(new VerifyCipherProviderVersionTest());
//    tests.add(new JavaClientLibraryVersionTest());
//    tests.add(new ReadWriteUserVersionTest());
//    tests.add(new QueryDataSizeTest());
//    tests.add(new FixedCursorWindowAllocationTest());
//    tests.add(new GrowingCursorWindowAllocationTest());
//    tests.add(new ReadWriteWriteAheadLoggingTest());
//    tests.add(new SQLiteOpenHelperEnableWriteAheadLogBeforeGetDatabaseTest());
//    tests.add(new SQLiteOpenHelperEnableWriteAheadLogAfterGetDatabaseTest());
//    tests.add(new SQLiteOpenHelperGetNameTest());
//    tests.add(new SQLiteOpenHelperOnDowngradeTest());
//    tests.add(new SQLiteOpenHelperConfigureTest());
//    tests.add(new CheckIsDatabaseIntegrityOkTest());
//    tests.add(new GetAttachedDatabasesTest());
//    tests.add(new EnableForeignKeyConstraintsTest());
//    tests.add(new ForeignKeyConstraintsEnabledWithTransactionTest());
//    tests.add(new EnableWriteAheadLoggingTest());
//    tests.add(new DisableWriteAheadLoggingTest());
//    tests.add(new CheckIsWriteAheadLoggingEnabledTest());
//    tests.add(new WriteAheadLoggingWithTransactionTest());
//    tests.add(new WriteAheadLoggingWithInMemoryDatabaseTest());
//    tests.add(new WriteAheadLoggingWithAttachedDatabaseTest());
//    tests.add(new TransactionNonExclusiveTest());
//    tests.add(new TransactionWithListenerTest());
//    tests.add(new LargeDatabaseCursorAccessTest());

//    tests.add(new TimeLargeByteArrayQueryTest());

//    tests.add(new QueryLimitTest());
//    tests.add(new RTreeTest());
//    tests.add(new ReadWriteDatabaseToExternalStorageTest());
//    tests.add(new BeginTransactionTest());
//    tests.add(new QueryTenThousandDataTest());
//    tests.add(new CompileBeginTest());
//    tests.add(new TimeQueryExecutionTest());
//    tests.add(new UnicodeTest());
//    tests.add(new QueryIntegerToStringTest());
//    tests.add(new QueryFloatToStringTest());
//    tests.add(new ClosedDatabaseTest());
//    tests.add(new AttachDatabaseTest());
//    tests.add(new CipherMigrateTest());
//    tests.add(new GetTypeFromCrossProcessCursorWrapperTest());
//    tests.add(new InvalidPasswordTest());
//    tests.add(new NullQueryResultTest());
//    tests.add(new CrossProcessCursorQueryTest());
//    tests.add(new InterprocessBlobQueryTest());
//    tests.add(new LoopingQueryTest());
//    tests.add(new LoopingCountQueryTest());
//    tests.add(new AttachNewDatabaseTest());
//    tests.add(new AttachExistingDatabaseTest());
//    tests.add(new CanThrowSQLiteExceptionTest());
//    tests.add(new RawExecSQLTest());
//    tests.add(new RawExecSQLExceptionTest());
//    tests.add(new CompiledSQLUpdateTest());
//    tests.add(new AES128CipherTest());
//    tests.add(new MigrateDatabaseFrom1xFormatToCurrentFormat());
//    tests.add(new StatusMemoryUsedTest());
//    tests.add(new ImportUnencryptedDatabaseTest());
//    tests.add(new FullTextSearchTest());
//    tests.add(new ReadableDatabaseTest());
//    tests.add(new AutoVacuumOverReadTest());
//    tests.add(new ReadableWritableAccessTest());
//    tests.add(new CursorAccessTest());
//    tests.add(new VerifyOnUpgradeIsCalledTest());
//    tests.add(new MigrationUserVersion());
//    tests.add(new ExportToUnencryptedDatabase());
//    tests.add(new QueryNonEncryptedDatabaseTest());
//    tests.add(new EnableForeignKeySupportTest());
//    tests.add(new AverageOpenTimeTest());
//    tests.add(new NestedTransactionsTest());
//    tests.add(new ComputeKDFTest());
//    tests.add(new SoundexTest());
//    tests.add(new RawQueryTest());
//    tests.add(new OpenReadOnlyDatabaseTest());
//    tests.add(new RawRekeyTest());
//    tests.add(new CorruptDatabaseTest());
//    tests.add(new CustomCorruptionHandlerTest());
//    tests.add(new MultiThreadReadWriteTest());
//    tests.add(new VerifyUTF8EncodingForKeyTest());
//    tests.add(new TextAsIntegerTest());
//    tests.add(new TextAsDoubleTest());
//    tests.add(new TextAsLongTest());
//    tests.add(new CreateNonEncryptedDatabaseTest());
//    tests.add(new ChangePasswordTest());
//    tests.add(new ReadableWritableInvalidPasswordTest());
//    tests.add(new InvalidOpenArgumentTest());
//    tests.add(new CopyStringToBufferTestFloatSmallBuffer());
//    tests.add(new CopyStringToBufferTestFloatLargeBuffer());
//    tests.add(new CopyStringToBufferTestIntegerSmallBuffer());
//    tests.add(new CopyStringToBufferTestIntegerLargeBuffer());
//    tests.add(new CopyStringToBufferTestStringSmallBuffer());
//    tests.add(new CopyStringToBufferTestStringLargeBuffer());
//    tests.add(new CopyStringToBufferNullTest());
//    tests.add(new OpenSQLCipher3DatabaseTest());
//    tests.add(new MUTF8ToUTF8WithNullMigrationTest());
//    tests.add(new RawQuerySyntaxErrorMessageTest());
//    tests.add(new RawQueryNonsenseStatementErrorMessageTest());
//    tests.add(new RawQueryNoSuchFunctionErrorMessageTest());
//    tests.add(new CompileStatementSyntaxErrorMessageTest());
//    tests.add(new ExecuteInsertConstraintErrorMessageTest());
//    tests.add(new InsertWithOnConflictTest());
//    tests.add(new FTS5Test());
//    tests.add(new BindBooleanRawQueryTest());
//    tests.add(new BindStringRawQueryTest());
//    tests.add(new BindDoubleRawQueryTest());
//    tests.add(new BindLongRawQueryTest());
//    tests.add(new BindFloatRawQueryTest());
//    tests.add(new BindByteArrayRawQueryTest());
//    tests.add(new NullRawQueryTest());
//    tests.add(new ReadWriteDatabaseToExternalStorageTest());

    return tests;
  }
}
