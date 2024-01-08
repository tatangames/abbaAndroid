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
        //customizeDatePicker();
        //customizeDatePickerButtons(context);
    }

    private void customizeDatePicker() {
        // Personaliza los colores del DatePicker según tus necesidades
        DatePicker datePicker = getDatePicker();
        datePicker.setBackgroundColor(0xFFFFFFFF);  // Color de fondo blanco
        datePicker.setCalendarViewShown(false);  // Oculta la vista de calendario
        // Puedes seguir configurando otros colores según tus necesidades

    }


    private void customizeDatePickerButtons(Context context) {
        // Personaliza los colores de los botones Cancel y OK
        DatePicker datePicker = getDatePicker();

        // Personaliza el fondo y el color del texto de los días
        int dayId = context.getResources().getIdentifier("android:id/day", null, null);
        TextView dayView = datePicker.findViewById(dayId);

        if (dayView != null) {
            // Personaliza el fondo del día
            dayView.setBackgroundResource(R.drawable.custom_day_background); // Reemplaza con tu propio drawable

            // Personaliza el color del texto del día
            dayView.setTextColor(ContextCompat.getColor(context, R.color.custom_day_background_color)); // Reemplaza con tu propio color
        }
    }
}
