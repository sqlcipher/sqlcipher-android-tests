package net.zetetic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import net.sqlcipher.Cursor;

public class ScrollingCursorAdapter extends CursorAdapter {

  public ScrollingCursorAdapter(Context context, Cursor cursor){
    super(context, cursor, 0);
  }

  @Override
  public View newView(Context context, android.database.Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(R.layout.cursor_item, parent, false);
  }

  @Override
  public void bindView(View view, Context context, android.database.Cursor cursor) {
    TextView messageDisplay = view.findViewById(R.id.message);
    String message = cursor.getString (cursor.getColumnIndex("a"));
    messageDisplay.setText(message);
  }
}
