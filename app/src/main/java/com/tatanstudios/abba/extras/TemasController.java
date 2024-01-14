package com.tatanstudios.abba.extras;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.tatanstudios.abba.network.TokenManager;

public class TemasController extends Application {

    TokenManager tokenManager;



    @Override
    public void onCreate() {
        super.onCreate();

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        if(tokenManager.getToken().getTema() == 0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        else if(tokenManager.getToken().getTema() == 1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }



}
