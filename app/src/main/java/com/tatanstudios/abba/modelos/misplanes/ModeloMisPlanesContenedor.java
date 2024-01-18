package com.tatanstudios.abba.modelos.misplanes;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesTitulo;

import java.util.List;

public class ModeloMisPlanesContenedor {

    @SerializedName("success")
    private int success;

    @SerializedName("portada")
    private String imagenPortada;

    @SerializedName("haydiaactual")
    private int hayDiaActual;

    @SerializedName("idultimobloque")
    private int idUltimoBloque;

    @SerializedName("listado")
    public List<ModeloMisPlanesBloques> modeloMisPlanesBloques;


    public int getHayDiaActual() {
        return hayDiaActual;
    }

    public int getIdUltimoBloque() {
        return idUltimoBloque;
    }

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
