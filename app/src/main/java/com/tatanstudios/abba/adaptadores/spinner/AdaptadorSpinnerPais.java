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

public class AdaptadorSpinnerPais extends ArrayAdapter<String> {


    private Context context;

    public AdaptadorSpinnerPais(@NonNull Context context, int resource, @NonNull String[] objects) {
        super(context, resource, objects);
        this.context = context;
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
            convertView = inflater.inflate(R.layout.spinner_item_img_layout, parent, false);
        }


        ImageView imgBandera = convertView.findViewById(R.id.imageView);

        switch (position) {
            case 0:
                imgBandera.setVisibility(View.GONE);
                break;
            case 1:
                imgBandera.setImageResource(R.drawable.flag_elsalvador);
                break;
            case 2:
                imgBandera.setImageResource(R.drawable.flag_guatemala);
                break;
            default:
                imgBandera.setVisibility(View.INVISIBLE);
                break;
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(getItem(position));

        // Color del texto para el primer elemento
        int textColor = isFirstItem ? ContextCompat.getColor(context, R.color.colorTextoGris) : ContextCompat.getColor(context, R.color.black);
        textView.setTextColor(textColor);

        return convertView;
    }
}