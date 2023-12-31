package com.tatanstudios.abba.modelos.usuario;

import com.google.gson.annotations.SerializedName;

public class ModeloUsuario {

    @SerializedName("success")
    public int success;

    @SerializedName("id")
    public String id;
    @SerializedName("refran")
    public String refran;

    @SerializedName("salmo")
    public String salmo;

    @SerializedName("nombre")
    public String nombre;

    @SerializedName("apellido")
    public String apellido;

    @SerializedName("fecha_nacimiento")
    public String fechaNacimiento;

    @SerializedName("correo")
    public String correo;

    @SerializedName("fecha_nac_raw")
    public String fechaNacimientoRaw;

    public int tema;
    public int idioma; // 1: espanol, 2: ingles


    public int getIdioma() {
        return idioma;
    }

    public void setIdioma(int idioma) {
        this.idioma = idioma;
    }

    public int getTema() {
        return tema;
    }

    public void setTema(int tema) {
        this.tema = tema;
    }

    public String getFechaNacimientoRaw() {
        return fechaNacimientoRaw;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getCorreo() {
        return correo;
    }

    public int getSuccess() {
        return success;
    }

    public String getRefran() {
        return refran;
    }

    public String getSalmo() {
        return salmo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
