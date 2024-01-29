package com.tatanstudios.abba.modelos.comunidad;

import com.tatanstudios.abba.modelos.insignias.ModeloDescripcionHitos;
import com.tatanstudios.abba.modelos.insignias.ModeloInsigniaHitos;
import com.tatanstudios.abba.modelos.planes.ModeloBotoneraPlanes;

import java.util.List;

public class ModeloVistaComunidad {

    public int tipoVista;

    public static final int TIPO_BOTONERA = 0;

    public static final int TIPO_RECYCLER = 1;


    private ModeloBotoneraPlanes modeloBotonera;

    private List<ModeloComunidad> modeloComunidad;


    public ModeloVistaComunidad(int tipoVista, ModeloBotoneraPlanes modeloBotonera,
                            List<ModeloComunidad> modeloComunidad

    ) {
        this.tipoVista = tipoVista;
        this.modeloBotonera = modeloBotonera;
        this.modeloComunidad = modeloComunidad;
    }

    public int getTipoVista() {
        return tipoVista;
    }

    public ModeloBotoneraPlanes getModeloBotonera() {
        return modeloBotonera;
    }

    public List<ModeloComunidad> getModeloComunidad() {
        return modeloComunidad;
    }
}
