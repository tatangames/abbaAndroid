package com.tatanstudios.abba.modelos.mas;

public class ModeloFraMasConfig {

    public String nombre;
    public int identificador;


    public ModeloFraMasConfig(int identificador, String nombre) {
        this.identificador = identificador;
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }
}
