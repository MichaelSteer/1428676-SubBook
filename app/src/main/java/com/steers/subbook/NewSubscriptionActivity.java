package com.steers.subbook;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.steers.Subscription.CommentTooLongException;
import com.steers.Subscription.NameTooLongException;
import com.steers.Subscription.NegativeCostException;
import com.steers.Subscription.Subscription;

import java.sql.Date;
import java.text.NumberFormat;
import java.util.Calendar;

public class NewSubscriptionActivity extends AppCompatActivity {

    private EditText subscriptionEditText, dateEditText, descriptionEditText, amountEditText;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subscription);

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
        date = Date.valueOf(dateString);

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
        } catch (NameTooLongException e) {
            Log.d("BADNAME", "addButtonPressed: DIDNT WORK");
            errorMessage.setText(R.string.NameTooLongErrorString);
        } catch (NegativeCostException e) {
            Log.d("BADVALUE", "addButtonPressed: DIDNT WORK");
            errorMessage.setText(R.string.NegativeAmountErrorString);
        } catch (CommentTooLongException e) {
            Log.d("BADDESC", "addButtonPressed: DIDNT WORK");
            errorMessage.setText(R.string.DescriptionTooLongErrorString);
        }
    }
}
