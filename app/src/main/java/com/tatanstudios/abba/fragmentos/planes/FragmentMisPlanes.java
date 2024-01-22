package com.tatanstudios.abba.fragmentos.planes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.planes.PlanesBloquesActivity;
import com.tatanstudios.abba.activitys.planes.PlanesContenedorActivity;
import com.tatanstudios.abba.adaptadores.planes.buscarplanes.AdaptadorBuscarNuevosPlanes;
import com.tatanstudios.abba.adaptadores.planes.buscarplanes.AdaptadorMisPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesTitulo;
import com.tatanstudios.abba.modelos.planes.misplanes.ModeloVistasMisPlanes;
import com.tatanstudios.abba.modelos.planes.planesmodelo.ModeloVistasBuscarPlanes;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentMisPlanes extends Fragment {


    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ArrayList<ModeloVistasMisPlanes> elementos;

    private TextView txtSinPlanes;

    private final int ID_INTENT_RETORNO_10 = 10;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_mis_planes, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        rootRelative = vista.findViewById(R.id.rootRelative);
        txtSinPlanes = vista.findViewById(R.id.txtSinPlanes);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        apiBuscarMisPlanes();

        return vista;
    }


    private void apiBuscarMisPlanes(){

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        elementos = new ArrayList<>();

        compositeDisposable.add(
                service.listadoMisPlanes(iduser, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {


                                            if(apiRespuesta.getHayinfo() == 1){
                                                if(apiRespuesta.getHaycontinuar() == 1){

                                                    for (ModeloPlanes arrayPlanes : apiRespuesta.getModeloPlanesContinuar()) {
                                                        elementos.add(new ModeloVistasMisPlanes( ModeloVistasMisPlanes.TIPO_CONTINUAR, new ModeloPlanes(
                                                                arrayPlanes.getId_Planes(),
                                                                arrayPlanes.getImagenportada(),
                                                                arrayPlanes.getTitulo(),
                                                                arrayPlanes.getSubtitulo())
                                                        ));
                                                    }
                                                }

                                                for (ModeloPlanes arrayPlanes : apiRespuesta.getModeloPlanes()) {
                                                    elementos.add(new ModeloVistasMisPlanes( ModeloVistasMisPlanes.TIPO_PLANES, new ModeloPlanes(
                                                            arrayPlanes.getId_Planes(),
                                                            arrayPlanes.getImagen(),
                                                            arrayPlanes.getTitulo(),
                                                            arrayPlanes.getSubtitulo())
                                                    ));
                                                }

                                                setearAdapter();

                                            }else{
                                                recyclerView.setVisibility(View.GONE);
                                                txtSinPlanes.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        else{
                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                })
        );
    }


    private void setearAdapter(){
        AdaptadorMisPlanes adapter = new AdaptadorMisPlanes(getContext(), elementos, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public void verBloquePlanes(int id){

        Intent intent = new Intent(getActivity(), PlanesBloquesActivity.class);
        intent.putExtra("ID", id);
        someActivityResultLauncher.launch(intent);
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                // DE ACTIVITY PlanesBloquesActivity.class
                //
                if(result.getResultCode() == ID_INTENT_RETORNO_10){

                    progressBar.setVisibility(View.VISIBLE);
                    apiBuscarMisPlanes();
                }

            });




    private void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(getActivity(), getString(R.string.error_intentar_de_nuevo)).show();
    }

    @Override
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if(compositeDisposable != null){
            compositeDisposable.clear();
        }
        super.onStop();
    }

}
