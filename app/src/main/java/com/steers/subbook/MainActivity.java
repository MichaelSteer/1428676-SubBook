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

public class MainActivity extends AppCompatActivity {

    //private Subscription test = new Subscription("Netflix", 10.99, new Date(System.currentTimeMillis()), "Netflix Media Service");
    //private Subscription test2 = new Subscription("Rent", 1000.00, new Date(System.currentTimeMillis()), "Apartment Rent");
    //private Subscription test3 = new Subscription("Utilities", 250.5, new Date(System.currentTimeMillis()),"Apartment Utilities");
    //private ArrayList<Subscription> testlist = new ArrayList<Subscription>();
    private ArrayList<Subscription> subscriptions;
    private SubscriptionAdapter subscriptionAdapter;

    public MainActivity() throws NameTooLongException, CommentTooLongException, NegativeCostException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CREATE", "onCreate: CREATED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //testlist.add(test);
        //testlist.add(test2);
        //testlist.add(test3);

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

    public void newSubscription(View view) {
        Intent intent = new Intent(this, NewSubscriptionActivity.class);
        startActivityForResult(intent, 1);
    }

    public void editSubscription(AdapterView<?> adapterView, int position, long id) {

        Log.d("LISTCLICK", "onItemClick: Clicked Item" + position);
        Intent intent = new Intent(this, NewSubscriptionActivity.class);

        Subscription selected = (Subscription) adapterView.getItemAtPosition(position);
        Log.d("LISTCLICK", "editSubscription: " + selected.toString());

        intent.putExtras(selected.toBundle());
        intent.putExtra("Position", position);
        startActivityForResult(intent, 2);
    }

    @Override 
    protected void onPause() {
        Log.d("PAUSE", "onPause: PAUSED");
        super.onPause();
        saveSubscriptions();
    }

    @Override
    protected void onStop() {
        Log.d("PAUSE", "onStop: STOPPED");
        super.onStop();
        saveSubscriptions();
    }

    @Override
    protected void onResume() {
        Log.d("RESUME", "onResume: RESUMED");
        super.onResume();
        //loadSubscriptions();
    }

    private void loadSubscriptions() {
        Log.d("LOAD", "loadSubscriptions: Loading");
        FileInputStream fis;
        try {
            fis = openFileInput("subscriptions.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();
            Type ListType = new TypeToken<ArrayList<Subscription>>(){}.getType();
            subscriptions = gson.fromJson(in, ListType);

            if (subscriptions == null) {
                Log.d("NULL", "loadSubscriptions: Subscription was null");
                subscriptions = new ArrayList<Subscription>();
            }

        } catch (FileNotFoundException e1) {
            subscriptions = new ArrayList<Subscription>();
            Log.d("FILENOTFOUND", "loadSubscriptions: Subscriptions.txt not found");
            e1.printStackTrace();
        }
    }

    private void saveSubscriptions() {
        Log.d("SAVE", "saveSubscriptions: SAVING");
        FileOutputStream fos;
        try {
            fos = openFileOutput("subscriptions.txt", Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            String jsonString = gson.toJson(subscriptions);
            out.write(jsonString);
            //gson.toJson(subscriptions, out);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("ActivityResult", "onActivityResult: RETURNED DATA");
        Subscription modified = Subscription.fromBundle(data.getExtras());

        // New
        if (requestCode == 1) {
            Log.d("ADD", "onActivityResult: NEW RESULT");
            if (resultCode == RESULT_OK) {
                subscriptions.add(modified);
                Log.d("SUB", "onActivityResult: "+modified.toString());
            }
        }

        // Existing
        else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Log.d("EXISTING", "onActivityResult: EXISTING RESULT");
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

    private void updateTotalCost() {
        TextView totalCost = findViewById(R.id.TotalCostTextView);
        TotalSubscriptionCost subscriptionCost = new TotalSubscriptionCost(subscriptions);
        String totalCostText = "Total: " + subscriptionCost.getTotalCostString();
        totalCost.setText(totalCostText);
    }
}
