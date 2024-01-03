package com.tatanstudios.abba.modelos.mas;

public class ModeloFraMasPerfil {

    public String letra;
    public String nombrePerfil;


    public ModeloFraMasPerfil(String letra, String nombrePerfil) {
        this.letra = letra;
        this.nombrePerfil = nombrePerfil;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getNombrePerfil() {
        return nombrePerfil;
    }

    public void setNombrePerfil(String nombrePerfil) {
        this.nombrePerfil = nombrePerfil;
    }
}
