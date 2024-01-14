package com.tatanstudios.abba.activitys.principal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.extras.InterfaceActualizarTema;
import com.tatanstudios.abba.extras.LocaleManagerExtras;
import com.tatanstudios.abba.extras.OnFragmentInteractionTema;
import com.tatanstudios.abba.fragmentos.menu.FragmentBiblia;
import com.tatanstudios.abba.fragmentos.menu.FragmentInicio;
import com.tatanstudios.abba.fragmentos.menu.FragmentMas;
import com.tatanstudios.abba.fragmentos.menu.FragmentPlanes;
import com.tatanstudios.abba.network.TokenManager;

import es.dmoral.toasty.Toasty;

public class PrincipalActivity extends AppCompatActivity  implements InterfaceActualizarTema {


    public BottomNavigationView bottomNavigationView;

    private MenuItem menuInicio;
    private MenuItem menuBiblia;
    private MenuItem menuPlanes;
    private MenuItem menuMas;


    private FragmentInicio primerFragment;
    private FragmentBiblia segundoFragment;
    private FragmentPlanes tercerFragment;
    private FragmentMas cuartoFragment;
    private Fragment fragmentActivo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        primerFragment = new FragmentInicio();
        segundoFragment = new FragmentBiblia();
        tercerFragment = new FragmentPlanes();
        cuartoFragment = new FragmentMas();

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setBackground(null);

        Menu menu = bottomNavigationView.getMenu();

        menuInicio = menu.findItem(R.id.menu_inicio);
        menuBiblia = menu.findItem(R.id.menu_biblia);
        menuPlanes = menu.findItem(R.id.menu_planes);
        menuMas = menu.findItem(R.id.menu_mas);

        // Configura el primer fragmento como el fragmento activo inicial
        fragmentActivo = primerFragment;

       // getSupportFragmentManager().beginTransaction().replace(R.id.main_container, new FragmentInicio()).commit();


        bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.menu_inicio) {
                cambioMenuInicio();
                //loadFragment(new FragmentInicio());
                cambiarFragmento(primerFragment);
            } else if (itemId == R.id.menu_biblia) {
                cambioMenuBiblia();
                cambiarFragmento(segundoFragment);
                //loadFragment(new FragmentBiblia());
            } else if (itemId == R.id.menu_planes) {
                cambioMenuPlanes();
                cambiarFragmento(tercerFragment);
                //loadFragment(new FragmentPlanes());

            }
            else if (itemId == R.id.menu_mas) {
                cambioMenuMas();
                cambiarFragmento(cuartoFragment);
                //loadFragment(new FragmentMas());
            }

            return true;
        });

        // Agrega el primer fragmento al contenedor
        getSupportFragmentManager().beginTransaction().add(R.id.main_container, primerFragment).commit();
    }


    private void cambiarFragmento(Fragment nuevoFragmento) {
        if (nuevoFragmento != fragmentActivo) {
            getSupportFragmentManager().beginTransaction().hide(fragmentActivo).show(nuevoFragmento).commit();
            fragmentActivo = nuevoFragmento;
        }
    }


    private void recargarFragmentAjustes(){
        loadFragment(new FragmentMas());
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



    @Override
    public void onFragmentInteraction(int tipoTema) {
        if(tipoTema == 1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        recargarFragmentAjustes();
    }






}