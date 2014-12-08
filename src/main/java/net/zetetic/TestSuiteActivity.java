package net.zetetic;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import net.zetetic.tests.ResultNotifier;
import net.zetetic.tests.TestResult;
import net.zetetic.tests.TestSuiteRunner;

import java.util.ArrayList;
import java.util.List;

public class TestSuiteActivity extends Activity implements ResultNotifier {

    private static String TAG = "net.zetetic.sqlcipher.test";
    ListView resultsView;
    List<TestResult> results;
    View statsView;

    public TestSuiteActivity(){
        results = new ArrayList<TestResult>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
        Bundle args = getIntent().getExtras();
        if(args != null){
            if(args.containsKey("run")){
                onButtonClick(null);
            }
        }
    }

    public void onButtonClick(View view) {

        results.clear();
        hideStats();
        findViewById(R.id.executeSuite).setEnabled(false);
        resultsView = (ListView) findViewById(R.id.test_suite_results);
        ZeteticApplication.getInstance().setCurrentActivity(this);
        new TestSuiteRunner().execute(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void send(TestResult result) {

        results.add(result);
        HeaderViewListAdapter adapter = (HeaderViewListAdapter) resultsView.getAdapter();
        if(adapter == null){
            statsView = View.inflate(this, R.layout.test_stats, null);
            resultsView.addHeaderView(statsView);
            hideStats();
            resultsView.setAdapter(new TestResultAdapter(ZeteticApplication.getInstance(), results));
        } else {
            ((ArrayAdapter<TestResult>)adapter.getWrappedAdapter()).notifyDataSetChanged();
        }
    }

    @Override
    public void complete() {

        TextView stats = (TextView) statsView.findViewById(R.id.stats);
        int successCount = 0;
        for(TestResult result : results){
            if(result.isSuccess()){
                successCount += 1;
            }
        }
        stats.setText(String.format("Passed: %d  Failed: %d", successCount, results.size() - successCount));
        stats.setVisibility(View.VISIBLE);
        findViewById(R.id.executeSuite).setEnabled(true);
    }

    private void hideStats(){
        if(statsView != null){
            statsView.findViewById(R.id.stats).setVisibility(View.GONE);
        }
    }
}