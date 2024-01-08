package com.tatanstudios.abba.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.network.TokenManager;

public class PruebaActivity extends AppCompatActivity {

    private Button btn;
    private TokenManager tokenManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

        btn = findViewById(R.id.button);
        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));


        btn.setOnClickListener(v -> {

            //tokenManager.guardarEstiloTema(0);3

            if(tokenManager.getToken().getTema() == 0){
                Toast.makeText(this, "Tema es: 0", Toast.LENGTH_SHORT).show();
            }
            else if(tokenManager.getToken().getTema() == 1){
                Toast.makeText(this, "Tema es: 1", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(this, "Tema es: 0", Toast.LENGTH_SHORT).show();
            }

            Intent info = new Intent(this, Prueba2Activity.class);
            startActivity(info);



        });



    }


    public boolean isDarkModeEnabled() {
        //  int nightMode = AppCompatDelegate.getDefaultNightMode();
        //  return nightMode == AppCompatDelegate.MODE_NIGHT_YES;
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        boolean valor;

        switch (currentNightMode){
            case Configuration.UI_MODE_NIGHT_NO:
                valor = false;
                break;

            case Configuration.UI_MODE_NIGHT_YES:
                valor = true;
                break;
            default:
                valor = false;
                break;
        }

        return valor;
    }
}