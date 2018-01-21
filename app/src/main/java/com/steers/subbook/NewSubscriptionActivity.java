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
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewSubscriptionActivity extends AppCompatActivity {

    private EditText subscriptionEditText, dateEditText, descriptionEditText, amountEditText;
    private TextView errorMessage;
    private Bundle existingSubscripton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean existing = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subscription);

        Intent passed = getIntent();

        subscriptionEditText = findViewById(R.id.New_Subscription_Name);
        dateEditText = findViewById(R.id.New_Subscription_Date);
        descriptionEditText = findViewById(R.id.New_Subscription_Description);
        amountEditText = findViewById(R.id.New_Subscription_Amount);
        errorMessage = findViewById(R.id.New_Subscription_Error_Text);

        dateEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                    showDatePicker();
                }
                return false;
            }
        });

        amountEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                Log.d("EDITORACTION", "onEditorAction: EDITOR ACTION");
                try {
                    String text = NumberFormat.getCurrencyInstance().format(Double.parseDouble(amountEditText.getText().toString()));
                    amountEditText.setText(text);
                }
                catch (NumberFormatException e) {
                    Log.d("AMOUNT FORMAT", "onEditorAction: Bad Number formatting. Leaving the numerical value alone");
                }
                catch (NullPointerException e) {
                    Log.d("BADBREAK", "onEditorAction: SHOULDNT BE BREAKING HERE");
                }
                return false;
            }
        });

        try {
            existingSubscripton = passed.getExtras();
            if (!existingSubscripton.isEmpty()) {
                Log.d("EXISTING", "onCreate: PREXISTING SUBSCRIPTION");
                existing = true;
            }
        } catch (NullPointerException e) {
            Log.d("NONEXISTING", "onCreate: NEW SUBSCRIPTION");
        }

        // Existing Subscription
        if (existing) {
            Subscription existingSubscription = Subscription.fromBundle(existingSubscripton);
            String title = existingSubscription.getName() + ": " + getString(R.string.ExistingSubscriptionTitle);
            setTitle(title);

            Button editButton = findViewById(R.id.AddSubscriptionButton);
            editButton.setText(R.string.ExistingSubscriptionButton);

            subscriptionEditText.setText(existingSubscription.getName());
            descriptionEditText.setText(existingSubscription.getComment());
            amountEditText.setText(Double.toString(existingSubscription.getCost()));
            dateEditText.setText(existingSubscription.getDateString());
        }

        // Only if not an existing subscription
        else {
            setTitle(R.string.NewSubscriptionTitle);
        }
    }

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
        datePickerDialog.show();
    }

    public void addButtonPressed(View v) {
        SimpleDateFormat formatter = new SimpleDateFormat(Subscription.DATE_FORMAT);
        boolean success = true;
        String name, amountText, dateString, description;
        Date date;
        double amount;

        name = subscriptionEditText.getText().toString();
        if (name.matches("")) {
            Log.d("NULLPTR", "addButtonPressed: Missing Subscription Text");
            errorMessage.setText("Enter a Subscription Name");
            return;
        }

        dateString = dateEditText.getText().toString();
        if(dateString.matches("")) {
            Log.d("NULLPTR", "addButtonPressed: Missing date value");
            errorMessage.setText("Enter a Date");
            return;
        }
        try {
            date = formatter.parse(dateString);
        }
        catch (ParseException e) {
            Log.d("PARSEEXCEPTION", "addButtonPressed: Bad Date");
            errorMessage.setText("Invalid Date");
            return;
        }

        amountText = amountEditText.getText().toString();

        if(amountText.matches("")) {
            Log.d("NULLPTR", "addButtonPressed: Missing amount Text");
            errorMessage.setText("Enter a monthly amount");
            return;
        }

        if(amountText.startsWith("$"))
            amountText = amountText.substring(1);
        amount = Double.parseDouble(amountText);

        description = descriptionEditText.getText().toString();

        try {
            Log.d("MADE IT", "addButtonPressed: MADE IT THIS FAR");
            Subscription newSubscription = new Subscription(name, amount, date, description);
            Log.d("FINISHED", "addButtonPressed: Finished adding new entry");
            this.finish();
        }

        catch (NameTooLongException e) {
            Log.d("BADNAME", "addButtonPressed: DIDNT WORK");
            errorMessage.setText(R.string.NameTooLongErrorString);
        }

        catch (NegativeCostException e) {
            Log.d("BADVALUE", "addButtonPressed: DIDNT WORK");
            errorMessage.setText(R.string.NegativeAmountErrorString);
        }

        catch (CommentTooLongException e) {
            Log.d("BADDESC", "addButtonPressed: DIDNT WORK");
            errorMessage.setText(R.string.DescriptionTooLongErrorString);
        }
    }
}
