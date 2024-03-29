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

    public void guardarIdiomaApp(int code) {
        editor.putInt("IDIOMAAPP", code).commit();
    }

    public void guardarIdiomaTexto(int code) {
        editor.putInt("IDIOMATEXTO", code).commit();
    }

    public void guardarTipoLetraTexto(int code) {
        editor.putInt("TIPOTEXTO", code).commit();
    }


    // si el idioma ya fue cambiado, para no preguntar idioma del telefono
    public void guardarIdiomaTelefono(int code) {
        editor.putInt("IDIOMACEL", code).commit();
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
        token.setIdiomaApp(prefs.getInt("IDIOMAAPP", 0));
        token.setIdiomaTextos(prefs.getInt("IDIOMATEXTO", 0));
        token.setToken(prefs.getString("TOKEN", ""));
        token.setTipoLetra(prefs.getInt("TIPOTEXTO", 0));
        token.setIdiomaCel(prefs.getInt("IDIOMACEL", 0));

        return token;
    }

}


