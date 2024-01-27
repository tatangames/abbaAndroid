package com.tatanstudios.abba.activitys.inicio;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.NestedScrollView;

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
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class InformacionPlanVistaActivity extends AppCompatActivity {

    private ImageView imgFlechaAtras, imgPlan;
    private TextView txtTitulo, txtSubtitulo, txtToolbar, txtDescripcion;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;


    // el id plan se busca en el servidor
    private int idBlockDeta = 0;

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
        setContentView(R.layout.activity_informacion_plan_vista);

        imgPlan = findViewById(R.id.imgPlan);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtSubtitulo = findViewById(R.id.txtSubtitulo);
        rootRelative = findViewById(R.id.rootRelative);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        nestedScrollView = findViewById(R.id.nested);
        txtDescripcion = findViewById(R.id.txtDescripcion);

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


        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idBlockDeta = bundle.getInt("IDBLOCKDETA");
        }


        imgFlechaAtras.setOnClickListener(v ->{
            onBackPressedDispatcher.onBackPressed();
        });

        apiBuscarDatos();
    }

    private void apiBuscarDatos(){

        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.informacionPlanSeleccionado(idBlockDeta, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {
                                            setearCampos(apiRespuesta);
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

        if(api.getDescripcion() != null && !TextUtils.isEmpty(api.getDescripcion())){
            txtDescripcion.setText(HtmlCompat.fromHtml(api.getDescripcion(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            txtDescripcion.setVisibility(View.VISIBLE);
        }

        nestedScrollView.setVisibility(View.VISIBLE);
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