package com.example.kasparasza.runningtrackerapp;

/**
 * A class that will listen to the input of data in date format
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    ////////
    // Implementation of an interface to communicate with the parent activity:
    ////////

    // Listener that will have to be used to share information with the parent activity
    OnDateSetByUserListener mListener;

    // Parent activity must implement this interface
    public interface OnDateSetByUserListener {
        // method / methods that are implemented by the interface
        public void onDateSetByUser(String date);
    }

    // We call onAttach because: the Fragment captures the interface implementation during its onAttach() lifecycle method,
    // and can then call the Interface methods in order to communicate with the Activity.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the parent activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mListener = (OnDateSetByUserListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnDateSetByUserListener");
        }
    }
    // Now the fragment can deliver messages to the activity by calling the onDateSetByUser() method
    // (or other methods in the interface) using the mListener instance
    // of the OnDateSetByUserListener interface.


    ////////
    // Other general methods implemented by the fragment:
    ////////

    // instance of a calendar
    private Calendar calendarDateSet = Calendar.getInstance();

    // method required to be implemented by an OnDateSetByUserListener interface
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    // method required to be implemented by an OnDateSetByUserListener interface
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // read the input and convert it to a String
        calendarDateSet.set(Calendar.YEAR, year);
        calendarDateSet.set(Calendar.MONTH, month);
        calendarDateSet.set(Calendar.DAY_OF_MONTH, day);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String selectedDate = formatter.format(calendarDateSet.getTime());

        // we use the callback interface to deliver the event to the parent activity
        mListener.onDateSetByUser(selectedDate);
    }
}
