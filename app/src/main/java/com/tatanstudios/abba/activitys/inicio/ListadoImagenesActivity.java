package com.tatanstudios.abba.activitys.inicio;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.videos.VideoServidorActivity;
import com.tatanstudios.abba.adaptadores.inicio.todosimagenes.AdaptadorTodosImagenes;
import com.tatanstudios.abba.adaptadores.inicio.todosvideos.AdaptadorTodosVideos;
import com.tatanstudios.abba.extras.ImageUtils;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.io.ByteArrayOutputStream;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.EasyPermissions;

public class ListadoImagenesActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private RecyclerView recyclerView;

    private RelativeLayout rootRelative;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TokenManager tokenManager;

    private ProgressBar progressBar;

    private TextView txtToolbar;
    private ImageView imgFlechaAtras;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private AdaptadorTodosImagenes adaptadorTodosImagenes;

    private Bitmap bitmapImgShare;

    private boolean bottomSheetImagen = false;

    private  int colorProgress = 0;

    private boolean temaActual = false;

    private boolean bloqueCompartir = true;

    private ColorStateList colorStateTintWhite, colorStateTintBlack;

    private int colorBlanco = 0;
    private int colorBlack = 0;

    private final int MY_PERMISSION_STORAGE_101 = 101;

    RequestOptions opcionesGlideOriginal = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL) // Forzar la carga desde la memoria caché para mejorar la velocidad
            .placeholder(R.drawable.camaradefecto)
            .override(Target.SIZE_ORIGINAL); // Cargar la imagen con su resolución original



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_imagenes);


        recyclerView = findViewById(R.id.recyclerView);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        txtToolbar = findViewById(R.id.txtToolbar);
        rootRelative = findViewById(R.id.rootRelative);

        txtToolbar.setText(getString(R.string.imagenes));

        colorProgress = ContextCompat.getColor(this, R.color.colorProgress);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);


        colorBlanco = ContextCompat.getColor(this, R.color.white);
        colorBlack = ContextCompat.getColor(this, R.color.black);

        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);


        if(tokenManager.getToken().getTema() == 1){
            temaActual = true;
        }


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

        apiBuscarTodosLasImagenes();
    }


    private void apiBuscarTodosLasImagenes(){

        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.obtenerTodosLasImagenes(iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            adaptadorTodosImagenes = new AdaptadorTodosImagenes(getApplicationContext(), apiRespuesta.getModeloInicioImagenes(), this);
                                            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
                                            layoutManager.setOrientation(RecyclerView.VERTICAL);
                                            recyclerView.setLayoutManager(layoutManager);
                                            recyclerView.setAdapter(adaptadorTodosImagenes);

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




    public void abrirModalImagenes(String urlImagen){

        if (!bottomSheetImagen) {
            bottomSheetImagen = true;


            // Mostrar bottomdialog de modal cargando

            BottomSheetDialog bottomSheetProgreso = new BottomSheetDialog(this);
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

                if(EasyPermissions.hasPermissions(this,
                        perms)){
                    // permiso autorizado

                    if(guardarImagen()){
                        bottomSheetProgreso.dismiss();
                    }

                }else{
                    // permiso denegado
                    EasyPermissions.requestPermissions(this,
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
            String path = MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmapImgShare, "Image", null);
            Uri imageUri = Uri.parse(path);

            // Adjuntar la imagen al Intent
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

            // Agregar permisos de lectura al Intent para otras aplicaciones
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Iniciar el Intent para compartir
            startActivity(Intent.createChooser(shareIntent, getString(R.string.compartir)));
        }
    }


    private boolean guardarImagen(){

        long currentTimeMillis = System.currentTimeMillis();

        String fileName = "imagen_" + currentTimeMillis + ".png"; // nombre del archivo

        Uri ur = ImageUtils.saveImageToGallery(bitmapImgShare, this, fileName);

        if(ur != null){
            Toasty.success(this, getString(R.string.guardado), Toasty.LENGTH_SHORT).show();
            return true;
        }else{
            Toasty.error(this, getString(R.string.error_al_guardar), Toasty.LENGTH_SHORT).show();
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
            Toasty.success(this, getString(R.string.permiso_autorizado), Toasty.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // Permiso denegado, muestra un mensaje al usuario
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // El usuario ha seleccionado "No volver a mostrar"
            // Muestra un mensaje o guía al usuario para que active manualmente el permiso
            Toasty.error(this, getString(R.string.permiso_almacenamiento_es_requerido), Toast.LENGTH_LONG).show();
        } else {
            // El usuario no ha seleccionado "No volver a mostrar", muestra un mensaje de solicitud de permiso estándar
            Toasty.error(this, getString(R.string.permiso_almacenamiento_es_requerido), Toast.LENGTH_LONG).show();
        }
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