package com.tatanstudios.abba.fragmentos.menu;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.extras.OnDataUpdateListener;
import com.tatanstudios.abba.fragmentos.inicio.tabs.TabPagerAdapter;
import com.tatanstudios.abba.modelos.rachas.ModeloRachas;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import es.dmoral.toasty.Toasty;
import pub.devrel.easypermissions.EasyPermissions;

public class FragmentInicio extends Fragment implements OnDataUpdateListener {


    private TokenManager tokenManager;
    private int tabStrokeColor, tabTextColor, colorPrimary;
    private ImageView imgNoti;

    private TextView txtRacha;

    private int colorProgress;

    private boolean hayInfoRachas = false;

    private ModeloRachas modeloRachas = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_inicio, container, false);

        ViewPager viewPager = vista.findViewById(R.id.viewPager);
        TabLayout tabLayout = vista.findViewById(R.id.tabLayout);
        txtRacha = vista.findViewById(R.id.txtRacha);
        imgNoti = vista.findViewById(R.id.imgNoti);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));


        colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);


        if (tokenManager.getToken().getTema() == 1) {
            colorPrimary = ContextCompat.getColor(getContext(), R.color.colorTextoGris); // Obtén el color para tema dark
            tabTextColor = ContextCompat.getColor(getContext(), R.color.black); // Obtén el color para tema dark
            tabStrokeColor = ContextCompat.getColor(getContext(), R.color.white); // Obtén el color para tema dark

            int color = ContextCompat.getColor(getContext(), R.color.white);

            Drawable iconDrawable = txtRacha.getCompoundDrawables()[0];

            // Cambiar el color del Drawable
            DrawableCompat.setTint(iconDrawable, color);
            DrawableCompat.setTintMode(iconDrawable, PorterDuff.Mode.SRC_IN);

            // Establecer el Drawable actualizado en el TextView
            txtRacha.setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null);

            imgNoti.setColorFilter(color);

        } else {
            colorPrimary = ContextCompat.getColor(getContext(), R.color.white); // Obtén el color para tema light
            tabTextColor = ContextCompat.getColor(getContext(), R.color.grey); // Obtén el color para tema light
            tabStrokeColor = ContextCompat.getColor(getContext(), R.color.black); // Obtén el color para tema light


            int color = ContextCompat.getColor(getContext(), R.color.black);

            Drawable iconDrawable = txtRacha.getCompoundDrawables()[0];

            // Cambiar el color del Drawable
            DrawableCompat.setTint(iconDrawable, color);
            DrawableCompat.setTintMode(iconDrawable, PorterDuff.Mode.SRC_IN);

            // Establecer el Drawable actualizado en el TextView
            txtRacha.setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null);

            imgNoti.setColorFilter(color);
        }

        tabLayout.setBackgroundColor(colorPrimary);
        tabLayout.setTabTextColors(tabTextColor, tabStrokeColor);


        txtRacha.setOnClickListener(v -> {
            if(hayInfoRachas){
                informacionRacha();
            }
        });

        imgNoti.setOnClickListener(v -> {

        });

        // Configura el adaptador del ViewPager y agrega pestañas al TabLayout
        TabPagerAdapter adapter = new TabPagerAdapter(getContext(), getChildFragmentManager(), this);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        return vista;
    }


    @Override
    public void updateData(ModeloRachas modeloR) {

        if(modeloR != null){
            hayInfoRachas = true;
            modeloRachas = modeloR;

            txtRacha.setText(String.valueOf(modeloR.getNivelrachaalta()));
            txtRacha.setVisibility(View.VISIBLE);
        }
    }


    private void informacionRacha(){

            BottomSheetDialog bottomSheetRacha = new BottomSheetDialog(requireContext());
            View bottomSheetViewLayout = getLayoutInflater().inflate(R.layout.botton_sheet_racha, null);
            bottomSheetRacha.setContentView(bottomSheetViewLayout);


            TextView txtRachaAlta = bottomSheetRacha.findViewById(R.id.textViewLeft);
            TextView txtDiasSeguidos = bottomSheetRacha.findViewById(R.id.textViewRight);
            TextView txtDiasEsteAnio = bottomSheetRacha.findViewById(R.id.txtDiasEsteAnio);

            ImageView imgLunes = bottomSheetRacha.findViewById(R.id.imgLunes);
            ImageView imgMartes = bottomSheetRacha.findViewById(R.id.imgMartes);
            ImageView imgMiercoles = bottomSheetRacha.findViewById(R.id.imgMiercoles);
            ImageView imgJueves = bottomSheetRacha.findViewById(R.id.imgJueves);
            ImageView imgViernes = bottomSheetRacha.findViewById(R.id.imgViernes);
            ImageView imgSabado = bottomSheetRacha.findViewById(R.id.imgSabado);
            ImageView imgDomingo = bottomSheetRacha.findViewById(R.id.imgDomingo);

            txtRachaAlta.setText(String.valueOf(modeloRachas.getNivelrachaalta()));
            txtDiasSeguidos.setText(String.valueOf(modeloRachas.getDiasconcecutivos()));
            txtDiasEsteAnio.setText(String.valueOf(modeloRachas.getDiasesteanio()));

            if(modeloRachas.getLunes() == 1){
                imgLunes.setImageResource(R.drawable.circuloblancocronometro);
            }

            if(modeloRachas.getMartes() == 1){
                imgMartes.setImageResource(R.drawable.circuloblancocronometro);
            }

            if(modeloRachas.getMiercoles() == 1){
                imgMiercoles.setImageResource(R.drawable.circuloblancocronometro);
            }

            if(modeloRachas.getJueves() == 1){
                imgJueves.setImageResource(R.drawable.circuloblancocronometro);
            }

            if(modeloRachas.getViernes() == 1){
                imgViernes.setImageResource(R.drawable.circuloblancocronometro);
            }

            if(modeloRachas.getSabado() == 1){
                imgSabado.setImageResource(R.drawable.circuloblancocronometro);
            }

            if(modeloRachas.getDomingo() == 1){
                imgDomingo.setImageResource(R.drawable.circuloblancocronometro);
            }

        bottomSheetRacha.show();
    }



}
