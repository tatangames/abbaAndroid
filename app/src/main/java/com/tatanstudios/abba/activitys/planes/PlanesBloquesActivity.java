package com.tatanstudios.abba.activitys.planes;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.tatanstudios.abba.activitys.planes.cuestionario.CuestionarioPlanActivity;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorBloqueHorizontal;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorBloqueVertical;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloqueDetalle;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PlanesBloquesActivity extends AppCompatActivity {


    private int idPlan = 0;

    private RecyclerView recyclerViewHorizontal, recyclerViewVertical;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private TextView txtToolbar;
    private ImageView imgFlechaAtras, imgPortada;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private final int ID_INTENT_RETORNO_10 = 10;
    private final int ID_INTENT_RETORNO_11 = 11;
    private AdaptadorBloqueHorizontal adapterHorizontal;
    private AdaptadorBloqueVertical adapterVertical;

    int tema = 0;

    private boolean puedeActualizarCheck;

    private LinearLayout linearContenedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes_bloques);

        recyclerViewHorizontal = findViewById(R.id.recyclerViewHorizontal);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);
        recyclerViewVertical = findViewById(R.id.recyclerViewVertical);
        imgPortada = findViewById(R.id.imgPortada);
        linearContenedor = findViewById(R.id.linearContenedor);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idPlan = bundle.getInt("ID");
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

        tema = tokenManager.getToken().getTema();
        puedeActualizarCheck = true;

        imgFlechaAtras.setOnClickListener(v -> {
            volverAtrasActualizar();
        });

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent returnIntent = new Intent();
                setResult(ID_INTENT_RETORNO_10, returnIntent);
                finish();
            }
        };

        onBackPressedDispatcher.addCallback(onBackPressedCallback);

        apiBuscarPlanesbloques();
    }



    private void apiBuscarPlanesbloques(){

        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();
        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.informacionPlanBloque(iduser, idiomaPlan, idPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            int hayDiaActual = apiRespuesta.getHayDiaActual();
                                            int idUltimoBloque = apiRespuesta.getIdUltimoBloque();

                                            adapterHorizontal = new AdaptadorBloqueHorizontal(this, apiRespuesta.getModeloMisPlanesBloques(), recyclerViewHorizontal, tema,
                                                    hayDiaActual, idUltimoBloque, this);
                                            recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                                            recyclerViewHorizontal.setAdapter(adapterHorizontal);

                                            setearAdapter(apiRespuesta.getImagenPortada());

                                            linearContenedor.setVisibility(View.VISIBLE);
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



    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);


    private void setearAdapter(String urlPortada){

        if(urlPortada != null && !TextUtils.isEmpty(urlPortada)){
            Glide.with(this)
                    .load(RetrofitBuilder.urlImagenes + urlPortada)
                    .apply(opcionesGlide)
                    .into(imgPortada);
        }else{
            int resourceId = R.drawable.camaradefecto;
            Glide.with(this)
                    .load(resourceId)
                    .apply(opcionesGlide)
                    .into(imgPortada);
        }
    }



    public void llenarDatosAdapterVertical(List<
            ModeloMisPlanesBloqueDetalle> modeloMisPlanesBloqueDetalles){

        adapterVertical = new AdaptadorBloqueVertical(this, modeloMisPlanesBloqueDetalles, this, tema);
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewVertical.setAdapter(adapterVertical);
    }



    public void actualizarCheck(int blockDeta, int valor){

        if(puedeActualizarCheck){
            puedeActualizarCheck = false;

            String iduser = tokenManager.getToken().getId();

            // NO TENDRA RETRY
            compositeDisposable.add(
                    service.actualizarPlanBloqueDetalle(iduser, blockDeta, valor)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(apiRespuesta -> {

                                        puedeActualizarCheck = true;

                                    },
                                    throwable -> {
                                        puedeActualizarCheck = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }


    public void redireccionarCuestionario(int idBlockDeta, int tienePreguntas){

        Intent intent = new Intent(this, CuestionarioPlanActivity.class);
        intent.putExtra("IDBLOQUE", idBlockDeta);
        intent.putExtra("PREGUNTAS", tienePreguntas);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                // DE ACTIVITY CuestionarioPlanActivity
                //
                if(result.getResultCode() == ID_INTENT_RETORNO_11){

                }

            });


    private void volverAtrasActualizar(){
        Intent returnIntent = new Intent();
        setResult(ID_INTENT_RETORNO_10, returnIntent);
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