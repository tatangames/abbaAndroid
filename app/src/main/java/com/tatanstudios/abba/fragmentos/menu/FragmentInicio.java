package com.tatanstudios.abba.fragmentos.menu;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.inicio.tabs.TabPagerAdapter;
import com.tatanstudios.abba.network.TokenManager;

public class FragmentInicio extends Fragment {


    private TokenManager tokenManager;
    private int tabStrokeColor, tabTextColor, colorPrimary;
    private ImageView imgNoti;

    private TextView txtRacha;

    private static final int REQUEST_CODE_COMPARTIR = 123;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_COMPARTIR) {
            if (resultCode == Activity.RESULT_OK) {
                // El contenido fue compartido exitosamente
                Toast.makeText(getActivity(), "La aplicación fue compartida exitosamente", Toast.LENGTH_SHORT).show();
            } else {
                // El usuario canceló la operación
                Toast.makeText(getActivity(), "El usuario canceló la operación", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_inicio, container, false);

        ViewPager viewPager = vista.findViewById(R.id.viewPager);
        TabLayout tabLayout = vista.findViewById(R.id.tabLayout);
        txtRacha = vista.findViewById(R.id.txtRacha);
        imgNoti = vista.findViewById(R.id.imgNoti);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

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



        imgNoti.setOnClickListener(v -> {

        });

        // Configura el adaptador del ViewPager y agrega pestañas al TabLayout
        TabPagerAdapter adapter = new TabPagerAdapter(getContext(), getChildFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        return vista;
    }










}
