package com.tatanstudios.abba.fragmentos.login;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.tatanstudios.abba.R;

public class FragmentOlvidePassword extends Fragment {


    private ImageView imgFlechaAtras;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_olvide_password, container, false);

        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);

        imgFlechaAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        return vista;
    }

}
