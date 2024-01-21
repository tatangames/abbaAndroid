package com.tatanstudios.abba.modelos.misplanes.preguntas;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloqueDetalle;

import java.util.List;

public class ModeloPreguntasContenedor {

    @SerializedName("success")
    private int success;

    @SerializedName("titulop")
    private String titulop;

    @SerializedName("descripcionp")
    private String descripcionp;

    @SerializedName("hayrespuesta")
    private int hayRespuesta;

    @SerializedName("listado")
    public List<ModeloPreguntas> modeloPreguntas;


    public String getDescripcionp() {
        return descripcionp;
    }

    public int getHayRespuesta() {
        return hayRespuesta;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getTitulop() {
        return titulop;
    }

    public void setTitulop(String titulop) {
        this.titulop = titulop;
    }

    public List<ModeloPreguntas> getModeloPreguntas() {
        return modeloPreguntas;
    }

    public void setModeloPreguntas(List<ModeloPreguntas> modeloPreguntas) {
        this.modeloPreguntas = modeloPreguntas;
    }
}
