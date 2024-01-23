package com.tatanstudios.abba.fragmentos.inicio.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tatanstudios.abba.R;

public class FragmentTabInicio extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_tab_inicio, container, false);




        return vista;
    }

}
