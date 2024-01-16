package com.tatanstudios.abba.activitys.planes;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.mas.AdaptadorFragmentMas;
import com.tatanstudios.abba.adaptadores.planes.AdaptadorPlanesContenedor;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;
import androidx.activity.OnBackPressedDispatcher;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class PlanesContenedorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;



    private ProgressBar progressBar;

    private TextView txtToolbar, txtSinPlanes;
    private ImageView imgFlechaAtras;

    private int idcontenedor = 0;

    private AdaptadorPlanesContenedor adapter;

    private final int ID_INTENT_RETORNO_10 = 10;
    private final int ID_INTENT_RETORNO_11 = 11;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planes_contenedor);

        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        txtSinPlanes = findViewById(R.id.txtSinPlanes);
        rootRelative = findViewById(R.id.rootRelative);

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

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idcontenedor = bundle.getInt("ID");
        }

        imgFlechaAtras.setOnClickListener(v -> {
            volverAtrasActualizar();
        });

        adapter = new AdaptadorPlanesContenedor();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent returnIntent = new Intent();
                setResult(ID_INTENT_RETORNO_11, returnIntent);
                finish();
            }
        };

        onBackPressedDispatcher.addCallback(onBackPressedCallback);
        apiBuscarPlanes();
    }


    private void apiBuscarPlanes(){

        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();
        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.listadoPlanesContenedor(idiomaPlan, iduser, idcontenedor)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            if(apiRespuesta.getHayinfo() == 1){
                                                adapter = new AdaptadorPlanesContenedor(this, apiRespuesta.getModeloPlanes(), this);
                                                recyclerView.setAdapter(adapter);
                                                recyclerView.setVisibility(View.VISIBLE);
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


    public void informacionPlan(int idplan){
        Intent intent = new Intent(this, VerPlanParaSeleccionarActivity.class);
        intent.putExtra("ID", idplan);
        someActivityResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {

                // DE ACTIVITY VerPlanParaSeleccionarActivity.class
                if(result.getResultCode() == ID_INTENT_RETORNO_10){
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    apiBuscarPlanes();
                }
            });


    private void volverAtrasActualizar(){
        Intent returnIntent = new Intent();
        setResult(ID_INTENT_RETORNO_11, returnIntent);
        finish();
    }



    /*@Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(ID_INTENT_RETORNO_11, returnIntent);
        super.onBackPressed();
    }*/




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