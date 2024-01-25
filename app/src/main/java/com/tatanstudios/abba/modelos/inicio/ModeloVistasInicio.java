package com.tatanstudios.abba.modelos.inicio;

import com.tatanstudios.abba.modelos.inicio.bloques.versiculos.ModeloVersiculo;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloVideos;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesPortada;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesReHorizontal;

public class ModeloVistasInicio {

    // PARA ELEGIR TIPO DE VISTA PARA INICIO

    public int tipoVista;

    // Esta vista es cuando tenemos el ultimo plan para poder continuarlo
    public static final int TIPO_VERSICULO = 0;
    public static final int TIPO_VIDEOS = 1;


    private ModeloVersiculo modeloVersiculo;
    private ModeloVideos modeloVideos;


    public ModeloVistasInicio(int tipoVista, ModeloVersiculo modeloVersiculo, ModeloVideos modeloVideos

    ) {
        this.tipoVista = tipoVista;
        this.modeloVersiculo = modeloVersiculo;
        this.modeloVideos = modeloVideos;

    }

    public int getTipoVista() {
        return tipoVista;
    }


    public ModeloVersiculo getModeloVersiculo() {
        return modeloVersiculo;
    }

    public ModeloVideos getModeloVideos() {
        return modeloVideos;
    }
}
