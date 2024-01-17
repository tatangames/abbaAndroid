package com.tatanstudios.abba.modelos.misplanes;

import com.google.gson.annotations.SerializedName;

public class ModeloMisPlanesReVertical {

    private int id;

    private int completado;
    private String titulo;

    public ModeloMisPlanesReVertical(int id, int completado, String titulo) {
        this.id = id;
        this.completado = completado;
        this.titulo = titulo;
    }

    public int getCompletado() {
        return completado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
