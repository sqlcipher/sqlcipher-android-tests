package net.zetetic.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.sqlcipher.CrossProcessCursorWrapper;
import net.sqlcipher.Cursor;
import net.sqlcipher.CursorWindow;
import net.sqlcipher.CustomCursorWindowAllocation;
import net.sqlcipher.database.SQLiteCursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.zetetic.R;
import net.zetetic.ScrollingCursorAdapter;
import net.zetetic.ZeteticApplication;

import java.io.File;

public class TestScrollingCursorActivity extends Activity {

  private SQLiteDatabase database;
  private String databaseFilename = "scrolling.db";
  private long allocationSize = 1024 * 1024 * 4;
  ListView listView;
  RadioGroup options;
  Cursor cursor;

  SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
    public void preKey(SQLiteDatabase database) {}
    public void postKey(SQLiteDatabase database) {
      database.execSQL("PRAGMA kdf_iter = 64000;");
      database.execSQL("PRAGMA cipher_page_size = 1024;");
      database.execSQL("PRAGMA cipher_hmac_algorithm = HMAC_SHA1;");
      database.execSQL("PRAGMA cipher_kdf_algorithm = PBKDF2_HMAC_SHA1;");
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.scrolling_cursor_view);
    try {
      initializeEnvironment();
      CursorWindow.setCursorWindowAllocation(new CustomCursorWindowAllocation(allocationSize, 0, allocationSize));
      options = findViewById(R.id.options);
      listView = findViewById(R.id.listView);
      RadioButton optimizeButton = findViewById(R.id.optimize_cursor);
      RadioButton explicitButton = findViewById(R.id.explicit_cursor);
      optimizeButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          bindContent(false);
        }
      });
      explicitButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          bindContent(true);
        }
      });
    } catch (Exception e) {
      Log.e(getClass().getSimpleName(), e.toString());
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    if(cursor != null) cursor.close();
    if(database != null) database.close();
  }

  private void bindContent(boolean value){
    setRadioButtonState(false);
    if(cursor != null) cursor.close();
    cursor = database.rawQuery("SELECT * FROM t1;", null);
    ((SQLiteCursor)((CrossProcessCursorWrapper)cursor).getWrappedCursor()).setFillWindowForwardOnly(value);
    final ScrollingCursorAdapter cursorAdapter = new ScrollingCursorAdapter(this, cursor);
    listView.setAdapter(cursorAdapter);
    listView.post(new Runnable() {
      @Override
      public void run() {
        listView.setSelection(cursorAdapter.getCount() - 1);
        setRadioButtonState(true);
      }
    });
  }

  void initializeEnvironment(){
    try{
      File databasePath = getDatabasePath(databaseFilename);
      ZeteticApplication.getInstance().extractAssetToDatabaseDirectory(databaseFilename);
      database = SQLiteDatabase.openDatabase(databasePath.getAbsolutePath(),
          ZeteticApplication.DATABASE_PASSWORD, null, SQLiteDatabase.OPEN_READWRITE, hook);
    } catch (Exception e){
      Log.e(getClass().getSimpleName(), e.toString());
    }
  }

  void setRadioButtonState(final boolean value) {
    for (int index = 0; index < options.getChildCount(); index++) {
      View item = options.getChildAt(index);
      if (item != null) item.setEnabled(value);
    }
  }
}
