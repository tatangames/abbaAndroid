package com.tatanstudios.abba.modelos.misplanes;

import com.tatanstudios.abba.modelos.planes.ModeloPlanes;

public class ModeloVistasPlanesbloques {

    // ELECCION ENTRE TIPO DE VISTAS PARA ACTIVITY PlanesContenedorActivity.class

    public int tipoVista;

    // Esta vista es cuando tenemos el ultimo plan para poder continuarlo
    public static final int TIPO_PORTADA = 0;
    public static final int TIPO_RECYCLER_BLOQUE_HORIZONTAL = 1;
    public static final int TIPO_RECYCLER_BLOQUE_VERTICAL = 2;


    public ModeloMisPlanesPortada modeloMisPlanesPortada;
    public ModeloMisPlanesReHorizontal modeloMisPlanesReHorizontal;
  //  public ModeloMisPlanesReVertical modeloMisPlanesReVertical;

    public ModeloVistasPlanesbloques(int tipoVista, ModeloMisPlanesPortada modeloMisPlanesPortada,
                                     ModeloMisPlanesReHorizontal modeloMisPlanesReHorizontal

    ) {
        this.tipoVista = tipoVista;
        this.modeloMisPlanesPortada = modeloMisPlanesPortada;
        this.modeloMisPlanesReHorizontal = modeloMisPlanesReHorizontal;

    }

    public int getTipoVista() {
        return tipoVista;
    }

    public ModeloMisPlanesPortada getModeloMisPlanesPortada() {
        return modeloMisPlanesPortada;
    }

    public ModeloMisPlanesReHorizontal getModeloMisPlanesReHorizontal() {
        return modeloMisPlanesReHorizontal;
    }

   /* public ModeloMisPlanesReVertical getModeloMisPlanesReVertical() {
        return modeloMisPlanesReVertical;
    }*/

    public void setTipoVista(int tipoVista) {
        this.tipoVista = tipoVista;
    }
}

