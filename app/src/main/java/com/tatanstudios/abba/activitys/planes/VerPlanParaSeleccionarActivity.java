package com.tatanstudios.abba.activitys.planes;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.planes.AdaptadorBuscarNuevosPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class VerPlanParaSeleccionarActivity extends AppCompatActivity {

    private ImageView imgFlechaAtras;
    private ShapeableImageView imgPlan;
    private TextView txtTitulo, txtSubtitulo, txtToolbar;
    private Button btnComenzar;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ColorStateList colorStateTintGrey, colorStateTintWhite, colorStateTintBlack;

    private int colorBlanco, colorBlack = 0;

    private int idPlan = 0;

    private NestedScrollView nestedScrollView;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_plan_para_seleccionar);

        imgPlan = findViewById(R.id.imgPlan);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtSubtitulo = findViewById(R.id.txtSubtitulo);
        rootRelative = findViewById(R.id.rootRelative);
        btnComenzar = findViewById(R.id.botonComenzar);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        nestedScrollView = findViewById(R.id.nested);

        txtToolbar.setText(getString(R.string.informacion_plan));

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

        int colorGris = ContextCompat.getColor(this, R.color.colorGrisBtnDisable);
        colorBlanco = ContextCompat.getColor(this, R.color.white);
        colorBlack = ContextCompat.getColor(this, R.color.black);

        colorStateTintGrey = ColorStateList.valueOf(colorGris);
        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);

        btnComenzar.setBackgroundTintList(colorStateTintBlack);
        btnComenzar.setTextColor(colorBlanco);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idPlan = bundle.getInt("ID");

            // Ahora puedes utilizar el valor como desees
        }

        btnComenzar.setOnClickListener(v ->{
            apiSeleccionarPlan();
        });

        imgFlechaAtras.setOnClickListener(v ->{
            onBackPressedDispatcher.onBackPressed();
        });

        apiBuscarDatos();
    }

    private void apiBuscarDatos(){

        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();


        compositeDisposable.add(
                service.informacionPlanSeleccionado(idPlan, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {
                                            setearCampos(apiRespuesta);
                                        }
                                        else if(apiRespuesta.getSuccess() == 2){

                                            volverAtrasActualizado();
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


    private void apiSeleccionarPlan(){


    }


    private void setearCampos(ModeloPlanes api){

        if(api.getImagen() != null && !TextUtils.isEmpty(api.getImagen())){

            Glide.with(this)
                    .load(RetrofitBuilder.urlImagenes + api.getImagen())
                    .apply(opcionesGlide)
                    .into(imgPlan);
        }

        txtTitulo.setText(api.getTitulo());

        if(api.getSubtitulo() != null && !TextUtils.isEmpty(api.getSubtitulo())){
            txtSubtitulo.setText(api.getSubtitulo());
        }else{
            txtSubtitulo.setVisibility(View.GONE);
        }

        nestedScrollView.setVisibility(View.VISIBLE);
    }


    public boolean isDarkModeEnabled() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        boolean valor;

        switch (currentNightMode){
            case Configuration.UI_MODE_NIGHT_NO:
                valor = false;
                break;

            case Configuration.UI_MODE_NIGHT_YES:
                valor = true;
                break;
            default:
                valor = false;
                break;
        }

        return valor;
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

    private void volverAtras(){
        onBackPressedDispatcher.onBackPressed();
    }

    private void volverAtrasActualizado(){
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK, returnIntent);

        Toasty.success(this, getString(R.string.plan_agregado)).show();
        onBackPressedDispatcher.onBackPressed();
    }

}