package com.tatanstudios.abba.activitys.inicio;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.inicio.cuestionario.MiPagerCuestionarioInicioAdapter;
import com.tatanstudios.abba.adaptadores.planes.cuestionario.MiPagerCuestionarioAdapter;
import com.tatanstudios.abba.network.TokenManager;

public class CuestionarioInicioActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    private TokenManager tokenManager;

    private int tabStrokeColor, tabTextColor, colorPrimary;


    private int idBloqueDeta = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario_inicio);


        viewPager2 = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            idBloqueDeta = bundle.getInt("IDBLOCKDETA");
        }

        if (tokenManager.getToken().getTema() == 1) {
            colorPrimary = ContextCompat.getColor(this, R.color.white); // Obtén el color para tema dark
            tabTextColor = ContextCompat.getColor(this, R.color.grey); // Obtén el color para tema dark
            tabStrokeColor = ContextCompat.getColor(this, R.color.black); // Obtén el color para tema dark
        } else {
            colorPrimary = ContextCompat.getColor(this, R.color.white); // Obtén el color para tema light
            tabTextColor = ContextCompat.getColor(this, R.color.grey); // Obtén el color para tema light
            tabStrokeColor = ContextCompat.getColor(this, R.color.black); // Obtén el color para tema light
        }


        // Configura el ViewPager2 con el adaptador
        MiPagerCuestionarioInicioAdapter pagerAdapter = new MiPagerCuestionarioInicioAdapter(this, idBloqueDeta);
        viewPager2.setAdapter(pagerAdapter);

        tabLayout.setBackgroundColor(colorPrimary);
        tabLayout.setTabTextColors(tabTextColor, tabStrokeColor);

        // Conecta el ViewPager2 al TabLayout
        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    // Establece el texto de la pestaña según la posición
                    if (position == 0) {
                        tab.setText(getString(R.string.devocional));
                    } else {
                        tab.setText(getString(R.string.meditacion));
                    }
                }
        ).attach();
    }




}