package com.tatanstudios.abba.activitys.planes;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorBloqueHorizontal;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorPlanBloque;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorVertical;
import com.tatanstudios.abba.adaptadores.planes.buscarplanes.AdaptadorBuscarNuevosPlanes;
import com.tatanstudios.abba.adaptadores.planes.buscarplanes.AdaptadorPlanesContenedor;
import com.tatanstudios.abba.fragmentos.planes.bloques.ItemModel;
import com.tatanstudios.abba.fragmentos.planes.bloques.SubItemModel;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloques;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesContenedor;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesPortada;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesReHorizontal;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesReVertical;
import com.tatanstudios.abba.modelos.misplanes.ModeloVistasPlanesbloques;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;
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

    private AdaptadorBloqueHorizontal adapterHorizontal;


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
        int tema = tokenManager.getToken().getTema();

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










        /*adapter = new AdaptadorPlanBloque(this, new ArrayList<>());

        // Configura el RecyclerView con el adaptador
        recyclerViewHorizontal.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHorizontal.setAdapter(adapter);

        List<ItemModel> datos = obtenerDatosDeEjemplo();

        adapter.actualizarDatos(datos);


        setearAdapterVertical();*/
    }

    private void setearAdapterVertical(){

        /*adapterVertical = new AdaptadorVertical(obtenerItemsVertical());
        recyclerViewVertical.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewVertical.setAdapter(adapterVertical);*/
    }

    public void calcular(){
        adapterHorizontal.notifyDataSetChanged();
    }


    // Método de ejemplo para obtener datos
   /* private List<ItemModel> obtenerDatosDeEjemplo() {
        // Retorna una lista de ItemModel con datos de ejemplo
        // Puedes personalizar esto según tus necesidades reales
        // Aquí estoy creando un ItemModel de tipo imagen y otro de tipo RecyclerView
        List<ItemModel> datos = new ArrayList<>();
        datos.add(new ItemModel(ItemModel.TIPO_IMAGEN,  R.drawable.edificios, null));
        datos.add(new ItemModel(ItemModel.TIPO_RECYCLER, 0, obtenerSubItemsDeEjemplo()));
        return datos;
    }*/

    // Método de ejemplo para obtener subítems
    /*private List<SubItemModel> obtenerSubItemsDeEjemplo() {
        // Retorna una lista de SubItemModel con datos de ejemplo
        // Puedes personalizar esto según tus necesidades reales
        List<SubItemModel> subItems = new ArrayList<>();
        subItems.add(new SubItemModel("Subítem 1"));
        subItems.add(new SubItemModel("Subítem 2"));
        // Agrega más subítems según sea necesario
        return subItems;
    }*/



   /* private List<ModeloMisPlanesReVertical> obtenerItemsVertical() {

        List<ModeloMisPlanesReVertical> subItems = new ArrayList<>();
        subItems.add(new ModeloMisPlanesReVertical(1, 0, "Items 1"));
        subItems.add(new ModeloMisPlanesReVertical(2, 0, "Items 2"));
        subItems.add(new ModeloMisPlanesReVertical(1, 0, "Items 1"));
        subItems.add(new ModeloMisPlanesReVertical(2, 0, "Items 2"));
        subItems.add(new ModeloMisPlanesReVertical(1, 0, "Items 1"));
        subItems.add(new ModeloMisPlanesReVertical(2, 0, "Items 2"));
        subItems.add(new ModeloMisPlanesReVertical(1, 0, "Items 1"));
        subItems.add(new ModeloMisPlanesReVertical(2, 0, "Items 2"));
        // Agrega más subítems según sea necesario
        return subItems;
    }*/










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