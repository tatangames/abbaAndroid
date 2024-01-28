package com.tatanstudios.abba.activitys.inicio;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.videos.VideoServidorActivity;
import com.tatanstudios.abba.adaptadores.inicio.todosvideos.AdaptadorTodosVideos;
import com.tatanstudios.abba.adaptadores.planes.completado.AdaptadorBloqueHorizontalVista;
import com.tatanstudios.abba.adaptadores.planes.completado.AdaptadorBloqueVerticalVista;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ListadoVideosActivity extends AppCompatActivity {



    private RecyclerView recyclerView;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private TextView txtToolbar;
    private ImageView imgFlechaAtras;

    private OnBackPressedDispatcher onBackPressedDispatcher;


    private AdaptadorTodosVideos adaptadorTodosVideos;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_videos);

        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);

        txtToolbar.setText(getString(R.string.videos));

        int colorProgress = ContextCompat.getColor(this, R.color.colorProgress);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        onBackPressedDispatcher = getOnBackPressedDispatcher();


        imgFlechaAtras.setOnClickListener(v -> {
            volverAtras();
        });


        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                volverAtras();
            }
        };

        onBackPressedDispatcher.addCallback(onBackPressedCallback);

        apiBuscarTodosLosVideos();
    }


    private void apiBuscarTodosLosVideos(){

        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();
        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.obtenerTodosLosVideos(iduser, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            adaptadorTodosVideos = new AdaptadorTodosVideos(getApplicationContext(), apiRespuesta.getModeloInicioVideos(), this);
                                            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                                            recyclerView.setAdapter(adaptadorTodosVideos);

                                        }else{

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


    private void volverAtras(){
        finish();
    }

    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(this, getString(R.string.error_intentar_de_nuevo)).show();
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



    public void redireccionamientoVideo(int tipoRedireccionamiento, String urlVideo) {

        if (tipoRedireccionamiento == 4) { // SERVIDOR PROPIO
            mostrarVideoServidor(urlVideo);
        } else {
            mostrarVideo(urlVideo); // Facebook, Instagram, Youtube
        }
    }



    private void mostrarVideo(String urlVideo){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlVideo));
        startActivity(Intent.createChooser(intent, getString(R.string.abrir_con)));
    }

    private void mostrarVideoServidor(String urlVideo) {
        Intent intent = new Intent(this, VideoServidorActivity.class);
        intent.putExtra("URL", urlVideo);
        startActivity(intent);
    }


}