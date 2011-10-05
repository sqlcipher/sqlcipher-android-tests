package net.zetetic;

import android.app.Activity;
import android.widget.TextView;
import info.guardianproject.database.sqlcipher.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HelloAndroidActivity extends Activity {

    private static String TAG = "net.zetetic.sqlcipher.test";
    private DataStore _dataStore;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
    }

    public void onButtonClick(View view) {
        Log.i(TAG,"onButtonClick");
        SQLiteDatabase.loadLibs(this);
        Log.i(TAG,"after loadLibs");
        _dataStore = DataStore.getInstance(this).open("test");
        _dataStore.createEntry("one for the money", "two for the show");
        Log.i(TAG,"after loadData");
        TextView tv = (TextView)findViewById(R.id.textview1);
        tv.setText(_dataStore.getData());
    }
}