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
