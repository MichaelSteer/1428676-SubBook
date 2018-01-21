package com.steers.subbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.steers.Subscription.CommentTooLongException;
import com.steers.Subscription.NameTooLongException;
import com.steers.Subscription.NegativeCostException;
import com.steers.Subscription.Subscription;
import com.steers.Subscription.SubscriptionAdapter;
import com.steers.Subscription.TotalSubscriptionCost;

import java.util.Date;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Subscription test = new Subscription("Netflix", 10.99, new Date(System.currentTimeMillis()), "Netflix Media Service");
    private Subscription test2 = new Subscription("Rent", 1000.00, new Date(System.currentTimeMillis()), "Apartment Rent");
    private Subscription test3 = new Subscription("Utilities", 250.5, new Date(System.currentTimeMillis()),"Apartment Utilities");
    private ArrayList<Subscription> testlist = new ArrayList<Subscription>();

    public MainActivity() throws NameTooLongException, CommentTooLongException, NegativeCostException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testlist.add(test);
        testlist.add(test2);
        testlist.add(test3);

        SubscriptionAdapter subscriptionAdapter = new SubscriptionAdapter(this, testlist);

        ListView listView = findViewById(R.id.SubscriptionListView);
        listView.setAdapter(subscriptionAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                editSubscription(adapterView, position, id);
            }
        });
        TextView totalCost = findViewById(R.id.TotalCostTextView);
        TotalSubscriptionCost subscriptionCost = new TotalSubscriptionCost(testlist);
        String totalCostText = "Total: " + subscriptionCost.getTotalCostString();
        totalCost.setText(totalCostText);

    }

    public void newSubscription(View view) {
        Intent intent = new Intent(this, NewSubscriptionActivity.class);
        startActivity(intent);
    }

    public void editSubscription(AdapterView<?> adapterView, int position, long id) {

        Log.d("LISTCLICK", "onItemClick: Clicked Item" + position);
        Intent intent = new Intent(this, NewSubscriptionActivity.class);

        Subscription selected = (Subscription) adapterView.getItemAtPosition(position);
        Log.d("LISTCLICK", "editSubscription: " + selected.toString());

        intent.putExtras(selected.toBundle());
        intent.putExtra("Position", position);
        startActivity(intent);
    }
}
