package com.tatanstudios.abba.modelos.misplanes;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesTitulo;

import java.util.List;

public class ModeloMisPlanesContenedor {

    @SerializedName("success")
    private int success;

    @SerializedName("portada")
    private String imagenPortada;


    @SerializedName("listado")
    public List<ModeloMisPlanesBloques> modeloMisPlanesBloques;






    public int getSuccess() {
        return success;
    }

    public String getImagenPortada() {
        return imagenPortada;
    }

    public List<ModeloMisPlanesBloques> getModeloMisPlanesBloques() {
        return modeloMisPlanesBloques;
    }
}
