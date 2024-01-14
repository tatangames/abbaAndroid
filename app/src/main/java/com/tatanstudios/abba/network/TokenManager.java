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

    public void guardarClienteTOKEN(ModeloUsuario token) {
        editor.putString("TOKEN", token.getToken()).commit();
    }

    public void guardarEstiloTema(int code) {
        editor.putInt("TEMA", code).commit();
    }

    public void guardarIdioma(int code) {
        editor.putInt("IDIOMA", code).commit();
    }





    // borrar datos guardados
    public void deletePreferences(){
        editor.remove("ID").commit();
        editor.remove("TEMA").commit();
        editor.remove("TOKEN").commit();
    }




    public ModeloUsuario getToken(){
        ModeloUsuario token = new ModeloUsuario();
        token.setId(prefs.getString("ID", ""));
        token.setTema(prefs.getInt("TEMA", 0));
        token.setIdioma(prefs.getInt("IDIOMA", 0));
        token.setToken(prefs.getString("TOKEN", ""));
        return token;
    }

}


