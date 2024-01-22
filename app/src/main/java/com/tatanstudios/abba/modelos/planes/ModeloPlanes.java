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

    @SerializedName("imagenportada")
    public String imagenportada;



    @SerializedName("titulo")
    public String titulo;

    @SerializedName("subtitulo")
    public String subtitulo;

    @SerializedName("descripcion")
    public String descripcion;

    @SerializedName("idplan")
    public int idplan;

    @SerializedName("contador")
    public int contador;


    // porque tabla planes_usuarios tiene el campo id_planes
    @SerializedName("id_planes")
    public int id_Planes;

    public int getId_Planes() {
        return id_Planes;
    }

    public int getContador() {
        return contador;
    }

    public ModeloPlanes(Integer id, String imagen, String titulo, String subtitulo) {
        this.id = id;
        this.imagen = imagen;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    public int getIdplan() {
        return idplan;
    }

    public String getImagenportada() {
        return imagenportada;
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

}
