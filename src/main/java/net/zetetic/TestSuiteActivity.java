package net.zetetic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import net.zetetic.tests.TestResult;
import net.zetetic.tests.TestSuiteRunner;

import java.util.List;

public class TestSuiteActivity extends Activity {

    private static String TAG = "net.zetetic.sqlcipher.test";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
    }

    public void onButtonClick(View view) {

        TestSuiteRunner runner = new TestSuiteRunner();
        List<TestResult> results = runner.runSuite();

        TextView resultsView = (TextView) findViewById(R.id.test_suite_results);
        StringBuilder buffer = new StringBuilder();
        for(TestResult result : results){
            buffer.append(result.toString() + "\n");
        }
        resultsView.setText(buffer.toString());
    }
}