package net.zetetic.tests;

import info.guardianproject.database.sqlcipher.SQLiteDatabase;
import net.zetetic.ZeteticApplication;

import java.util.ArrayList;
import java.util.List;

public class TestSuiteRunner {

    public List<TestResult> runSuite(){

        SQLiteDatabase.loadLibs(ZeteticApplication.getInstance());
        List<TestResult> results = new ArrayList<TestResult>();
        for(SQLCipherTest test : getTestsToRun()){
            results.add(test.run());
        }
        return results;
    }

    private List<SQLCipherTest> getTestsToRun(){
        List<SQLCipherTest> tests = new ArrayList<SQLCipherTest>();
        tests.add(new NullQueryResultTest());
        return tests;
    }
}
