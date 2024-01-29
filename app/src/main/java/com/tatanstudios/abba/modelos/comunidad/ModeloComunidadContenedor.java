package com.tatanstudios.abba.modelos.comunidad;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abba.modelos.inicio.bloques.insignias.ModeloInicioInsignias;

import java.util.List;

public class ModeloComunidadContenedor {

    @SerializedName("success")
    private int success;

    @SerializedName("listado")
    public List<ModeloComunidad> modeloComunidad;


    public int getSuccess() {
        return success;
    }

    public List<ModeloComunidad> getModeloComunidad() {
        return modeloComunidad;
    }
}
