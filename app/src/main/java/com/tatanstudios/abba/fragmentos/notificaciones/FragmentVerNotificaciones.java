package com.tatanstudios.abba.fragmentos.notificaciones;

import static android.content.Context.MODE_PRIVATE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedDispatcher;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.registro.FragmentRegistro;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import es.dmoral.toasty.Toasty;
import io.reactivex.disposables.CompositeDisposable;

public class FragmentVerNotificaciones extends Fragment {

    private RecyclerView recycler;

    private RelativeLayout rootRelative;

    private ApiService service;
    private TokenManager tokenManager;
    private ProgressBar progressBar;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView txtToolbar;
    private ImageView imgFlechaAtras, imgAjustes;


    private OnBackPressedDispatcher onBackPressedDispatcher;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ver_notificaciones, container, false);

        rootRelative = vista.findViewById(R.id.rootRelative);
        recycler = vista.findViewById(R.id.recycler);
        txtToolbar = vista.findViewById(R.id.txtToolbar);
        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);
        imgAjustes = vista.findViewById(R.id.imgAjustes);

        txtToolbar.setText(getString(R.string.notificaciones));

        onBackPressedDispatcher = getActivity().getOnBackPressedDispatcher();

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);

        imgFlechaAtras.setOnClickListener(v -> {
            onBackPressedDispatcher.onBackPressed();
        });

        imgAjustes.setOnClickListener(v -> {

            verAjusteNotificacion();
        });




        solicitarNotificaciones();

        return vista;
    }

    private void solicitarNotificaciones(){


    }


    private void verAjusteNotificacion(){

        FragmentEditarNotificacion fragmentEditarNotificacion = new FragmentEditarNotificacion();
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
        if(currentFragment.getClass().equals(fragmentEditarNotificacion.getClass())) return;

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentEditarNotificacion)
                .addToBackStack(null)
                .commit();
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
