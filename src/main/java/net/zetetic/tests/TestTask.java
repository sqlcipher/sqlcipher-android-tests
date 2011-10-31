package net.zetetic.tests;

import android.os.AsyncTask;

public class TestTask extends AsyncTask<SQLCipherTest, Void, TestResult> {

    private ResultNotifier notifier;

    public TestTask(ResultNotifier notifier){
        this.notifier = notifier;
    }

    @Override
    protected TestResult doInBackground(SQLCipherTest... sqlCipherTests) {
        SQLCipherTest test = sqlCipherTests[0];
        return test.run();
    }

    @Override
    protected void onPostExecute(TestResult testResult) {
        notifier.send(testResult);
    }
}
