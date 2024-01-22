package com.tatanstudios.abba.fragmentos.menu;

import static android.content.Context.MODE_PRIVATE;

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
import com.tatanstudios.abba.fragmentos.planes.FragmentMisPlanes;
import com.tatanstudios.abba.fragmentos.planes.FragmentPlanesCompletados;
import com.tatanstudios.abba.modelos.planes.ModeloBotoneraPlanes;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class FragmentPlanes extends Fragment {


    private RecyclerView recyclerView;
    private AdapterBotoneraPlanes adapterBotoneraPlanes;

    private TokenManager tokenManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_planes, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

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

        int tema = tokenManager.getToken().getTema();

        List<ModeloBotoneraPlanes> modeloBotoneraPlanes = new ArrayList<>();
        modeloBotoneraPlanes.add(new ModeloBotoneraPlanes(1, "Mis planes"));
        modeloBotoneraPlanes.add(new ModeloBotoneraPlanes(2, "Buscar planes"));
        modeloBotoneraPlanes.add(new ModeloBotoneraPlanes(3, "Completados"));

        adapterBotoneraPlanes = new AdapterBotoneraPlanes(getContext(), modeloBotoneraPlanes, this, tema);
        recyclerView.setAdapter(adapterBotoneraPlanes);


        // automaticamente cargar el Fragment Mis Planes

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new FragmentMisPlanes())
                .addToBackStack(null)
                .commit();
    }


    public void tipoPlan(int identificador){


        if(identificador == 1) { // MIS PLANES
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new FragmentMisPlanes())
                    .addToBackStack(null)
                    .commit();
        }
        else if(identificador == 2){ // NUEVOS PLANES
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new FragmentBuscarPlanes())
                    .addToBackStack(null)
                    .commit();
        }
        else if(identificador == 3){ // PLANES COMPLETADOS
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new FragmentPlanesCompletados())
                    .addToBackStack(null)
                    .commit();
        }
    }

}
