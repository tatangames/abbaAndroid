package com.tatanstudios.abba.fragmentos.login;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.principal.PrincipalActivity;
import com.tatanstudios.abba.fragmentos.registro.FragmentRegistro;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentLogin extends Fragment {



    private TextView txtIngresar;
    private Button btnRegistro;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_login, container, false);

        btnRegistro = vista.findViewById(R.id.btnRegistro);
        txtIngresar = vista.findViewById(R.id.btnIngresar);


        btnRegistro.setOnClickListener(v ->{
            vistaRegistro();
        });

        txtIngresar.setOnClickListener(v ->{
            vistaIngresarDatos();
        });

        return vista;
    }

    private void vistaIngresarDatos(){

        FragmentLoginDatos fragmentLoginDatos = new FragmentLoginDatos();
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
        if(currentFragment.getClass().equals(fragmentLoginDatos.getClass())) return;

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentLoginDatos)
                .addToBackStack(null)
                .commit();
    }



    private void vistaRegistro(){
        FragmentRegistro fragmentRegistro = new FragmentRegistro();
        Fragment currentFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContenedor);
        if(currentFragment.getClass().equals(fragmentRegistro.getClass())) return;

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, fragmentRegistro)
                .addToBackStack(null)
                .commit();
    }



}
