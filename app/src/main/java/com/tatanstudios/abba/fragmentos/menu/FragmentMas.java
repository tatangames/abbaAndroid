package com.tatanstudios.abba.fragmentos.menu;

import static android.content.Context.MODE_PRIVATE;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewTreeObserver;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.login.LoginActivity;
import com.tatanstudios.abba.activitys.perfil.ActualizarPasswordActivity;
import com.tatanstudios.abba.activitys.perfil.EditarPerfilActivity;
import com.tatanstudios.abba.activitys.perfil.VerNotificacionesActivity;
import com.tatanstudios.abba.activitys.splash.SplashActivity;
import com.tatanstudios.abba.adaptadores.mas.AdaptadorFragmentMas;
import com.tatanstudios.abba.extras.InterfaceActualizarTema;
import com.tatanstudios.abba.extras.LanguageUtils;
import com.tatanstudios.abba.modelos.mas.ModeloFraMasConfig;
import com.tatanstudios.abba.modelos.mas.ModeloFraMasPerfil;
import com.tatanstudios.abba.modelos.mas.ModeloFragmentMas;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentMas extends Fragment {


    private static final int APP_INGLES = 1;
    private static final int APP_ESPANOL = 0;

    private RecyclerView recyclerMas;

    private RelativeLayout rootRelative;

    private ApiService service;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ArrayList<ModeloFragmentMas> elementos;

    private String letra = "";
    private String nombreUsuario = "";

    private boolean bottomSheetShowing, bottomDialogIdioma = false;


    private InterfaceActualizarTema mListener;

    private boolean bloqueoPorTema;

    private boolean unaVezMlistener, unaVezRadioIdioma;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_mas, container, false);

        rootRelative = vista.findViewById(R.id.rootRelative);
        recyclerMas = vista.findViewById(R.id.recyclerMas);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        int colorProgress = ContextCompat.getColor(getContext(), R.color.colorProgress);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        rootRelative.addView(progressBar, params);

        bloqueoPorTema = true;
        unaVezMlistener = true;
        unaVezRadioIdioma = true;


        apiInformacionListado();

        return vista;
    }



    private void apiInformacionListado(){

        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.informacionListadoAjuste(iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                           letra = apiRespuesta.getLetra();
                                           nombreUsuario = apiRespuesta.getNombre();

                                           llenarLista();
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




    private void llenarLista(){

        elementos = new ArrayList<>();

        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_PERFIL, new ModeloFraMasPerfil(letra, nombreUsuario), null));

        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(1, getString(R.string.notificaciones))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(2, getString(R.string.contrasena))));

        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_LINEA_SEPARACION, null, null));

        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(3, getString(R.string.insignias))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(4, getString(R.string.idioma))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(5, getString(R.string.temas))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(6, getString(R.string.cerrar_sesion))));

        AdaptadorFragmentMas adapter = new AdaptadorFragmentMas(getContext(), elementos, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerMas.setLayoutManager(layoutManager);
        recyclerMas.setHasFixedSize(true);
        recyclerMas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMas.setAdapter(adapter);
    }




    public void verPosicion(int tipo){

        if(bloqueoPorTema){
            switch (tipo){

                case 1:
                    verNotificaciones();
                    break;

                case 2:
                    modificarPassword();
                    break;

                case 3:
                    verInsignias();
                    break;

                    case 4:
                    cambiarIdiomaModal();
                    break;

                case 5:
                    editarTema();
                    break;

                case 6:
                    cerrarSesion();
                    break;

                default:
                    break;
            }
        }
    }

    private void modificarPassword(){
        Intent intentLogin = new Intent(getContext(), ActualizarPasswordActivity.class);
        startActivity(intentLogin);
    }

    private void verInsignias(){


    }





    private void editarTema(){

        if (!bottomSheetShowing) {
            bottomSheetShowing = true;

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            View bottomSheetView = getLayoutInflater().inflate(R.layout.botton_sheet_tema, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            SwitchCompat switchCompat = bottomSheetDialog.findViewById(R.id.switchButtonTema);

            if(tokenManager.getToken().getTema() == 0){
                switchCompat.setTextOff(getString(R.string.claro));
                switchCompat.setChecked(false);
            }else{
                switchCompat.setTextOn(getString(R.string.oscuro));
                switchCompat.setChecked(true);
            }


            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                    // Verifica si el cambio es causado por una animación
                    if (buttonView.isPressed()) {
                        // Crea una animación
                        ObjectAnimator animator = ObjectAnimator.ofFloat(buttonView, "translationX", 0f);
                        animator.setDuration(500); // Duración de la animación en milisegundos

                        // Agrega un oyente para detectar el final de la animación
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {

                                switchCompat.setEnabled(false);
                                bloqueoPorTema = false;


                                // Aquí puedes ejecutar el código después de que termina la animación
                                if (buttonView.isChecked()) {
                                    // El SwitchCompat está en la posición ON
                                    // Tu código aquí
                                    if(unaVezMlistener){
                                        unaVezMlistener = false;
                                        tokenManager.guardarEstiloTema(1);
                                        mListener.onFragmentInteraction(1);
                                    }

                                } else {
                                    // El SwitchCompat está en la posición OFF
                                    // Tu código aquí
                                    if(unaVezMlistener){
                                        unaVezMlistener = false;
                                        tokenManager.guardarEstiloTema(0);
                                        mListener.onFragmentInteraction(0);
                                    }
                                }

                                bottomSheetDialog.dismiss();
                            }
                        });

                        // Inicia la animación
                        animator.start();
                    }
                }
            });



            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetDialog.setOnDismissListener(dialog -> {
                bottomSheetShowing = false;
            });

            bottomSheetDialog.show();
        }
    }




    private void verNotificaciones(){
        Intent intentLogin = new Intent(getContext(), VerNotificacionesActivity.class);
        startActivity(intentLogin);
    }


    public void editarPerfil(){
        Intent intentLogin = new Intent(getContext(), EditarPerfilActivity.class);
        someActivityResultLauncher.launch(intentLogin);
    }




    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    apiInformacionListado();
                }
            });

    boolean seguroCerrarSesion = true;

    void cerrarSesion(){

        if(seguroCerrarSesion) {
            seguroCerrarSesion = false;

            KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.WARNING_TYPE, false);

            pDialog.setTitleText(getString(R.string.cerrar_sesion));
            pDialog.setTitleTextGravity(Gravity.CENTER);
            pDialog.setTitleTextSize(19);

            pDialog.setContentText("");
            pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START);
            pDialog.setContentTextSize(17);

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.confirmButtonColor(R.drawable.kalert_dialog_corners_confirmar);
            pDialog.setConfirmClickListener(getString(R.string.si), sDialog -> {
                sDialog.dismissWithAnimation();
                salir();
            });

            pDialog.cancelButtonColor(R.drawable.kalert_dialog_corners_cancelar);
            pDialog.setCancelClickListener(getString(R.string.no), sDialog -> {
                sDialog.dismissWithAnimation();
                seguroCerrarSesion = true;
            });

            pDialog.show();
        }
    }


    private void cambiarIdiomaModal(){

        if (!bottomDialogIdioma) {
            bottomDialogIdioma = true;


            BottomSheetDialog bottomSheetDialogIdioma = new BottomSheetDialog(requireContext());
            View bottomSheetView = getLayoutInflater().inflate(R.layout.modal_opciones_idiomas, null);
            bottomSheetDialogIdioma.setContentView(bottomSheetView);

            RadioButton radioIngles = bottomSheetDialogIdioma.findViewById(R.id.radio_button_english);
            RadioButton radioEspanol = bottomSheetDialogIdioma.findViewById(R.id.radio_button_spanish);


            if(tokenManager.getToken().getIdioma() == 0){ // espanol
                radioEspanol.setChecked(true);
                radioIngles.setChecked(false);
            }
            else if(tokenManager.getToken().getIdioma() == 1){ // ingles
                radioIngles.setChecked(true);
                radioEspanol.setChecked(false);
            }else{
                // defecto espanol
                radioEspanol.setChecked(true);
                radioIngles.setChecked(false);
            }

            radioIngles.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(unaVezRadioIdioma){
                      unaVezRadioIdioma = false;
                        radioEspanol.setEnabled(false);
                        tokenManager.guardarIdioma(APP_INGLES);
                        changeLanguage();
                    }

                }
            });

            radioEspanol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(unaVezRadioIdioma){
                        unaVezRadioIdioma = false;
                        radioEspanol.setEnabled(false);
                        tokenManager.guardarIdioma(APP_ESPANOL);
                        changeLanguage();

                    }

                }
            });

            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetDialogIdioma.setOnDismissListener(dialog -> {
                bottomDialogIdioma = false;
            });

            bottomSheetDialogIdioma.show();
        }
    }


    private void changeLanguage() {


        KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.SUCCESS_TYPE, false);

        pDialog.setTitleText(getString(R.string.idioma_actualizado));
        pDialog.setTitleTextGravity(Gravity.CENTER);
        pDialog.setTitleTextSize(19);

        pDialog.setContentText(getString(R.string.para_aplicar_efectos_se_debe_reiniciar));
        pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START);
        pDialog.setContentTextSize(17);

        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.kalert_dialog_corners_confirmar);
        pDialog.setConfirmClickListener(getString(R.string.reiniciar), sDialog -> {
            sDialog.dismissWithAnimation();
            reiniciarApp();
        });
        pDialog.show();
    }

    private void reiniciarApp(){
        Intent intentLogin = new Intent(getContext(), SplashActivity.class);
        startActivity(intentLogin);
        getActivity().finish();
    }



    void salir(){
        tokenManager.deletePreferences();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }







    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InterfaceActualizarTema) {
            mListener = (InterfaceActualizarTema) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(getContext(), getString(R.string.error_intentar_de_nuevo)).show();
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
