package com.tatanstudios.abba.modelos.planes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloPlanesContenedor {

    @SerializedName("success")
    public int success;

    @SerializedName("listado")
    public List<ModeloPlanesTitulo> modeloPlanesTitulos;

    public int getSuccess() {
        return success;
    }

    public List<ModeloPlanesTitulo> getModeloPlanesTitulos() {
        return modeloPlanesTitulos;
    }
}
