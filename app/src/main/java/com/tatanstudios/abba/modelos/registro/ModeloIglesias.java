package com.tatanstudios.abba.modelos.registro;

public class ModeloIglesias {

    private int id;
    private String nombre;

    public ModeloIglesias(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
