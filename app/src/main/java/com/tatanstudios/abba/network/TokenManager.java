package com.tatanstudios.abba.network;

import android.content.SharedPreferences;

import com.tatanstudios.abba.modelos.usuario.ModeloUsuario;

public class TokenManager {


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static TokenManager INSTANCE = null;

    private TokenManager(SharedPreferences prefs) {
        this.prefs = prefs;
        this.editor = prefs.edit();
    }


    public static synchronized TokenManager getInstance(SharedPreferences prefs) {
        if (INSTANCE == null) {
            INSTANCE = new TokenManager(prefs);
        }
        return INSTANCE;
    }


    // guardar Id de cliente
    public void guardarClienteID(ModeloUsuario token) {
        editor.putString("ID", token.getId()).commit();
    }











    // borrar datos guardados
    public void deletePreferences(){
        editor.remove("ID").commit();
    }




    public ModeloUsuario getToken(){
        ModeloUsuario token = new ModeloUsuario();
        token.setId(prefs.getString("ID", null));
        return token;
    }

}


