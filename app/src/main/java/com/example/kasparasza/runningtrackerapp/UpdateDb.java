package com.example.kasparasza.runningtrackerapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kasparasza.runningtrackerapp.Databases.RunDbHelper;
import com.example.kasparasza.runningtrackerapp.Databases.RunEntryContract.RunEntry;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// Activity implements custom interface (OnDateSetByUserListener) that allows it to get information
// from the child Fragment. Definition of the interface is in the Fragment
public class UpdateDb extends AppCompatActivity implements DatePickerFragment.OnDateSetByUserListener {

    // Binding / declaration of the UI Views
    @BindView(R.id.date_input_display) TextView dateEntryDisplay;
    @BindView(R.id.run_type_input) Spinner runTypeSpinner;
    @BindView(R.id.distance_input) EditText mainDistanceEntry;
    @BindView(R.id.duration_input) EditText durationEntry;
    @BindView(R.id.repeats_input) EditText repeatsEntry;
    @BindView(R.id.break_length_input) EditText breakDistanceEntry;
    @BindView(R.id.confirm_button) Button confirmInput;

    // Binding / declaration of resources used
    @BindString(R.string.Toast_text_successful_adition_to_db) String ToastText; // annotation was used only for one instance in order to try out the code

    // String constants used:
    private static final String DATE_KEY = "DATE KEY";

    // other variables:
    // Possible values of the mType variable are 1, 2, 3, 4 and 5.
    // The variable is used by the Spinner object
    private int mType = 1;

    // String variable that holds the data for the date of a training
    private String dateSet;

    // instance of a calendar
    private Calendar calendar = Calendar.getInstance();

    // instance of the RunDbHelper class
    private RunDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_db);
        // Binding / initialisation - committing the annotations being used to the UI
        ButterKnife.bind(this);

        // Set up of initial date to be displayed by the dateEntryDisplay TextView
        // if the savedInstanceState is null (that is the activity was not restarted e.g. due to orientation change)
        // we set the initial date equal to the current one
        if (savedInstanceState == null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            dateSet = formatter.format(calendar.getTime());
            dateEntryDisplay.setText(dateSet);
        } else {
            dateSet = savedInstanceState.getString(DATE_KEY);
            dateEntryDisplay.setText(dateSet);
        }

        // Set up of Spinner Object
        setupSpinner();

        // Instantiate a helper class for the interaction with db
        mDbHelper = new RunDbHelper(this);
    }

    ////////
    // Methods that implement elements of the UI:
    ////////

    // Set up of OnClickListener for Date Picker
    @OnClick(R.id.date_input_display) public void onClickDatePicker(View view) {
        // DatePickerFragment is called
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // Set up of OnClickListener for the button that triggers info input into db
    // and returns to the parent activity
    @OnClick(R.id.confirm_button) public void onClickConfirmButton(View view) {
        // write data to db
        writeToDb();
        Intent returnToDisplayDbActivity = new Intent(UpdateDb.this, DisplayDb.class);
        // exit this activity
        finish();
        // start the parent activity
        startActivity(returnToDisplayDbActivity);
    }

    /**
     * Setup the dropdown spinner that allows the user to select the type of training.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter runTypeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_run_type_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        runTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        runTypeSpinner.setAdapter(runTypeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        runTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.type_1))) {
                        mType = RunEntry.TYPE_RECOVERY_RUN; // recovery run
                    } else if (selection.equals(getString(R.string.type_2))) {
                        mType = RunEntry.TYPE_FARTLEK; // fartlek
                    } else if (selection.equals(getString(R.string.type_3))){
                       mType = RunEntry.TYPE_VO2_MAX; // VO2 max
                    } else if (selection.equals(getString(R.string.type_4))){
                        mType = RunEntry.TYPE_TEMPO_RUN; // tempo run
                    } else {
                        mType = RunEntry.TYPE_LONG_RUN; // long run
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mType = 1; // recovery run
            }
        });
    }

    /*
    * Method that is required to be implemented by OnDateSetByUserListener interface
    * The method sets the date to be displayed by the UI
    * @param date - date that was selected in the DatePickerFragment
    *
    * */
    public void onDateSetByUser(String date) {
        // The user selected has selected the date in DatePickerFragment
        // We use this date to update the UI
        dateSet = date;
        dateEntryDisplay.setText(date);
    }

    ////////
    // Methods that save data on orientation change:
    ////////

    /*
    * Method that saves variables in relation to Activity lifecycle (e.g. orientation change)
    * */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(DATE_KEY, dateSet);
    }

    /*
    * Method that restores variables in relation to Activity lifecycle (e.g. orientation change)
    * */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dateSet = savedInstanceState.getString(DATE_KEY);
    }

    ////////
    // Methods that write data to the db:
    ////////

    /*
    * Method that inserts a new row with run exercise data into the db
    * */
    private void writeToDb () {
        // Read the input data
        float mainDistance = Float.valueOf(handleEmptyStringToZero(mainDistanceEntry.getText().toString().trim()));
        float duration = Float.valueOf(handleEmptyStringToZero(durationEntry.getText().toString().trim()));
        int repeats = Integer.parseInt(handleEmptyStringToOne(repeatsEntry.getText().toString().trim()));
        float breakDistance = Float.valueOf(handleEmptyStringToZero(breakDistanceEntry.getText().toString().trim()));

        // Create a new map of values, where column names are the keys
        ContentValues contentValues = new ContentValues();
        contentValues.put(RunEntry.COLUMN_DATE_OF_RUN, dateSet);
        contentValues.put(RunEntry.COLUMN_RUN_TYPE, mType);
        contentValues.put(RunEntry.COLUMN_MAIN_RUN_DISTANCE, mainDistance);
        contentValues.put(RunEntry.COLUMN_MAIN_RUN_DURATION, duration);
        contentValues.put(RunEntry.COLUMN_RUN_REPEATS_MADE, repeats);
        contentValues.put(RunEntry.COLUMN_BREAK_DISTANCE, breakDistance);

        // Gets the db into write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Insert the data into the db
        long newRowId = db.insert(RunEntry.TABLE_NAME, null, contentValues);

        // display a toast with a feedback if the input was successful
        if (newRowId < 0 ){
            Toast.makeText(this, getString(R.string.Toast_text_failed_adition_to_db), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, ToastText + newRowId, Toast.LENGTH_SHORT).show();
        }

    }

    /*
    * Method that assigns value of "0" for cases where no input is made to EditText
    * this is done to avoid an exception in valueOf() method
    * @param inputString
    * @return inputString or "0"
    * */
    private String handleEmptyStringToZero(String inputString){
        String returnString;
        if (inputString.isEmpty()){
            return returnString = "0";
        } else{
            return inputString;
        }
    }

    /*
    * Method that assigns value of "1" for cases where no input is made to EditText
    * this is done to avoid an exception in parseInt() method
    * @param inputString
    * @return inputString or "1"
    * */
    private String handleEmptyStringToOne(String inputString){
        String returnString;
        if (inputString.isEmpty()){
            return returnString = "1";
        } else{
            return inputString;
        }
    }
}
