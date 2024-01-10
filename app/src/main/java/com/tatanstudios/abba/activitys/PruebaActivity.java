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



        });



    }


}