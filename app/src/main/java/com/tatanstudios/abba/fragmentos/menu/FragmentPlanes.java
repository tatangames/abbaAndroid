package com.tatanstudios.abba.fragmentos.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.planes.AdapterBotoneraPlanes;
import com.tatanstudios.abba.fragmentos.planes.FragmentBuscarPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloBotoneraPlanes;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FragmentPlanes extends Fragment {


    private RecyclerView recyclerView;

    private AdapterBotoneraPlanes adapterBotoneraPlanes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_planes, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);


        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapterBotoneraPlanes = new AdapterBotoneraPlanes();
        recyclerView.setAdapter(adapterBotoneraPlanes);

        modeloDatos();


        return vista;
    }

    private void modeloDatos(){

        List<ModeloBotoneraPlanes> modeloBotoneraPlanes = new ArrayList<>();
        modeloBotoneraPlanes.add(new ModeloBotoneraPlanes(1, "Mis planes"));
        modeloBotoneraPlanes.add(new ModeloBotoneraPlanes(2, "Buscar planes"));
        modeloBotoneraPlanes.add(new ModeloBotoneraPlanes(3, "Completados"));

        adapterBotoneraPlanes = new AdapterBotoneraPlanes(getContext(), modeloBotoneraPlanes, this);
        recyclerView.setAdapter(adapterBotoneraPlanes);
    }


    public void tipoPlan(int identificador){

        if(identificador == 2){ // NUEVOS PLANES
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new FragmentBuscarPlanes())
                    .addToBackStack(null)
                    .commit();
        }
    }

}
