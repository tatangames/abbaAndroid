package com.tatanstudios.abba.modelos.planes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloPlanesTitulo {

    @SerializedName("id")
    public int id;

    @SerializedName("titulo")
    public String titulo;

    @SerializedName("detalle")
    public List<ModeloPlanes>  modeloPlanes;


    public ModeloPlanesTitulo(int id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public List<ModeloPlanes> getModeloPlanes() {
        return modeloPlanes;
    }
}
