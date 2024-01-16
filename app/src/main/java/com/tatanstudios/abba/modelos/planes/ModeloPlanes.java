package com.tatanstudios.abba.modelos.planes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ModeloPlanes {

    @SerializedName("success")
    public Integer success;

    @SerializedName("id")
    public Integer id;

    @SerializedName("imagen")
    public String imagen;

    @SerializedName("barra_progreso")
    public Integer barraProgreso;

    @SerializedName("titulo")
    public String titulo;

    @SerializedName("subtitulo")
    public String subtitulo;

    @SerializedName("descripcion")
    public String descripcion;


    public ModeloPlanes(Integer id, String imagen, Integer barraProgreso, String titulo, String subtitulo) {
        this.id = id;
        this.imagen = imagen;
        this.barraProgreso = barraProgreso;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }


    public String getDescripcion() {
        return descripcion;
    }

    public Integer getSuccess() {
        return success;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public Integer getId() {
        return id;
    }

    public String getImagen() {
        return imagen;
    }

    public Integer getBarraProgreso() {
        return barraProgreso;
    }
}
