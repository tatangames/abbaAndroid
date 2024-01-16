package com.tatanstudios.abba.modelos.planes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloPlanesContenedor {

    @SerializedName("success")
    public int success;

    @SerializedName("hayinfo")
    public int hayinfo;

    @SerializedName("listado")
    public List<ModeloPlanesTitulo> modeloPlanesTitulos;

    @SerializedName("listaplanes")
    public List<ModeloPlanes> modeloPlanes;

    public int getSuccess() {
        return success;
    }

    public List<ModeloPlanesTitulo> getModeloPlanesTitulos() {
        return modeloPlanesTitulos;
    }

    public int getHayinfo() {
        return hayinfo;
    }

    public List<ModeloPlanes> getModeloPlanes() {
        return modeloPlanes;
    }
}
