package com.tatanstudios.abba.activitys.planes;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.planes.buscarplanes.AdaptadorMisPlanes;
import com.tatanstudios.abba.adaptadores.planes.buscarplanes.AdaptadorPlanesContenedor;
import com.tatanstudios.abba.adaptadores.planes.misplanes.AdaptadorPlanBloque;
import com.tatanstudios.abba.modelos.mas.ModeloFragmentMas;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloqueDetalle;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloques;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesContenedor;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesPortada;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesReHorizontal;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesReVertical;
import com.tatanstudios.abba.modelos.misplanes.ModeloVistasPlanesbloques;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.misplanes.ModeloVistasMisPlanes;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PlanesBloquesActivity extends AppCompatActivity {


    private int idPlan = 0;

    private RecyclerView recyclerView;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private TextView txtToolbar;
    private ImageView imgFlechaAtras;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private final int ID_INTENT_RETORNO_10 = 10;

    private ArrayList<ModeloVistasPlanesbloques> elementos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes_bloques);

        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);

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

        elementos = new ArrayList<>();

        compositeDisposable.add(
                service.informacionPlanBloque(iduser, idiomaPlan, idPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            elementos.add(new ModeloVistasPlanesbloques( ModeloVistasPlanesbloques.TIPO_PORTADA,
                                                    new ModeloMisPlanesPortada(apiRespuesta.getImagenPortada()),
                                                    null, null)
                                            );

                                            for (ModeloMisPlanesBloques m : apiRespuesta.getModeloMisPlanesBloques()){
                                                elementos.add(new ModeloVistasPlanesbloques( ModeloVistasPlanesbloques.TIPO_RECYCLER_BLOQUE_HORIZONTAL,
                                                        null,
                                                        new ModeloMisPlanesReHorizontal(m.getId(),
                                                                m.getVisible(),
                                                                m.getEsperar_fecha(),
                                                                m.getAbreviatura(),
                                                                m.getContador(),
                                                                m.getMismodia()),
                                                        null)
                                                );

                                                for (ModeloMisPlanesBloqueDetalle mdeta : m.getModeloMisPlanesBloqueDetalles()){

                                                    elementos.add(new ModeloVistasPlanesbloques( ModeloVistasPlanesbloques.TIPO_RECYCLER_BLOQUE_VERTICAL,
                                                            null,
                                                            null,
                                                            new ModeloMisPlanesReVertical(mdeta.getId(),
                                                                    mdeta.getCompletado(),
                                                                    mdeta.getTitulo()
                                                            ))
                                                    );
                                                }
                                            }

                                            setearAdapter();

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

        AdaptadorPlanBloque adapter = new AdaptadorPlanBloque(this, elementos, this);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

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