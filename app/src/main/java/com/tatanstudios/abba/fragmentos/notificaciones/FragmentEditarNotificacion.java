package com.tatanstudios.abba.fragmentos.notificaciones;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

public class FragmentEditarNotificacion extends Fragment {

    private SwitchCompat switchCompat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_editar_notificaciones, container, false);

        switchCompat = vista.findViewById(R.id.idnoti);

        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {




        });


        return vista;
    }


}
