package com.tatanstudios.abba.modelos.inicio.bloques.separador;

public class ModeloInicioSeparador {

    private int id;

    private String nombre;

    private int hayMas;


    public ModeloInicioSeparador(int id, String nombre, int hayMas) {
        this.id = id;
        this.nombre = nombre;
        this.hayMas = hayMas;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getHayMas() {
        return hayMas;
    }
}
