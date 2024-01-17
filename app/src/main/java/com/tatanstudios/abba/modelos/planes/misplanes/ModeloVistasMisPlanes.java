package com.tatanstudios.abba.modelos.planes.misplanes;

import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesTitulo;

public class ModeloVistasMisPlanes {

    // ELECCION ENTRE TIPO DE VISTAS PARA FRAGMENT MIS PLANES

    public int tipoVista;

    // Esta vista es cuando tenemos el ultimo plan para poder continuarlo
    public static final int TIPO_CONTINUAR = 0;
    public static final int TIPO_PLANES = 1;

    public ModeloPlanes modeloPlanes;

    public ModeloVistasMisPlanes(int tipoVista, ModeloPlanes modeloPlanes
    ) {
        this.tipoVista = tipoVista;
        this.modeloPlanes = modeloPlanes;
    }

    public int getTipoVista() {
        return tipoVista;
    }

    public ModeloPlanes getModeloPlanes() {
        return modeloPlanes;
    }

    public void setTipoVista(int tipoVista) {
        this.tipoVista = tipoVista;
    }
}

