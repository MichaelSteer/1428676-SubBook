/*
 MainActivity.java
 @author Michael Steer

 Main application activity
 */
package com.steers.subbook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.steers.Subscription.CommentTooLongException;
import com.steers.Subscription.NameTooLongException;
import com.steers.Subscription.NegativeCostException;
import com.steers.Subscription.Subscription;
import com.steers.Subscription.SubscriptionAdapter;
import com.steers.Subscription.TotalSubscriptionCost;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Main Activity Class
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<Subscription> subscriptions;
    private SubscriptionAdapter subscriptionAdapter;
    private static int NewSubscriptionCode = 1;
    private static int ModifySubscriptionCode = 2;

    /**
     * Constructor
     * @throws NameTooLongException
     * @throws CommentTooLongException
     * @throws NegativeCostException
     */
    public MainActivity() throws NameTooLongException,
            CommentTooLongException,
            NegativeCostException {
    }

    /**
     * Called when the application instance is created
     * @param savedInstanceState {@code Bundle} any data that was saved from the last state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadSubscriptions();
        updateTotalCost();


        subscriptionAdapter = new SubscriptionAdapter(this, subscriptions);
        ListView listView = findViewById(R.id.SubscriptionListView);
        listView.setAdapter(subscriptionAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                editSubscription(adapterView, position, id);
            }
        });


    }

    /**
     * Called when the "New" Button is clicked
     * @param view {@code View}
     */
    public void newSubscription(View view) {
        Intent intent = new Intent(this, NewSubscriptionActivity.class);
        startActivityForResult(intent, NewSubscriptionCode);
    }

    /**
     * Called when any subscription in the ListView is clicked
     * @param adapterView {@code AdapterView<>} The arraylist adapter
     * @param position {@code int} the index in the arraylist
     * @param id {@code id} the objects id
     */
    public void editSubscription(AdapterView<?> adapterView, int position, long id) {

        Intent intent = new Intent(this, NewSubscriptionActivity.class);

        Subscription selected = (Subscription) adapterView.getItemAtPosition(position);

        intent.putExtras(selected.toBundle());
        intent.putExtra("Position", position);
        startActivityForResult(intent, 2);
    }

    /**
     * Called when the application is paused
     */
    @Override 
    protected void onPause() {
        super.onPause();
        saveSubscriptions();
    }

    /**
     * Called when the application is stopped
     */
    @Override
    protected void onStop() {
        super.onStop();
        saveSubscriptions();
    }

    /**
     * Called when the application is resumed
     */
    @Override
    protected void onResume() {
        super.onResume();
        //loadSubscriptions();
    }

    /**
     * Loads the subscriptions from a JSON file and into an ArrayList
     */
    private void loadSubscriptions() {
        Log.d("LOAD", "loadSubscriptions: Loading");
        FileInputStream fis;
        try {
            fis = openFileInput("subscriptions.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type ListType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            subscriptions = gson.fromJson(in, ListType);

            // Case where there are no subscriptions and JSON returns an empty object
            if (subscriptions == null) {
                subscriptions = new ArrayList<Subscription>();
            }
        }

        // Case where the file doesn't exist yet
        catch (FileNotFoundException e1) {
            subscriptions = new ArrayList<Subscription>();
            e1.printStackTrace();
        }
    }

    /**
     * Save the subscriptions to a file
     */
    private void saveSubscriptions() {
        FileOutputStream fos;
        try {
            fos = openFileOutput("subscriptions.txt", Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            String jsonString = gson.toJson(subscriptions);
            out.write(jsonString);
            //gson.toJson(subscriptions, out);
            out.close();
        }
        // Case when the file wasn't found or couldnt be created, or if something weird occurs
        // When writing
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when NewSubscriptionActivity returns back to MainActivity
     * @param requestCode {@code int} the request code
     * @param resultCode {@code int} the result code
     * @param data {@code Intent} the data passed between the Activities
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Subscription modified = Subscription.fromBundle(data.getExtras());

        // New
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                subscriptions.add(modified);
            }
        }

        // Existing
        else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                int position = data.getIntExtra("Position", 0);
                if (data.getBooleanExtra("delete", false))
                    subscriptions.remove(position);
                else
                    subscriptions.set(position, modified);
            }
        }
        subscriptionAdapter.notifyDataSetChanged();
        updateTotalCost();
    }

    /**
     * Calculate the total monthly cost by summing each subscriptions cost in the ArrayList
     */
    private void updateTotalCost() {
        TextView totalCost = findViewById(R.id.TotalCostTextView);
        TotalSubscriptionCost subscriptionCost = new TotalSubscriptionCost(subscriptions);
        String totalCostText = "Total: " + subscriptionCost.getTotalCostString();
        totalCost.setText(totalCostText);
    }
}
