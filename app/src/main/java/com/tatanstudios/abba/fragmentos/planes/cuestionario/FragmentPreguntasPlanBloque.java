package com.tatanstudios.abba.fragmentos.planes.cuestionario;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tatanstudios.abba.R;

public class FragmentPreguntasPlanBloque extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_preguntas_plan_bloque, container, false);

        return vista;
    }



}
