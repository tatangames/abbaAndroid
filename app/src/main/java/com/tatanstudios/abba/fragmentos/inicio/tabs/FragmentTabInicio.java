package com.tatanstudios.abba.fragmentos.inicio.tabs;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.inicio.CuestionarioInicioActivity;
import com.tatanstudios.abba.activitys.inicio.InformacionPlanVistaActivity;
import com.tatanstudios.abba.activitys.videos.VideoServidorActivity;
import com.tatanstudios.abba.adaptadores.inicio.AdaptadorInicio;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorPreguntas;
import com.tatanstudios.abba.modelos.inicio.ModeloContenedorInicio;
import com.tatanstudios.abba.modelos.inicio.ModeloVistasInicio;
import com.tatanstudios.abba.modelos.inicio.bloques.comparteapp.ModeloInicioComparteApp;
import com.tatanstudios.abba.modelos.inicio.bloques.imagenes.ModeloInicioImagenes;
import com.tatanstudios.abba.modelos.inicio.bloques.insignias.ModeloInicioInsignias;
import com.tatanstudios.abba.modelos.inicio.bloques.separador.ModeloInicioSeparador;
import com.tatanstudios.abba.modelos.inicio.bloques.versiculos.ModeloInicioDevocional;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;
import com.tatanstudios.abba.modelos.mas.ModeloFraMasConfig;
import com.tatanstudios.abba.modelos.mas.ModeloFragmentMas;
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

    private AdaptadorInicio adapter;

    private ModeloInicioSeparador modeloInicioSeparador;



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
                service.informacionBloqueInicio(iduser, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {
                                            llenarBloques(apiRespuesta);
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

    private void llenarBloques(ModeloContenedorInicio apiRespuesta){

        modeloInicioSeparador = new ModeloInicioSeparador(
                apiRespuesta.getVideomayor5(),
                apiRespuesta.getImagenesmayor5(),
                apiRespuesta.getInsigniasmayor5()
        );

        if(apiRespuesta.getMostrarbloquedevocional() == 1 && apiRespuesta.getDevohaydevocional() == 1){

            elementos.add(new ModeloVistasInicio( ModeloVistasInicio.TIPO_DEVOCIONAL,
                    new ModeloInicioDevocional(apiRespuesta.getDevohaydevocional(),
                            apiRespuesta.getDevocuestionario(),
                            apiRespuesta.getDevoidblockdeta()),
                    null,
                    null,
                    null,
                    null

            ));
        }

        // BLOQUE DE POSICION 2 - Videos

        if(apiRespuesta.getMostrarbloquevideo() == 1 && apiRespuesta.getVideohayvideos() == 1){

            elementos.add(new ModeloVistasInicio( ModeloVistasInicio.TIPO_VIDEOS,null,
                    apiRespuesta.getModeloInicioVideos(),
                    null,
                    null,
                    null
            ));
        }

        // BLOQUE DE POSICION 3 - Imagenes

        if(apiRespuesta.getMostrarbloqueimagenes() == 1 && apiRespuesta.getImageneshayhoy() == 1){
            elementos.add(new ModeloVistasInicio( ModeloVistasInicio.TIPO_IMAGENES,null,
                    null,
                    apiRespuesta.getModeloInicioImagenes(),
                    null,
                    null
            ));
        }



        // BLOQUE DE POSICION 4 - Comparte App

        if(apiRespuesta.getMostrarbloquecomparte() == 1){
            elementos.add(new ModeloVistasInicio( ModeloVistasInicio.TIPO_COMPARTEAPP,null,
                    null,
                    null,
                    new ModeloInicioComparteApp(apiRespuesta.getComparteappimagen(),
                            apiRespuesta.getComparteapptitulo(),
                            apiRespuesta.getComparteappdescrip()),
                    null
            ));
        }




        // BLOQUE DE POSICION 5 - Insignias


        if(apiRespuesta.getMostrarbloqueinsignias() == 1 && apiRespuesta.getInsigniashay() == 1){
            elementos.add(new ModeloVistasInicio( ModeloVistasInicio.TIPO_INSIGNIAS,null,
                    null,
                    null,
                    null,
                    apiRespuesta.getModeloInicioInsignias()
            ));
        }


        setearAdaptador();
    }

    private void setearAdaptador(){

        boolean temaActual = false;
        if(tokenManager.getToken().getTema() == 1){
            temaActual = true;
        }

        adapter = new AdaptadorInicio(getContext(), elementos, this, temaActual, modeloInicioSeparador);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }



    public void redireccionamientoVideo(int tipoRedireccionamiento, String urlVideo) {

        if (tipoRedireccionamiento == 4) { // SERVIDOR PROPIO
            mostrarVideoServidor(urlVideo);
        } else {
            mostrarVideo(urlVideo); // Facebook, Instagram, Youtube
        }
    }


    public void redireccionarCuestionario(int idblockdeta){
        Intent intent = new Intent(getContext(), CuestionarioInicioActivity.class);
        intent.putExtra("IDBLOCKDETA", idblockdeta);
        startActivity(intent);
    }


    public void redireccionarInfoPlanVista(int idblockdeta){
        Intent intent = new Intent(getContext(), InformacionPlanVistaActivity.class);
        intent.putExtra("IDBLOCKDETA", idblockdeta);
        startActivity(intent);
    }


    public void compartirAplicacion(){

        String packageName = getContext().getPackageName();
        String playStoreLink = "https://play.google.com/store/apps/details?id=" + packageName;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.compartir) + playStoreLink);

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.compartir)));
        } catch (Exception e) {

        }

    }



    private void mostrarVideo(String urlVideo){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo));
        startActivity(Intent.createChooser(intent, getString(R.string.abrir_con)));
    }

    private void mostrarVideoServidor(String urlVideo) {
        Intent intent = new Intent(getContext(), VideoServidorActivity.class);
        intent.putExtra("URL", urlVideo);
        startActivity(intent);
    }

    public void vistaTodosLosVideos(){

    }

    public void vistaTodosLasImagenes(){


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
