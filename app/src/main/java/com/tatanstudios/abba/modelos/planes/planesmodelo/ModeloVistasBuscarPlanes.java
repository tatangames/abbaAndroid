package com.tatanstudios.abba.modelos.planes.planesmodelo;

import com.tatanstudios.abba.modelos.mas.ModeloFraMasConfig;
import com.tatanstudios.abba.modelos.mas.ModeloFraMasPerfil;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesTitulo;

public class ModeloVistasBuscarPlanes {

    // ELECCION ENTRE TIPO DE VISTAS PARA FRAGMENT BUSCAR NUEVOS PLANES
    // 1- VISTA PARA EL TITULO
    // 2- AGREGA LOS PLANES (img, titulo, subtitulo, barra progreso)

    public int tipoVista;

    public static final int TIPO_TITULO = 0;
    public static final int TIPO_PLANES = 1;

    public ModeloPlanesTitulo modeloPlanesTitulo;
    public ModeloPlanes modeloPlanes;

    public ModeloVistasBuscarPlanes(int tipoVista,
                                    ModeloPlanesTitulo modeloPlanesTitulo,
                                    ModeloPlanes modeloPlanes

    ) {
        this.tipoVista = tipoVista;
        this.modeloPlanesTitulo = modeloPlanesTitulo;
        this.modeloPlanes = modeloPlanes;
    }

    public int getTipoVista() {
        return tipoVista;
    }

    public ModeloPlanesTitulo getModeloPlanesTitulo() {
        return modeloPlanesTitulo;
    }

    public ModeloPlanes getModeloPlanes() {
        return modeloPlanes;
    }

    public void setTipoVista(int tipoVista) {
        this.tipoVista = tipoVista;
    }
}

