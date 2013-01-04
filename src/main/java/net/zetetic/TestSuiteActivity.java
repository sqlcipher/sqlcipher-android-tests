package net.zetetic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import net.zetetic.tests.ResultNotifier;
import net.zetetic.tests.TestResult;
import net.zetetic.tests.TestSuiteRunner;

import java.util.ArrayList;
import java.util.List;

public class TestSuiteActivity extends Activity implements ResultNotifier {

    private static String TAG = "net.zetetic.sqlcipher.test";
    ListView resultsView;
    List<TestResult> results;

    public TestSuiteActivity(){
        results = new ArrayList<TestResult>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
    }

    public void onButtonClick(View view) {

        findViewById(R.id.executeSuite).setEnabled(false);
        resultsView = (ListView) findViewById(R.id.test_suite_results);
        ZeteticApplication.getInstance().setCurrentActivity(this);
        new TestSuiteRunner().execute(this);
    }

    @Override
    public void send(TestResult result) {

        results.add(result);
        ArrayAdapter<TestResult> adapter = (ArrayAdapter<TestResult>) resultsView.getAdapter();
        if(adapter == null){
            resultsView.setAdapter(new TestResultAdapter(ZeteticApplication.getInstance(), results));
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void complete() {
        findViewById(R.id.executeSuite).setEnabled(true);
    }
}