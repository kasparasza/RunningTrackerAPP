package com.example.kasparasza.runningtrackerapp.Databases;

import android.app.Application;
import android.content.Context;
import android.provider.BaseColumns;

/**
 * A contract class that specifies the layout of RunningTracker db schema
 */

public final class RunEntryContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private RunEntryContract() {}

    /* Inner class that defines the table contents */
    public static class RunEntry implements BaseColumns {

        /**
         * Name of the db table
         */
        public static final String TABLE_NAME = "RunningExercises";

        /*
        * Unique ID number for each exercise (only for use in the database table)
        *
        * Type: INTEGER
        **/
        public static final String _ID = BaseColumns._ID;

        /*
        * Date when each run exercise was performed (format: YYYY-MM-DD)
        *
        * Type: TEXT
        **/
        public static final String COLUMN_DATE_OF_RUN = "date";

        /*
        * Type of each run exercise (limited to one of the types outlined below)
        *
        * Type: INTEGER
        **/
        public static final String COLUMN_RUN_TYPE = "run_type";

        /*
        * Distance of each run exercise (measured in kilometers)
        *
        * Type: REAL
        **/
        public static final String COLUMN_MAIN_RUN_DISTANCE = "main_distance";

        /*
        * Duration of each run exercise (measured in minutes)
        *
        * Type: REAL
        **/
        public static final String COLUMN_MAIN_RUN_DURATION = "main_duration";

        /*
        * Number of run repeats made during each run exercise
        *
        * Type: INTEGER
        **/
        public static final String COLUMN_RUN_REPEATS_MADE = "run_repeats";

        /*
        * Distance of run between the run repeats made during each run exercise (measured in kilometers)
        *
        * Type: REAL
        **/
        public static final String COLUMN_BREAK_DISTANCE = "break_distance";


        /**
         * Available values for the type of each run exercise.
         */
        public static final int TYPE_RECOVERY_RUN = 1;
        public static final int TYPE_FARTLEK = 2;
        public static final int TYPE_VO2_MAX = 3;
        public static final int TYPE_TEMPO_RUN = 4;
        public static final int TYPE_LONG_RUN = 5;

        /**
         * String constant that creates the table.
         */
        public static final String SQL_CREATE_RUN_EXERCISES_TABLE =
                "CREATE TABLE " + RunEntry.TABLE_NAME + " (" +
                        RunEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RunEntry.COLUMN_DATE_OF_RUN + " TEXT NOT NULL DEFAULT CURRENT_DATE, " +
                        RunEntry.COLUMN_RUN_TYPE + " INTEGER NOT NULL, " +
                        RunEntry.COLUMN_MAIN_RUN_DISTANCE + " REAL NOT NULL, " +
                        RunEntry.COLUMN_MAIN_RUN_DURATION + " REAL NOT NULL, " +
                        RunEntry.COLUMN_RUN_REPEATS_MADE + " INTEGER DEFAULT 1, " +
                        RunEntry.COLUMN_BREAK_DISTANCE + " REAL DEFAULT 0);";
    }
}
