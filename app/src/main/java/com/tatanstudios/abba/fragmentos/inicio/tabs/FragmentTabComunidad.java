package com.tatanstudios.abba.fragmentos.inicio.tabs;

import static android.content.Context.MODE_PRIVATE;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.inicio.AdaptadorInicio;
import com.tatanstudios.abba.modelos.comunidad.ModeloVistaComunidad;
import com.tatanstudios.abba.modelos.inicio.ModeloVistasInicio;
import com.tatanstudios.abba.modelos.inicio.bloques.separador.ModeloInicioSeparador;
import com.tatanstudios.abba.modelos.insignias.ModeloContenedorInsignias;
import com.tatanstudios.abba.modelos.insignias.ModeloDescripcionHitos;
import com.tatanstudios.abba.modelos.insignias.ModeloVistaHitos;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentTabComunidad extends Fragment {


    private ProgressBar progressBar;

    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private AdaptadorInicio adapter;

    private ModeloInicioSeparador modeloInicioSeparador;

    private boolean bottomSheetImagen = false;

    int colorProgress = 0;

    private ColorStateList colorStateTintGrey, colorStateTintWhite, colorStateTintBlack;

    private int colorBlanco = 0;
    private int colorBlack = 0;

    private boolean temaActual = false;


    private ArrayList<ModeloVistaComunidad> elementos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_tab_comunidad, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        rootRelative = vista.findViewById(R.id.rootRelative);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        if(tokenManager.getToken().getTema() == 1){
            temaActual = true;
        }

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        elementos = new ArrayList<>();

        colorBlanco = ContextCompat.getColor(requireContext(), R.color.white);
        colorBlack = ContextCompat.getColor(requireContext(), R.color.black);
        int colorGris = ContextCompat.getColor(requireContext(), R.color.colorGrisBtnDisable);

        colorStateTintGrey = ColorStateList.valueOf(colorGris);
        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);

        apiSolicutesAceptadas();

        return vista;
    }


    private void apiSolicutesAceptadas(){

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        elementos = new ArrayList<>();

        compositeDisposable.add(
                service.listadoComunidadAceptado(iduser, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {



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


    private void setearCampos(ModeloContenedorInsignias apiRespuesta){

        // agregar la botonera



    }

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
