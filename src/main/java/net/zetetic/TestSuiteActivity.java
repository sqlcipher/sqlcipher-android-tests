package net.zetetic;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import net.zetetic.tests.ResultNotifier;
import net.zetetic.tests.TestResult;
import net.zetetic.tests.TestSuiteRunner;

public class TestSuiteActivity extends Activity implements ResultNotifier {

    private static String TAG = "net.zetetic.sqlcipher.test";
    TextView resultsView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
    }

    public void onButtonClick(View view) {

        findViewById(R.id.executeSuite).setEnabled(false);
        resultsView = (TextView) findViewById(R.id.test_suite_results);
        resultsView.setMovementMethod(ScrollingMovementMethod.getInstance());
        ZeteticApplication.getInstance().setCurrentActivity(this);
        new TestSuiteRunner().execute(this);
    }

    @Override
    public void send(TestResult result) {
        resultsView.append(result.toString());
    }

    @Override
    public void complete() {
        findViewById(R.id.executeSuite).setEnabled(true);
    }
}