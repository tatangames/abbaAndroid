package com.tatanstudios.abba.extras;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.tatanstudios.abba.R;

public class CustomDatePickerDialog extends DatePickerDialog {

    public CustomDatePickerDialog(Context context, OnDateSetListener listener, int year, int month, int dayOfMonth) {
        super(context, listener, year, month, dayOfMonth);
    }

}
