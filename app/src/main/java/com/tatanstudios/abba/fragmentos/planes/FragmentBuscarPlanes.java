package com.tatanstudios.abba.fragmentos.planes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tatanstudios.abba.R;

public class FragmentBuscarPlanes extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_buscar_planes, container, false);

        return vista;
    }
}
