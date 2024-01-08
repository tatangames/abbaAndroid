package com.tatanstudios.abba.adaptadores.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tatanstudios.abba.R;

public class AdaptadorSpinnerGenero extends ArrayAdapter<String> {

    private Context context;
    private boolean tipoTema;

    public AdaptadorSpinnerGenero(@NonNull Context context, int resource, @NonNull String[] objects, boolean tipoTema) {
        super(context, resource, objects);
        this.context = context;
        this.tipoTema = tipoTema;

        Toast.makeText(context, "huy: " + tipoTema, Toast.LENGTH_SHORT).show();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, position == 0);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent, false);
    }

    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent, boolean isFirstItem) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.spinner_item_layout, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position));

        if(tipoTema){ // dark
            // Color del texto para el primer elemento
            int textColor = ContextCompat.getColor(context, R.color.white);
            textView.setTextColor(textColor);

        }else{
            // Color del texto para el primer elemento
            int textColor = isFirstItem ? ContextCompat.getColor(context, R.color.colorTextoGris) : ContextCompat.getColor(context, R.color.black);
            textView.setTextColor(textColor);
        }



        return convertView;
    }
}