package com.tatanstudios.abba.adaptadores.spinner.cuestionario;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.tatanstudios.abba.R;

public class AdaptadorSpinnerTipoLetra extends ArrayAdapter<String> {

    private Context context;
    private boolean tipoTema;


    public AdaptadorSpinnerTipoLetra(Context context, int resource, int tipoTema) {
        super(context, resource);
        this.context = context;

        if(tipoTema == 1){ // dark
            this.tipoTema = true;
        }else{
            this.tipoTema = false;
        }

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
            convertView = inflater.inflate(R.layout.spinner_item_tipo_letra, parent, false);
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


        // TIPOS DE LETRA

        switch (position){
            case 0:
                // AKTIV GROTESK
                final Typeface typeface1 = ResourcesCompat.getFont(context, R.font.aktivgro_medium);
                textView.setTypeface(typeface1);
                break;
            case 1:
                // ROBOTO SERIF
                final Typeface typeface2 = ResourcesCompat.getFont(context, R.font.roboto_serif);
                textView.setTypeface(typeface2);
                break;
            case 2:
                // ROBOTO SANS
                final Typeface typeface3 = ResourcesCompat.getFont(context, R.font.roboto_sans);
                textView.setTypeface(typeface3);
                break;
            default:
                final Typeface typeface4 = ResourcesCompat.getFont(context, R.font.roboto_sans);
                textView.setTypeface(typeface4);
                break;
        }

        return convertView;
    }
}