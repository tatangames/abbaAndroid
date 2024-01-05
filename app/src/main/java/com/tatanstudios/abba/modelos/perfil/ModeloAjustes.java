package com.tatanstudios.abba.modelos.perfil;

import com.google.gson.annotations.SerializedName;

public class ModeloAjustes {

    @SerializedName("success")
    public int success;

    @SerializedName("letra")
    public String letra;

    @SerializedName("nombre")
    public String nombre;


    public int getSuccess() {
        return success;
    }

    public String getLetra() {
        return letra;
    }

    public String getNombre() {
        return nombre;
    }
}
