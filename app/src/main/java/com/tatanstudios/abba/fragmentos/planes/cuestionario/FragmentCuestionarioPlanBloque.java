package com.tatanstudios.abba.fragmentos.planes.cuestionario;

import static android.content.Context.MODE_PRIVATE;
import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tatanstudios.abba.R;
import androidx.core.text.HtmlCompat;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentCuestionarioPlanBloque extends Fragment {


    private ProgressBar progressBar;
    private TokenManager tokenManager;

    private FloatingActionButton fabButton;

    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView txtHtml;

    private static final String ARG_DATO = "IDBLOQUE";

    private int idBloqueDeta = 0;

    public static FragmentCuestionarioPlanBloque newInstance(int dato) {
        FragmentCuestionarioPlanBloque fragment = new FragmentCuestionarioPlanBloque();
        Bundle args = new Bundle();
        args.putInt(ARG_DATO, dato);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_cuestionario_plan_bloque_contenedor, container, false);

        rootRelative = vista.findViewById(R.id.rootRelative);
        txtHtml = vista.findViewById(R.id.txtCuestionario);
        //fabButton = vista.findViewById(R.id.fabButton);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        idBloqueDeta = getArguments().getInt(ARG_DATO, 0);

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);


       // fabButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));


        apiBuscarCuestionario();


        return vista;
    }


    private void apiBuscarCuestionario(){

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.informacionCuestionarioBloqueDetalle(iduser, idBloqueDeta, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {


                                            setearTexto(apiRespuesta.getTitulo());
                                        }
                                        else if(apiRespuesta.getSuccess() == 2) {
                                            // BLOQUE NO TIENE CUESTIONARIO

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


    private void setearTexto(String texto){

        if(texto != null){
            if(!TextUtils.isEmpty(texto)){
                txtHtml.setText(HtmlCompat.fromHtml(texto, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        }
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
