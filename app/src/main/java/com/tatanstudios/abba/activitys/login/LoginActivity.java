package com.tatanstudios.abba.activitys.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.login.FragmentLogin;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContenedor, new FragmentLogin())
                .commit();



    }
}