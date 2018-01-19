package com.steers.subbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.steers.Subscription.CommentTooLongException;
import com.steers.Subscription.NameTooLongException;
import com.steers.Subscription.NegativeCostException;
import com.steers.Subscription.Subscription;
import com.steers.Subscription.SubscriptionAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Subscription test = new Subscription("Netflix", 10.99, "Netflix Media Service");
    private ArrayList<Subscription> testlist = new ArrayList<Subscription>();

    public MainActivity() throws NameTooLongException, CommentTooLongException, NegativeCostException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testlist.add(test);
        SubscriptionAdapter subscriptionAdapter = new SubscriptionAdapter(this, testlist);

        ListView listView = findViewById(R.id.SubscriptionListView);
        listView.setAdapter(subscriptionAdapter);
    }

    public void newSubscription(View view) {
        Intent intent = new Intent(this, NewSubscriptionActivity.class);
        startActivity(intent);
    }
}
