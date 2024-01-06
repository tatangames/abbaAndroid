package com.tatanstudios.abba.fragmentos.notificaciones;

import static android.content.Context.MODE_PRIVATE;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.AppBarLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

public class FragmentEditarNotificacion extends Fragment {


    private TextView txtToolbar;
    private ImageView imgFlechaAtras;


    private SwitchCompat switchCompat;

    private ScrollView scrollView;

    private FrameLayout frameLayout;
    private boolean isShadowVisible = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_editar_notificaciones, container, false);

        txtToolbar = vista.findViewById(R.id.txtToolbar);
        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);

        txtToolbar.setText(getString(R.string.editar_notificaciones));


        imgFlechaAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });





        return vista;
    }


}
