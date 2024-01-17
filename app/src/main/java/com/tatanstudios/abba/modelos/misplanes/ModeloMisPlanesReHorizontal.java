package com.tatanstudios.abba.modelos.misplanes;

import com.google.gson.annotations.SerializedName;

public class ModeloMisPlanesReHorizontal {

    private int id;
    private int visible;
    private int esperar_fecha;
    private String abreviatura;
    private int contador;

    private int mismodia;


    public ModeloMisPlanesReHorizontal(int id, int visible, int esperar_fecha, String abreviatura, int contador, int mismodia) {
        this.id = id;
        this.visible = visible;
        this.esperar_fecha = esperar_fecha;
        this.abreviatura = abreviatura;
        this.contador = contador;
        this.mismodia = mismodia;
    }

    public int getId() {
        return id;
    }

    public int getVisible() {
        return visible;
    }

    public int getEsperar_fecha() {
        return esperar_fecha;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public int getContador() {
        return contador;
    }

    public int getMismodia() {
        return mismodia;
    }
}
