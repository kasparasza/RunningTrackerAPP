package com.example.kasparasza.runningtrackerapp.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.kasparasza.runningtrackerapp.Databases.RunEntryContract.RunEntry;

/**
 * Class that implements a set of APIs that will be used in interacting with db
 */

public class RunDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "RunExercise.db";

    // Constructor
    public RunDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method that is called when the db is created for the 1st time
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RunEntry.SQL_CREATE_RUN_EXERCISES_TABLE);
    }

    // Method that is called when the db is upgraded
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // so far the method body is left blank;
        // the default implementation could have this logic:
/*        onUpgrade(db, oldVersion, newVersion);*/
    }
}
