package com.tatanstudios.abba.modelos.misplanes;

import com.google.gson.annotations.SerializedName;

public class ModeloMisPlanesBloqueDetalle {

    @SerializedName("id")
    private int id;

    @SerializedName("completado")
    private int completado;

    @SerializedName("posicion")
    private int posicion;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("tiene_preguntas")
    private int tienePreguntas;


    public ModeloMisPlanesBloqueDetalle(int id, int completado, int posicion, String titulo, int tienePreguntas) {
        this.id = id;
        this.completado = completado;
        this.posicion = posicion;
        this.titulo = titulo;
        this.tienePreguntas = tienePreguntas;
    }

    public int getTienePreguntas() {
        return tienePreguntas;
    }

    public void setCompletado(int completado) {
        this.completado = completado;
    }

    public int getId() {
        return id;
    }

    public int getCompletado() {
        return completado;
    }

    public int getPosicion() {
        return posicion;
    }

    public String getTitulo() {
        return titulo;
    }
}
