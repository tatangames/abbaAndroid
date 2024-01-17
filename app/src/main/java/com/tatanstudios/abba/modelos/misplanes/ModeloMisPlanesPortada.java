package com.tatanstudios.abba.modelos.misplanes;

import com.google.gson.annotations.SerializedName;

public class ModeloMisPlanesPortada {

    @SerializedName("imagen")
    private String imagen;

    public ModeloMisPlanesPortada(String imagen) {
        this.imagen = imagen;
    }


    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
