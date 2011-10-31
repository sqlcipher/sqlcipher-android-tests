package net.zetetic.tests;

import info.guardianproject.database.sqlcipher.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.util.ArrayList;
import java.util.List;

public class TestSuiteRunner {

    private ResultNotifier notifier;

    public TestSuiteRunner(ResultNotifier notifier) {
        this.notifier = notifier;
    }

    public void runSuite(){

        SQLiteDatabase.loadLibs(ZeteticApplication.getInstance());
        for(SQLCipherTest test : getTestsToRun()){
            new TestTask(notifier).execute(test);
        }
    }

    private List<SQLCipherTest> getTestsToRun(){
        List<SQLCipherTest> tests = new ArrayList<SQLCipherTest>();
        tests.add(new NullQueryResultTest());
        tests.add(new CrossProcessCursorQueryTest());
        tests.add(new LoopingQueryTest());
        return tests;
    }
}
