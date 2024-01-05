package com.tatanstudios.abba.activitys.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.login.FragmentLogin;
import com.tatanstudios.abba.fragmentos.notificaciones.FragmentVerNotificaciones;

public class VerNotificacionesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_notificaciones);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, new FragmentVerNotificaciones())
                .commit();
    }
}