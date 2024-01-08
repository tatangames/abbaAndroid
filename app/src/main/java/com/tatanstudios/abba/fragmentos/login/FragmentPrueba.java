package com.tatanstudios.abba.fragmentos.login;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;

import java.lang.reflect.Field;

public class FragmentPrueba extends Fragment {


    TextInputLayout tilInput;
    TextInputEditText edt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_prueba, container, false);

        tilInput = vista.findViewById(R.id.inputNombre);
        edt = vista.findViewById(R.id.edtNombre);




        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto está a punto de cambiar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto ha cambiado
            }

            @Override
            public void afterTextChanged(Editable editable) {

                String textoIngresado = editable.toString();
                // Hacer algo con el texto ingresado
                if (TextUtils.isEmpty(textoIngresado)) {

                    tilInput.setError("Nombre es requerido");
                }else{
                    tilInput.setError(null);
                }

            }
        });


        //tilInput.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.white));
       // tilInput.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);


        return vista;
    }



}
