package com.tatanstudios.abba.activitys.principal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.menu.FragmentBiblia;
import com.tatanstudios.abba.fragmentos.menu.FragmentInicio;
import com.tatanstudios.abba.fragmentos.menu.FragmentMas;
import com.tatanstudios.abba.fragmentos.menu.FragmentPlanes;

public class PrincipalActivity extends AppCompatActivity {


    public BottomNavigationView bottomNavigationView;

    private MenuItem menuInicio;
    private MenuItem menuBiblia;
    private MenuItem menuPlanes;
    private MenuItem menuMas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setBackground(null);

        Menu menu = bottomNavigationView.getMenu();

        menuInicio = menu.findItem(R.id.menu_inicio);
        menuBiblia = menu.findItem(R.id.menu_biblia);
        menuPlanes = menu.findItem(R.id.menu_planes);
        menuMas = menu.findItem(R.id.menu_mas);

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new FragmentInicio()).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {

            Fragment selectedFragment = null;
            int itemId = item.getItemId();
            if (itemId == R.id.menu_inicio) {
                cambioMenuInicio();
                selectedFragment = new FragmentInicio();
            } else if (itemId == R.id.menu_biblia) {
                cambioMenuBiblia();
                selectedFragment = new FragmentBiblia();
            } else if (itemId == R.id.menu_planes) {
                cambioMenuPlanes();
                selectedFragment = new FragmentPlanes();
            }
            else if (itemId == R.id.menu_mas) {
                cambioMenuMas();
                selectedFragment = new FragmentMas();
            }
            // It will help to replace the
            // one fragment to other.
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.main_container, selectedFragment).commit();
            }
            return true;
        });
    }




    private void cambioMenuInicio(){
        menuInicio.setIcon(R.drawable.vector_casa_lleno);

        menuBiblia.setIcon(R.drawable.vector_biblia_linea);
        menuPlanes.setIcon(R.drawable.vector_planes_linea);
        menuMas.setIcon(R.drawable.vector_tuerca_linea);
    }

    private void cambioMenuBiblia(){
        menuBiblia.setIcon(R.drawable.vector_biblia_lleno);

        menuInicio.setIcon(R.drawable.vector_casa_linea);
        menuPlanes.setIcon(R.drawable.vector_planes_linea);
        menuMas.setIcon(R.drawable.vector_tuerca_linea);
    }

    private void cambioMenuPlanes(){
        menuPlanes.setIcon(R.drawable.vector_planes_lleno);

        menuInicio.setIcon(R.drawable.vector_casa_linea);
        menuBiblia.setIcon(R.drawable.vector_biblia_linea);
        menuMas.setIcon(R.drawable.vector_tuerca_linea);
    }

    private void cambioMenuMas(){
        menuMas.setIcon(R.drawable.vector_tuerca_lleno);

        menuInicio.setIcon(R.drawable.vector_casa_linea);
        menuBiblia.setIcon(R.drawable.vector_biblia_linea);
        menuPlanes.setIcon(R.drawable.vector_planes_linea);
    }



}