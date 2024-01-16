package com.tatanstudios.abba.fragmentos.planes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.modelos.planes.planesmodelo.ModeloVistasBuscarPlanes;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;

public class FragmentMisPlanes extends Fragment {


    private ProgressBar progressBar;

    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_mis_planes, container, false);

        return vista;
    }

}
