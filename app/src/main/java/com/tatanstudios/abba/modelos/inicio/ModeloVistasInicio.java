package com.tatanstudios.abba.modelos.inicio;

import com.tatanstudios.abba.modelos.inicio.bloques.comparteapp.ModeloInicioComparteApp;
import com.tatanstudios.abba.modelos.inicio.bloques.imagenes.ModeloInicioImagenes;
import com.tatanstudios.abba.modelos.inicio.bloques.insignias.ModeloInicioInsignias;
import com.tatanstudios.abba.modelos.inicio.bloques.versiculos.ModeloInicioDevocional;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;

public class ModeloVistasInicio {

    // PARA ELEGIR TIPO DE VISTA PARA INICIO

    public int tipoVista;

    // Esta vista es cuando tenemos el ultimo plan para poder continuarlo
    public static final int TIPO_DEVOCIONAL = 0;

    public static final int TIPO_VIDEOS = 1;

    public static final int TIPO_IMAGENES = 2;

    public static final int TIPO_COMPARTEAPP = 3;

    public static final int TIPO_INSIGNIAS = 4;


    private ModeloInicioDevocional modeloInicioDevocional;
    private ModeloInicioVideos modeloInicioVideos;

    private ModeloInicioImagenes modeloInicioImagenes;
    private ModeloInicioComparteApp ModeloInicioComparteApp;
    private ModeloInicioInsignias modeloInicioInsignias;


    public ModeloVistasInicio(int tipoVista, ModeloInicioDevocional modeloInicioDevocional,
                              ModeloInicioVideos modeloInicioVideos,
                              ModeloInicioImagenes modeloInicioImagenes,
                              ModeloInicioComparteApp ModeloInicioComparteApp,
                              ModeloInicioInsignias modeloInicioInsignias
    ) {
        this.tipoVista = tipoVista;
        this.modeloInicioDevocional = modeloInicioDevocional;
        this.modeloInicioVideos = modeloInicioVideos;
        this.modeloInicioImagenes = modeloInicioImagenes;
        this.ModeloInicioComparteApp = ModeloInicioComparteApp;
        this.modeloInicioInsignias = modeloInicioInsignias;
    }

    public int getTipoVista() {
        return tipoVista;
    }





    public ModeloInicioDevocional getModeloInicioDevocional() {
        return modeloInicioDevocional;
    }

    public ModeloInicioVideos getModeloInicioVideos() {
        return modeloInicioVideos;
    }

    public ModeloInicioImagenes getModeloInicioImagenes() {
        return modeloInicioImagenes;
    }

    public com.tatanstudios.abba.modelos.inicio.bloques.comparteapp.ModeloInicioComparteApp getModeloInicioComparteApp() {
        return ModeloInicioComparteApp;
    }

    public ModeloInicioInsignias getModeloInicioInsignias() {
        return modeloInicioInsignias;
    }
}
