package com.tatanstudios.abba.modelos.planes;

public class ModeloBotoneraPlanes {

    public int id;
    public String texto;

    public ModeloBotoneraPlanes(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
