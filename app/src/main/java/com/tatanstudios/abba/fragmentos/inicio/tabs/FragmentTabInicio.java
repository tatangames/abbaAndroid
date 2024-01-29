package com.tatanstudios.abba.fragmentos.inicio.tabs;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.STORAGE_SERVICE;


import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.inicio.CuestionarioInicioActivity;
import com.tatanstudios.abba.activitys.inicio.InformacionInsigniaActivity;
import com.tatanstudios.abba.activitys.inicio.InformacionPlanVistaActivity;
import com.tatanstudios.abba.activitys.inicio.ListadoImagenesActivity;
import com.tatanstudios.abba.activitys.inicio.ListadoInsigniasActivity;
import com.tatanstudios.abba.activitys.inicio.ListadoVideosActivity;
import com.tatanstudios.abba.activitys.videos.VideoServidorActivity;
import com.tatanstudios.abba.adaptadores.inicio.AdaptadorInicio;
import com.tatanstudios.abba.extras.ImageUtils;
import com.tatanstudios.abba.extras.OnDataUpdateListener;
import com.tatanstudios.abba.modelos.inicio.ModeloContenedorInicio;
import com.tatanstudios.abba.modelos.inicio.ModeloVistasInicio;
import com.tatanstudios.abba.modelos.inicio.bloques.comparteapp.ModeloInicioComparteApp;
import com.tatanstudios.abba.modelos.inicio.bloques.separador.ModeloInicioSeparador;
import com.tatanstudios.abba.modelos.inicio.bloques.versiculos.ModeloInicioDevocional;
import com.tatanstudios.abba.modelos.rachas.ModeloRachas;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import android.app.Activity;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class FragmentTabInicio extends Fragment implements EasyPermissions.PermissionCallbacks{


    private ProgressBar progressBar;

    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ArrayList<ModeloVistasInicio> elementos;

    private AdaptadorInicio adapter;

    private ModeloInicioSeparador modeloInicioSeparador;

    private boolean bottomSheetImagen = false;

    int colorProgress = 0;

    private ColorStateList colorStateTintGrey, colorStateTintWhite, colorStateTintBlack;

    private int colorBlanco = 0;
    private int colorBlack = 0;

    private boolean temaActual = false;
    private boolean bloqueCompartir = true;

    private final int MY_PERMISSION_STORAGE_101 = 101;

    private Bitmap bitmapImgShare;


    RequestOptions opcionesGlideOriginal = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Forzar la carga desde la memoria caché para mejorar la velocidad
            .placeholder(R.drawable.camaradefecto)
            .override(Target.SIZE_ORIGINAL); // Cargar la imagen con su resolución original


    private OnDataUpdateListener onDataUpdateListener;

    // Método de fábrica para crear una nueva instancia de Fragment1
    public static FragmentTabInicio newInstance(OnDataUpdateListener listener) {
        FragmentTabInicio fragment = new FragmentTabInicio();
        fragment.onDataUpdateListener = listener;
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_tab_inicio, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        rootRelative = vista.findViewById(R.id.rootRelative);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        if(tokenManager.getToken().getTema() == 1){
            temaActual = true;
        }

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);


        colorBlanco = ContextCompat.getColor(requireContext(), R.color.white);
        colorBlack = ContextCompat.getColor(requireContext(), R.color.black);
        int colorGris = ContextCompat.getColor(requireContext(), R.color.colorGrisBtnDisable);

        colorStateTintGrey = ColorStateList.valueOf(colorGris);
        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);

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

        // llenar un modelo de rachas

        ModeloRachas modeloRachas = null;

        for (ModeloRachas mRa : apiRespuesta.getModeloRachas()){
            modeloRachas = new ModeloRachas(mRa.getDiasesteanio(),
                    mRa.getDiasconcecutivos(), mRa.getNivelrachaalta(),
                    mRa.getDomingo(), mRa.getLunes(), mRa.getMartes(),
                    mRa.getMiercoles(), mRa.getJueves(), mRa.getViernes(),
                    mRa.getSabado());
        }

        if (onDataUpdateListener != null) {
            onDataUpdateListener.updateData(modeloRachas);
        }

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


    public void abrirModalImagenes(String urlImagen){

        if (!bottomSheetImagen) {
            bottomSheetImagen = true;


            // Mostrar bottomdialog de modal cargando

            BottomSheetDialog bottomSheetProgreso = new BottomSheetDialog(requireContext());
            View bottomSheetViewProgreso = getLayoutInflater().inflate(R.layout.botton_sheet_imagen_loading, null);
            bottomSheetProgreso.setContentView(bottomSheetViewProgreso);


            ProgressBar barraProgress = bottomSheetProgreso.findViewById(R.id.progressBarLoading);
            barraProgress.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

            ShapeableImageView imgImagen = bottomSheetProgreso.findViewById(R.id.imgImagen);
            Button btnDescargar = bottomSheetProgreso.findViewById(R.id.btnDescargar);
            Button btnCompartir = bottomSheetProgreso.findViewById(R.id.btnCompartir);

            if(urlImagen != null && !TextUtils.isEmpty(urlImagen)){
                Glide.with(this)
                        .load(RetrofitBuilder.urlImagenes + urlImagen)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                // Manejar el caso de carga fallida aquí
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                // La imagen se ha cargado exitosamente, realizar acciones aquí

                                bitmapImgShare = ((BitmapDrawable) resource).getBitmap();
                                bloqueCompartir = true;

                                barraProgress.setVisibility(View.GONE);
                                imgImagen.setVisibility(View.VISIBLE);
                                btnDescargar.setVisibility(View.VISIBLE);
                                btnCompartir.setVisibility(View.VISIBLE);

                                return false;
                            }
                        })
                        .apply(opcionesGlideOriginal)
                        .into(imgImagen);
            }else{
                int resourceId = R.drawable.camaradefecto;
                Glide.with(this)
                        .load(resourceId)
                        .into(imgImagen);
            }

            if(temaActual){ // Dark
                btnDescargar.setBackgroundTintList(colorStateTintWhite);
                btnDescargar.setTextColor(colorBlack);

                btnCompartir.setBackgroundTintList(colorStateTintWhite);
                btnCompartir.setTextColor(colorBlack);
            }else{
                btnDescargar.setBackgroundTintList(colorStateTintBlack);
                btnDescargar.setTextColor(colorBlanco);

                btnCompartir.setBackgroundTintList(colorStateTintBlack);
                btnCompartir.setTextColor(colorBlanco);
            }


            btnDescargar.setOnClickListener(v -> {


                String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

                if(EasyPermissions.hasPermissions(getActivity(),
                        perms)){
                    // permiso autorizado

                    if(guardarImagen()){
                        bottomSheetProgreso.dismiss();
                    }

                }else{
                    // permiso denegado
                    EasyPermissions.requestPermissions(getActivity(),
                            getString(R.string.permiso_almacenamiento_es_requerido),
                            MY_PERMISSION_STORAGE_101,
                            perms);
                }

            });


            btnCompartir.setOnClickListener(v -> {
                if(bloqueCompartir){
                    bloqueCompartir = false;
                    compartirImagen();
                    bottomSheetProgreso.dismiss();
                }
            });


            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetProgreso.setOnDismissListener(dialog -> {
                bitmapImgShare = null;
                bottomSheetImagen = false;
            });

            bottomSheetProgreso.show();
        }
    }

    private void compartirImagen(){
        if(bitmapImgShare != null){

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpeg");

            // Convertir la imagen Bitmap a un Uri
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmapImgShare.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmapImgShare, "Image", null);
            Uri imageUri = Uri.parse(path);

            // Adjuntar la imagen al Intent
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            // Agregar permisos de lectura al Intent para otras aplicaciones
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Iniciar el Intent para compartir
            getActivity().startActivity(Intent.createChooser(shareIntent, getString(R.string.compartir)));
        }
    }


    private boolean guardarImagen(){

        long currentTimeMillis = System.currentTimeMillis();

        String fileName = "imagen_" + currentTimeMillis + ".png"; // nombre del archivo

        Uri ur = ImageUtils.saveImageToGallery(bitmapImgShare, getContext(), fileName);

        if(ur != null){
            Toasty.success(getActivity(), getString(R.string.guardado), Toasty.LENGTH_SHORT).show();
            return true;
        }else{
            Toasty.error(getActivity(), getString(R.string.error_al_guardar), Toasty.LENGTH_SHORT).show();
            return false;
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Propaga los resultados de la solicitud de permisos a EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(requestCode == MY_PERMISSION_STORAGE_101){
            Toasty.success(getActivity(), getString(R.string.permiso_autorizado), Toasty.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // Permiso denegado, muestra un mensaje al usuario
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // El usuario ha seleccionado "No volver a mostrar"
            // Muestra un mensaje o guía al usuario para que active manualmente el permiso
            Toasty.error(getActivity(), getString(R.string.permiso_almacenamiento_es_requerido), Toast.LENGTH_LONG).show();
        } else {
            // El usuario no ha seleccionado "No volver a mostrar", muestra un mensaje de solicitud de permiso estándar
            Toasty.error(getActivity(), getString(R.string.permiso_almacenamiento_es_requerido), Toast.LENGTH_LONG).show();
        }
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

    public void vistaInformacionInsignia(int tipoinsignia){
        Intent intent = new Intent(getContext(), InformacionInsigniaActivity.class);
        intent.putExtra("IDINSIGNIA", tipoinsignia);
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
        Intent intent = new Intent(getContext(), ListadoVideosActivity.class);
        startActivity(intent);
    }

    public void vistaTodosLasImagenes(){
        Intent intent = new Intent(getContext(), ListadoImagenesActivity.class);
        startActivity(intent);
    }

    public void vistaTodosLasInsignias(){
        Intent intent = new Intent(getContext(), ListadoInsigniasActivity.class);
        startActivity(intent);
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
