package net.zetetic.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import net.zetetic.R;

public class TestRunnerSelectionActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.test_runners);
    Button behaviorSuite = findViewById(R.id.run_behavior_test_suite);
    Button scrollingCursorSuite = findViewById(R.id.run_scrolling_test_suite);
    Button supportSuite = findViewById(R.id.run_support_test_suite);
    if(behaviorSuite != null){
      behaviorSuite.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(TestRunnerSelectionActivity.this,
              TestSuiteBehaviorsActivity.class);
          startActivity(intent);
        }
      });
    }
    if(scrollingCursorSuite != null){
      scrollingCursorSuite.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(TestRunnerSelectionActivity.this,
              TestScrollingCursorActivity.class);
          startActivity(intent);
        }
      });
    }
    if(supportSuite != null){
      supportSuite.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(TestRunnerSelectionActivity.this,
            TestSuiteBehaviorsActivity.class).putExtra(TestSuiteBehaviorsActivity.EXTRA_IS_SUPPORT, true);
          startActivity(intent);
        }
      });
    }
  }
}
