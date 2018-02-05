/*
 SubscriptionAdapter.java
 @author Michael Steer
 */
package com.steers.Subscription;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.steers.subbook.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * SubscriptionAdapter class. Takes an @code{ArrayList<Subscription>}
 * and produces an adapter for use with Androids ListView
 *
 * References: This tutorial https://appsandbiscuits.com/listview-tutorial-android-12-ccef4ead27cc
 * for how ListViews work and sniplets of CMPUT 301 Lab code
 */
public class SubscriptionAdapter extends ArrayAdapter<Subscription> {
    private final Activity context;
    private final ArrayList<Subscription> subscriptions;

    /**
     * Constructor
     * @param context {@code Activity} The activity making the function call
     * @param subscriptions {@code ArrayList<Subscription>} the subscription list
     */
    public SubscriptionAdapter(Activity context, ArrayList<Subscription> subscriptions) {
        super(context, R.layout.subscriptionrow, subscriptions);

        this.context = context;
        this.subscriptions = subscriptions;
    }

    /**
     * Returns an individual list element as a view
     * @param position {@code int} the position in the ArrayList
     * @param view {@code View} the input view
     * @param parent {@code View} the ListView
     * @return
     */
    public View getView(int position, View view, ViewGroup parent) {

        SimpleDateFormat sm = new SimpleDateFormat(Subscription.DATE_FORMAT, Locale.getDefault());

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.subscriptionrow, null, true);

        TextView NameTextView = rowView.findViewById(R.id.NameTextView);
        TextView DateTextView = rowView.findViewById(R.id.DateTextView);
        TextView CostTextView = rowView.findViewById(R.id.CostTextView);

        String name = subscriptions.get(position).getName();
        String date = subscriptions.get(position).getDateString();
        String cost = NumberFormat.getCurrencyInstance().format(subscriptions.get(position).getCost());

        NameTextView.setText(name);
        DateTextView.setText(date);
        CostTextView.setText(cost);

        return rowView;
    }
}
