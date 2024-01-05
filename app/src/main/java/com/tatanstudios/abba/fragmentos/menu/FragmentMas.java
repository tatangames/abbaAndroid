package com.tatanstudios.abba.fragmentos.menu;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.kalert.KAlertDialog;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.login.LoginActivity;
import com.tatanstudios.abba.activitys.perfil.EditarPerfilActivity;
import com.tatanstudios.abba.activitys.perfil.VerNotificacionesActivity;
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

            case 12:
                cerrarSesion();
                break;

            default:
                break;
        }
    }


    private void verNotificaciones(){
        Intent intentLogin = new Intent(getContext(), VerNotificacionesActivity.class);
        startActivity(intentLogin);
    }


    public void editarPerfil(){
        Intent intentLogin = new Intent(getContext(), EditarPerfilActivity.class);
        startActivity(intentLogin);
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
