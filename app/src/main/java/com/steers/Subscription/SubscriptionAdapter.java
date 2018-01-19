package com.steers.Subscription;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.steers.subbook.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Michael Steer
 * on 2018-01-18.
 */

public class SubscriptionAdapter extends ArrayAdapter<Subscription> {
    private final Activity context;
    private final ArrayList<Subscription> subscriptions;

    public SubscriptionAdapter(Activity context, ArrayList<Subscription> subscriptions) {
        super(context, R.layout.subscriptionrow, subscriptions);

        this.context = context;
        this.subscriptions = subscriptions;
    }

    public View getView(int position, View view, ViewGroup parent) {

        SimpleDateFormat sm = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.subscriptionrow, null, true);

        TextView NameTextView = rowView.findViewById(R.id.NameTextView);
        TextView DateTextView = rowView.findViewById(R.id.DateTextView);
        TextView CostTextView = rowView.findViewById(R.id.CostTextView);

        String name = subscriptions.get(position).getName();
        String date = sm.format(subscriptions.get(position).getDate());
        String cost = Double.toString(subscriptions.get(position).getCost());

        NameTextView.setText(name);
        DateTextView.setText(date);
        CostTextView.setText(cost);

        return rowView;
    }
}
