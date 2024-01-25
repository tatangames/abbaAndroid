package com.tatanstudios.abba.modelos.inicio.bloques.comparteapp;

import com.google.gson.annotations.SerializedName;

public class ModeloInicioComparteApp {

    private String imagen;
    private String titulo;
    private String descripcion;


    public ModeloInicioComparteApp(String imagen, String titulo, String descripcion) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }


    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
