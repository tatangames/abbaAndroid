package com.tatanstudios.abba.fragmentos.menu;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.login.LoginActivity;
import com.tatanstudios.abba.activitys.perfil.EditarPerfilActivity;
import com.tatanstudios.abba.activitys.perfil.VerNotificacionesActivity;
import com.tatanstudios.abba.activitys.principal.PrincipalActivity;
import com.tatanstudios.abba.adaptadores.mas.AdaptadorFragmentMas;
import com.tatanstudios.abba.modelos.mas.ModeloFraMasConfig;
import com.tatanstudios.abba.modelos.mas.ModeloFraMasPerfil;
import com.tatanstudios.abba.modelos.mas.ModeloFragmentMas;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentMas extends Fragment {


    private RecyclerView recyclerMas;

    private RelativeLayout rootRelative;

    private ApiService service;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private ArrayList<ModeloFragmentMas> elementos;

    private String letra = "";
    private String nombreUsuario = "";



    private boolean bottomSheetShowing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_mas, container, false);

        rootRelative = vista.findViewById(R.id.rootRelative);
        recyclerMas = vista.findViewById(R.id.recyclerMas);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);

        int colorProgress = ContextCompat.getColor(getContext(), R.color.colorProgress);
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        rootRelative.addView(progressBar, params);






        informacionListado();

        return vista;
    }




    private void informacionListado(){

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
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_LINEA_SEPARACION, null, null));

        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(2, getString(R.string.versiculo_del_dia))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(3, getString(R.string.oracion))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(4, getString(R.string.videos))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(5, getString(R.string.eventos))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_LINEA_SEPARACION, null, null));

        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(6, getString(R.string.imagenes))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(7, getString(R.string.notas))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(8, getString(R.string.insignias))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_LINEA_SEPARACION, null, null));

        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(9, getString(R.string.acerca_de))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(10, getString(R.string.idioma))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(11, getString(R.string.temas))));
        elementos.add(new ModeloFragmentMas( ModeloFragmentMas.TIPO_ITEM_NORMAL, null, new ModeloFraMasConfig(12, getString(R.string.configuracion))));


        AdaptadorFragmentMas adapter = new AdaptadorFragmentMas(getContext(), elementos, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerMas.setLayoutManager(layoutManager);
        recyclerMas.setHasFixedSize(true);
        recyclerMas.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMas.setAdapter(adapter);
    }



    public void verPosicion(int tipo){

        switch (tipo){

            case 1:
                verNotificaciones();
                break;

            case 2:
                editarTema();
                break;
            case 12:
                cerrarSesion();
                break;

            default:
                break;
        }
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

            switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {

                if (isChecked) {
                    tokenManager.guardarEstiloTema(1);
                    alertaTemaCambio();
                } else {
                    tokenManager.guardarEstiloTema(0);
                    alertaTemaCambio();
                }

            });

            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetDialog.setOnDismissListener(dialog -> {
                bottomSheetShowing = false;
            });

            bottomSheetDialog.show();
        }
    }


    private void alertaTemaCambio(){
        KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(getString(R.string.tema_actualizado));
        pDialog.setContentText(getString(R.string.para_aplicar_efectos_se_debe_reiniciar));
        pDialog.setConfirmText(getString(R.string.reiniciar));
        pDialog.setContentTextSize(16);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    reiniciarApp();
                });
        pDialog.show();

    }

    private void verNotificaciones(){
        Intent intentLogin = new Intent(getContext(), VerNotificacionesActivity.class);
        startActivity(intentLogin);
    }


    public void editarPerfil(){
        Intent intentLogin = new Intent(getContext(), EditarPerfilActivity.class);
        startActivity(intentLogin);
    }

    private void reiniciarApp(){
        Intent intentLogin = new Intent(getContext(), PrincipalActivity.class);
        startActivity(intentLogin);
        getActivity().finish();
    }

    boolean seguroCerrarSesion = true;

    void cerrarSesion(){

        if(seguroCerrarSesion) {
            seguroCerrarSesion = false;
            KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.WARNING_TYPE);
            pDialog.setTitleText(getString(R.string.cerrar_sesion));
            pDialog.setContentText("");
            pDialog.setConfirmText(getString(R.string.si));
            pDialog.setContentTextSize(16);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.confirmButtonColor(R.drawable.dialogo_theme_success)
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismissWithAnimation();
                        salir();
                    });
            pDialog.cancelButtonColor(R.drawable.dialogo_theme_cancel)
                    .setContentTextSize(16)
                    .setCancelText(getString(R.string.no))
                    .setCancelClickListener(kAlertDialog -> {
                        kAlertDialog.dismissWithAnimation();
                        seguroCerrarSesion = true;
                    });
            pDialog.show();
        }
    }












    void salir(){
        tokenManager.deletePreferences();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
