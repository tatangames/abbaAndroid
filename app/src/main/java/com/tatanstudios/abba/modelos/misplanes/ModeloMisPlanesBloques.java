package com.tatanstudios.abba.modelos.misplanes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloMisPlanesBloques {


    @SerializedName("id")
    private int id;

    @SerializedName("visible")
    private int visible;

    @SerializedName("esperar_fecha")
    private int esperar_fecha;

    @SerializedName("abreviatura")
    private String abreviatura;

    @SerializedName("contador")
    private int contador;

    @SerializedName("mismodia")
    private int mismodia;

    @SerializedName("detalle")
    public List<ModeloMisPlanesBloqueDetalle> modeloMisPlanesBloqueDetalles;


    public int getId() {
        return id;
    }

    public int getVisible() {
        return visible;
    }

    public int getEsperar_fecha() {
        return esperar_fecha;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public int getContador() {
        return contador;
    }

    public int getMismodia() {
        return mismodia;
    }

    public List<ModeloMisPlanesBloqueDetalle> getModeloMisPlanesBloqueDetalles() {
        return modeloMisPlanesBloqueDetalles;
    }
}
