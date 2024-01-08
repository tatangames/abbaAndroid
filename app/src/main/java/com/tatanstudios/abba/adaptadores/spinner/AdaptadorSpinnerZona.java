package com.tatanstudios.abba.adaptadores.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tatanstudios.abba.R;

public class AdaptadorSpinnerZona extends ArrayAdapter<String> {


    private Context context;
    private boolean tipoTema;

    public AdaptadorSpinnerZona(@NonNull Context context, int resource, boolean tipoTema) {
        super(context, resource);
        this.context = context;
        this.tipoTema = tipoTema;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createView(position, convertView, parent);
    }

    private View createView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
            int textColor = ContextCompat.getColor(context, R.color.black);
            textView.setTextColor(textColor);
        }


        return convertView;
    }
}