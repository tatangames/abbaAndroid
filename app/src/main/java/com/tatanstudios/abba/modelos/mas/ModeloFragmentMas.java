package com.tatanstudios.abba.modelos.mas;


public class ModeloFragmentMas {

    public int tipoVista;

    public static final int TIPO_PERFIL = 0;
    public static final int TIPO_ITEM_NORMAL = 1;
    public static final int TIPO_LINEA_SEPARACION = 2;

    public ModeloFraMasPerfil modeloFraMasPerfil;
    public ModeloFraMasConfig modeloFraMasConfig;

    public ModeloFragmentMas(int tipoVista,
                             ModeloFraMasPerfil modeloFraMasPerfil,
                             ModeloFraMasConfig modeloFraMasConfig

    ) {
        this.tipoVista = tipoVista;
        this.modeloFraMasPerfil = modeloFraMasPerfil;
        this.modeloFraMasConfig = modeloFraMasConfig;
    }


    public ModeloFraMasPerfil getModeloFraMasPerfil() {
        return modeloFraMasPerfil;
    }

    public ModeloFraMasConfig getModeloFraMasConfig() {
        return modeloFraMasConfig;
    }

    public int getTipoVista() {
        return tipoVista;
    }

    public void setTipoVista(int tipoVista) {
        this.tipoVista = tipoVista;
    }
}
