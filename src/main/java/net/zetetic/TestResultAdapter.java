package net.zetetic;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import net.zetetic.tests.TestResult;

import java.util.List;

public class TestResultAdapter extends ArrayAdapter<TestResult> {

    public TestResultAdapter(Context context, List<TestResult> results) {
        super(context, 0, results);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if(view == null){
            LayoutInflater viewInflater = (LayoutInflater) ZeteticApplication.getInstance().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = viewInflater.inflate(R.layout.test_result_row, null);
            holder = new ViewHolder();
            holder.testName = (TextView) view.findViewById(R.id.test_name);
            holder.testStatus = (TextView) view.findViewById(R.id.test_status);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }
        TestResult result = getItem(position);
        holder.testName.setText(result.getName());
        holder.testStatus.setText(result.toString());
        int displayColor = result.isSuccess() ? Color.GREEN : Color.RED;
        holder.testStatus.setTextColor(displayColor);
        return view;
    }

    static class ViewHolder {
        TextView testName;
        TextView testStatus;
    }
}
