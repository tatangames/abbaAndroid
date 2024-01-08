package com.tatanstudios.abba.activitys.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.extras.OnFragmentInteractionTema;
import com.tatanstudios.abba.fragmentos.login.FragmentLogin;
import com.tatanstudios.abba.fragmentos.registro.FragmentRegistro;
import com.tatanstudios.abba.network.TokenManager;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity implements OnFragmentInteractionTema {

    private TokenManager tokenManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        //getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        //recreate();

        boolean info = isDarkModeEnabled();

        Toasty.info(this, "es.: " + info, Toasty.LENGTH_SHORT).show();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, new FragmentLogin())
                .commit();
    }

    @Override
    public boolean onFragmentInteraction() {
        // Implementa la lógica que deseas ejecutar desde el Fragment
        return false;
    }


    public boolean isDarkModeEnabled() {
      //  int nightMode = AppCompatDelegate.getDefaultNightMode();
      //  return nightMode == AppCompatDelegate.MODE_NIGHT_YES;

        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isNightModeEnabled = currentNightMode == Configuration.UI_MODE_NIGHT_YES;

        return isNightModeEnabled;
    }
}