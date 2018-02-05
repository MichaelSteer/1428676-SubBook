/*
 NewSubscriptionActivity.java
 @author Michael Steer

 Activity for creating and editing subscriptions
 */
package com.steers.subbook;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.steers.Subscription.CommentTooLongException;
import com.steers.Subscription.NameTooLongException;
import com.steers.Subscription.NegativeCostException;
import com.steers.Subscription.Subscription;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * New / Edit Subscription activity class
 */
public class NewSubscriptionActivity extends AppCompatActivity {

    private EditText subscriptionEditText, dateEditText, descriptionEditText, amountEditText;
    private TextView errorMessage;
    private Bundle existingSubscripton;
    private boolean existing;
    private boolean toDelete = false;

    int position;

    /**
     * Called when the class is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        existing = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subscription);

        Intent passed = getIntent();

        // Text boxes
        subscriptionEditText = findViewById(R.id.New_Subscription_Name);
        dateEditText = findViewById(R.id.New_Subscription_Date);
        descriptionEditText = findViewById(R.id.New_Subscription_Description);
        amountEditText = findViewById(R.id.New_Subscription_Amount);
        errorMessage = findViewById(R.id.New_Subscription_Error_Text);

        // Date Edit field
        dateEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    showDatePicker();
                }
                return false;
            }
        });

        // Amount Edit field
        amountEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                try {
                    String text = amountEditText.getText().toString();
                    amountEditText.setText(text);
                }
                catch (NumberFormatException | NullPointerException e) {
                    e.printStackTrace();
                    return false;
                }
                return false;
            }
        });

        // Check to see if the subscription is new or existing by seeing if there is a bundle
        try {
            existingSubscripton = passed.getExtras();
            if (!existingSubscripton.isEmpty()) {
                existing = true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Existing Subscription
        if (existing) {
            Subscription existingSubscription = Subscription.fromBundle(existingSubscripton);

            // Change the title to display the subscription name
            String title = existingSubscription.getName() + ": " + getString(R.string.ExistingSubscriptionTitle);
            setTitle(title);

            // Change the button to display edit
            Button editButton = findViewById(R.id.AddSubscriptionButton);
            editButton.setText(R.string.ExistingSubscriptionButton);


            // Fill in fields with existing information
            subscriptionEditText.setText(existingSubscription.getName());
            descriptionEditText.setText(existingSubscription.getComment());
            amountEditText.setText(Double.toString(existingSubscription.getCost()));
            dateEditText.setText(existingSubscription.getDateString());
        }

        // New Subscription
        else {
            setTitle(R.string.NewSubscriptionTitle);

            // Get rid of the delete button
            Button deleteButton = findViewById(R.id.DeleteButton);
            deleteButton.setVisibility(View.GONE);
        }
    }

    /** Display a date picket when a text field is clicked
     * Reference: https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
     */
    public void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(NewSubscriptionActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        String dateString = year + "-" + (month + 1) + "-" + day;
                        dateEditText.setText(dateString);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    /**
     * Called when the Delete button is pressed
     * @param v {@code View}
     */
    void deleteButtonPressed(View v) {
        toDelete = true;
        addButtonPressed(v);
    }

    /**
     * Called when the add button is pressed
     * @param v {@code View}
     */
    public void addButtonPressed(View v) {
        SimpleDateFormat formatter = new SimpleDateFormat(Subscription.DATE_FORMAT);
        boolean success = true;
        String name, amountText, dateString, description;
        Date date;
        double amount;

        // Check name string to see if its null
        name = subscriptionEditText.getText().toString();
        if (name.matches("")) {
            errorMessage.setText("Enter a Subscription Name");
            return;
        }

        // Check date string to see if its valid
        dateString = dateEditText.getText().toString();
        if(dateString.matches("")) {
            errorMessage.setText("Enter a Date");
            return;
        }
        try {
            date = formatter.parse(dateString);
        }
        catch (ParseException e) {
            errorMessage.setText("Invalid Date");
            return;
        }

        amountText = amountEditText.getText().toString();

        // Check amount to see if it is valid
        if(amountText.matches("")) {
            errorMessage.setText("Enter a monthly amount");
            return;
        }

        if(amountText.startsWith("$"))
            amountText = amountText.substring(1);
        amount = Double.parseDouble(amountText);

        description = descriptionEditText.getText().toString();

        Intent data = new Intent();
        Subscription out = null;
        try {
            out = new Subscription(name, amount, date, description);
        } catch (NameTooLongException | NegativeCostException | CommentTooLongException e) {
            e.printStackTrace();
        }

        if (out == null) {
            throw new NullPointerException("This subscription shouldn't be null");
        }

        data.putExtras(out.toBundle());

        // Push an index of 0 if new so bundling doesnt complain
        if (!existing) {
            data.putExtra("Position", 0);
        }
        else {
            position = existingSubscripton.getInt("Position");
            data.putExtra("delete", toDelete);
            data.putExtra("Position", position);
        }
        setResult(RESULT_OK, data);
        finish();
    }
}
