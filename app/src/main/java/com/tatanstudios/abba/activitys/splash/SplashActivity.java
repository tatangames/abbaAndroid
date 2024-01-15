package com.tatanstudios.abba.activitys.splash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.Prueba2Activity;
import com.tatanstudios.abba.activitys.PruebaActivity;
import com.tatanstudios.abba.activitys.login.LoginActivity;
import com.tatanstudios.abba.activitys.principal.PrincipalActivity;
import com.tatanstudios.abba.extras.LocaleManagerExtras;
import com.tatanstudios.abba.network.TokenManager;

public class SplashActivity extends AppCompatActivity {


    private final int SPLASH_DISPLAY_LENGTH = 3000;
    TokenManager tokenManager;


    private static final String APP_INGLES = "en";
    private static final String APP_ESPANOL = "es";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken().getIdiomaApp() == 0){
            LocaleManagerExtras.setLocale(this, APP_ESPANOL);
        }
        else if(tokenManager.getToken().getIdiomaApp() == 1){
            LocaleManagerExtras.setLocale(this, APP_INGLES);
        }
        else{
            LocaleManagerExtras.setLocale(this, APP_ESPANOL);
        }

        new Handler().postDelayed(() -> {

            // inicio automatico con token que iria en el SPLASH
            if(!TextUtils.isEmpty(tokenManager.getToken().getId())){

                // Siguiente Actvity
                Intent intent = new Intent(this, PrincipalActivity.class);
                startActivity(intent);

                // Animación personalizada de entrada
                overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
                finish();

            }else {
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }




}