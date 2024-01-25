package com.tatanstudios.abba.fragmentos.inicio.tabs;

import static android.content.Context.MODE_PRIVATE;

import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayer.Builder;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.modelos.inicio.ModeloVistasInicio;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesTitulo;
import com.tatanstudios.abba.modelos.planes.planesmodelo.ModeloVistasBuscarPlanes;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentTabInicio extends Fragment {


    private ProgressBar progressBar;

    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ArrayList<ModeloVistasInicio> elementos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_tab_inicio, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        rootRelative = vista.findViewById(R.id.rootRelative);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        apiBuscarDatos();

        return vista;
    }


    private void apiBuscarDatos(){


        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        elementos = new ArrayList<>();

        compositeDisposable.add(
                service.buscarPlanesNuevos(iduser, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getHayinfo() == 1){

                                               /* for (ModeloPlanesTitulo arrayTitulo : apiRespuesta.getModeloPlanesTitulos()) {
                                                    elementos.add(new ModeloVistasBuscarPlanes( ModeloVistasBuscarPlanes.TIPO_TITULO, new ModeloPlanesTitulo(arrayTitulo.getId(), arrayTitulo.getTitulo()), null));

                                                    for (ModeloPlanes arrayPlanes : arrayTitulo.getModeloPlanes()){

                                                        elementos.add(new ModeloVistasBuscarPlanes( ModeloVistasBuscarPlanes.TIPO_PLANES, null, new ModeloPlanes(
                                                                arrayPlanes.getId(),
                                                                arrayPlanes.getImagen(),
                                                                arrayPlanes.getTitulo(),
                                                                arrayPlanes.getSubtitulo()
                                                        )));
                                                    }
                                                }

                                                completarAdapter();
*/

                                            }else{
                                                recyclerView.setVisibility(View.GONE);
                                              //  txtSinPlanes.setVisibility(View.VISIBLE);
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
