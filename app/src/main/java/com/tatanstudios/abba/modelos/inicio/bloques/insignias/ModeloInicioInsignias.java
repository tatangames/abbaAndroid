package com.tatanstudios.abba.modelos.inicio.bloques.insignias;

import com.google.gson.annotations.SerializedName;

public class ModeloInicioInsignias {

    @SerializedName("id")
    private int id;

    @SerializedName("id_tipo_insignia")
    private int idTipoInsignia;

    @SerializedName("id_usuario")
    private int idUsuario;


    @SerializedName("fecha")
    private String fecha;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("nivelvoy")
    private int nivelVoy;

    public ModeloInicioInsignias(int id, String titulo, String descripcion, int nivelVoy) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nivelVoy = nivelVoy;
    }

    public int getId() {
        return id;
    }

    public int getIdTipoInsignia() {
        return idTipoInsignia;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getNivelVoy() {
        return nivelVoy;
    }
}
