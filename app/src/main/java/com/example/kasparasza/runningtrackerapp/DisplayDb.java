package com.example.kasparasza.runningtrackerapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kasparasza.runningtrackerapp.Databases.RunDbHelper;
import com.example.kasparasza.runningtrackerapp.Databases.RunEntryContract.RunEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DisplayDb extends AppCompatActivity {

    // Binding / declaration of the UI Views
    @BindView(R.id.database_contents) TextView databaseContents;
    @BindView(R.id.add_exercise_button) Button addToDbButton;

    // Global variables of the class:
    // instance of the RunDbHelper class
    private RunDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding / initialisation - committing the annotations being used to the UI
        ButterKnife.bind(this);

        // Instantiate a helper class for the interaction with db
        mDbHelper = new RunDbHelper(this);

        // initiate the methods that displays contents of the db
        displayDbContents(readDb());
    }

    ////////
    // Methods that implement elements of the UI:
    ////////

    // set up of OnClick Listener for addToDbButton
    @OnClick(R.id.add_exercise_button) public void startIntent(){
        Intent startUpdateDbActivity = new Intent(DisplayDb.this, UpdateDb.class);
        startActivity(startUpdateDbActivity);
    }

    ////////
    // Methods that read data from the db:
    ////////

    /*
    * Method that reads the db and returns contents of the query in a Cursor object
    * @return Cursor
    * */
    private Cursor readDb (){
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                RunEntry._ID,
                RunEntry.COLUMN_DATE_OF_RUN,
                RunEntry.COLUMN_RUN_TYPE,
                RunEntry.COLUMN_MAIN_RUN_DISTANCE,
                RunEntry.COLUMN_MAIN_RUN_DURATION,
                RunEntry.COLUMN_RUN_REPEATS_MADE,
                RunEntry.COLUMN_BREAK_DISTANCE
        };

        // Filter results WHERE "title" = 'My Title'
        // other parameters are set to null
        Cursor cursorWithContents = db.query(
                RunEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't use any sort order
        );
        // return contents of the query in a Cursor object
       return cursorWithContents;
    }


    /*
    * Method that displays contents of the Cursor object
    * @param Cursor with contents of the query from the db
    * */
    private void displayDbContents(Cursor cursor) {
        // First we create a few header rows which will display to the user short summary
        // of the db contents and its columns
        try {
            // Create a header in the Text View that looks like this:
            //
            // The RunningExercises table contains <number of rows in Cursor> exercises.
            // _id - date - run_type - main_distance - main_duration - run_repeats - break_distance
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            databaseContents.setText(getString(R.string.Contents_of_the_db_p01) + cursor.getCount() + getString(R.string.Contents_of_the_db_p02));
            databaseContents.append(RunEntry._ID + " - " +
                    RunEntry.COLUMN_DATE_OF_RUN + " - " +
                    RunEntry.COLUMN_RUN_TYPE + " - " +
                    RunEntry.COLUMN_MAIN_RUN_DISTANCE + " - " +
                    RunEntry.COLUMN_MAIN_RUN_DURATION + " - " +
                    RunEntry.COLUMN_RUN_REPEATS_MADE + " - " +
                    RunEntry.COLUMN_BREAK_DISTANCE + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(RunEntry._ID);
            int dateColumnIndex = cursor.getColumnIndex(RunEntry.COLUMN_DATE_OF_RUN);
            int typeColumnIndex = cursor.getColumnIndex(RunEntry.COLUMN_RUN_TYPE);
            int mainDistanceColumnIndex = cursor.getColumnIndex(RunEntry.COLUMN_MAIN_RUN_DISTANCE);
            int mainDurationColumnIndex = cursor.getColumnIndex(RunEntry.COLUMN_MAIN_RUN_DURATION);
            int repeatsColumnIndex = cursor.getColumnIndex(RunEntry.COLUMN_RUN_REPEATS_MADE);
            int repeatsDistanceColumnIndex = cursor.getColumnIndex(RunEntry.COLUMN_BREAK_DISTANCE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String, Int or Float value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentDate = cursor.getString(dateColumnIndex);
                int currentTypeInt = cursor.getInt(typeColumnIndex);
                float currentMainDistance = cursor.getFloat(mainDistanceColumnIndex);
                float currentMainDuration = cursor.getFloat(mainDurationColumnIndex);
                int currentRepeats = cursor.getInt(repeatsColumnIndex);
                float currentRepeatsDistance = cursor.getFloat(repeatsDistanceColumnIndex);

                // type of run is stored as an integer in the db,
                // we call a method that converts it back to String
                String currentTypeString = getTypeAsString(currentTypeInt);

                // Display the values from each column of the current row in the cursor in the TextView
                databaseContents.append(("\n" + currentID + " - " +
                        currentDate + " - " + currentTypeString + " - " + currentMainDistance + " - " +
                        currentMainDuration + " - " + currentRepeats + " - " + currentRepeatsDistance));
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /*
    * Method that decodes String corresponding the particular int value
    * @param integer that corresponds to one of the types of run
    * */
    private String getTypeAsString(int typeInt){
        String typeString;
        switch (typeInt){
            case 1: typeString = getString(R.string.type_1);
                break;
            case 2: typeString = getString(R.string.type_2);
                break;
            case 3: typeString = getString(R.string.type_3);
                break;
            case 4: typeString = getString(R.string.type_4);
                break;
            case 5: typeString = getString(R.string.type_5);
                break;
            default: typeString = getString(R.string.type_1);
        }
        return typeString;
    }
}
