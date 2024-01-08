package com.tatanstudios.abba.activitys.principal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.menu.FragmentBiblia;
import com.tatanstudios.abba.fragmentos.menu.FragmentInicio;
import com.tatanstudios.abba.fragmentos.menu.FragmentMas;
import com.tatanstudios.abba.fragmentos.menu.FragmentPlanes;
import com.tatanstudios.abba.network.TokenManager;

public class PrincipalActivity extends AppCompatActivity  {


    public BottomNavigationView bottomNavigationView;

    private MenuItem menuInicio;
    private MenuItem menuBiblia;
    private MenuItem menuPlanes;
    private MenuItem menuMas;

    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setBackground(null);

        tokenManager = TokenManager.getInstance(this.getSharedPreferences("prefs", MODE_PRIVATE));

        cambioIdioma();

        Menu menu = bottomNavigationView.getMenu();

        menuInicio = menu.findItem(R.id.menu_inicio);
        menuBiblia = menu.findItem(R.id.menu_biblia);
        menuPlanes = menu.findItem(R.id.menu_planes);
        menuMas = menu.findItem(R.id.menu_mas);


        if(tokenManager.getToken().getTema() == 1){
        //    changeThemeDark();
        }else{
            // changeThemeLight();
        }

        tokenManager.deletePreferences();

        getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new FragmentInicio()).commit();


        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.menu_inicio) {
                cambioMenuInicio();
                loadFragment(new FragmentInicio());
            } else if (itemId == R.id.menu_biblia) {
                cambioMenuBiblia();
                loadFragment(new FragmentBiblia());
            } else if (itemId == R.id.menu_planes) {
                cambioMenuPlanes();
                loadFragment(new FragmentPlanes());

            }
            else if (itemId == R.id.menu_mas) {
                cambioMenuMas();
                loadFragment(new FragmentMas());
            }

            return true;
        });
    }

    private void cambioIdioma(){

        /*String[] supportedLanguages = {"en", "es"};  // Idiomas soportados en tu aplicación
        String defaultLanguage = "en";  // Idioma por defecto

        String preferredLanguage = LanguageUtils.getPreferredLanguage(supportedLanguages, defaultLanguage);

        // Cambiar la configuración del idioma de la aplicación
        LocaleManager.setLocale(this, preferredLanguage);*/





    }



    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
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


    // Cambiar el tema de la actividad según sea necesario
    public void changeThemeDark() {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public void changeThemeLight() {
        getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }







}