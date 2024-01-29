package com.tatanstudios.abba.activitys.inicio;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.inicio.insignias.AdaptadorInsigniaHitos;
import com.tatanstudios.abba.adaptadores.inicio.todosvideos.AdaptadorTodosVideos;
import com.tatanstudios.abba.modelos.inicio.ModeloVistasInicio;
import com.tatanstudios.abba.modelos.inicio.bloques.versiculos.ModeloInicioDevocional;
import com.tatanstudios.abba.modelos.insignias.ModeloContenedorInsignias;
import com.tatanstudios.abba.modelos.insignias.ModeloDescripcionHitos;
import com.tatanstudios.abba.modelos.insignias.ModeloInsigniaHitos;
import com.tatanstudios.abba.modelos.insignias.ModeloVistaHitos;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class InformacionInsigniaActivity extends AppCompatActivity {


    private int idTipoInsignia = 0;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private TextView txtToolbar;
    private ImageView imgFlechaAtras;

    private OnBackPressedDispatcher onBackPressedDispatcher;
    private RecyclerView recyclerView;

    private boolean tema;


    private AdaptadorInsigniaHitos adaptadorInsigniaHitos;

    private ArrayList<ModeloVistaHitos> elementos;


    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_insignia);

        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idTipoInsignia = bundle.getInt("IDINSIGNIA");
        }

        txtToolbar.setText(getString(R.string.planes));

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

        if(tokenManager.getToken().getTema() == 1){
            tema = true;
        }

        imgFlechaAtras.setOnClickListener(v -> {
            volverAtras();
        });

        elementos = new ArrayList<>();


        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                volverAtras();
            }
        };

        onBackPressedDispatcher.addCallback(onBackPressedCallback);

        apiBuscarInfoInsignia();
    }


    private void apiBuscarInfoInsignia(){

        String iduser = tokenManager.getToken().getId();
        int idioma = tokenManager.getToken().getIdiomaTextos();


        // NO TENDRA RETRY
        compositeDisposable.add(
                service.informacionInsigniaSeleccionada(iduser, idioma, idTipoInsignia)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if (apiRespuesta.getSuccess() == 1) {

                                            setearCampos(apiRespuesta);

                                        }else{
                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    String da = throwable.toString();
                                    Log.i("PORTADA", "error: " + da);
                                    mensajeSinConexion();
                                })
        );
    }


    private void setearCampos(ModeloContenedorInsignias apiRespuesta){

        elementos.add(new ModeloVistaHitos( ModeloVistaHitos.TIPO_IMAGEN,
                new ModeloDescripcionHitos(apiRespuesta.getImagen(), apiRespuesta.getTitulo(),
                        apiRespuesta.getDescripcion(), apiRespuesta.getNivelvoy()),
                null
        ));


        List<ModeloInsigniaHitos> mm = new ArrayList<>();


        // verificar si hay siguiente nivel
        if(apiRespuesta.getHitoHayNextLevel() == 1){
            mm.add(new ModeloInsigniaHitos("", 1, apiRespuesta.getCualNextLevel(),0, apiRespuesta.getHitoCuantoFalta(),
                    ""
                    ));
        }


        for (ModeloInsigniaHitos m : apiRespuesta.getModeloInsigniaHitos()){
            mm.add(new ModeloInsigniaHitos(m.getFechaFormat(), 0, 0,m.getNivel(), 0,
                    m.getTextoCompletado()));
        }


        elementos.add(new ModeloVistaHitos( ModeloVistaHitos.TIPO_RECYCLER,
                null,
                mm
        ));

        // llenar adaptador de los hitos, y le estoy pasando textoFalta o remaining
        adaptadorInsigniaHitos = new AdaptadorInsigniaHitos(getApplicationContext(), elementos, apiRespuesta.getTextofalta());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adaptadorInsigniaHitos);
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
}