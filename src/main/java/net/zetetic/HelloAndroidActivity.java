package net.zetetic;

import android.app.Activity;
import info.guardianproject.database.sqlcipher.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

public class HelloAndroidActivity extends Activity {

    private static String TAG = "net.zetetic.sqlcipher.test";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
        setContentView(R.layout.main);
        SQLiteDatabase.loadLibs(this);
        loadData();
    }

    private void loadData() {
        DataStore dataStore = DataStore.getInstance(this).open("test");
        dataStore.createEntry("one for the money", "two for the show");
    }
}