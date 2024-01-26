package com.tatanstudios.abba.modelos.inicio;

import com.tatanstudios.abba.modelos.inicio.bloques.comparteapp.ModeloInicioComparteApp;
import com.tatanstudios.abba.modelos.inicio.bloques.imagenes.ModeloInicioImagenes;
import com.tatanstudios.abba.modelos.inicio.bloques.insignias.ModeloInicioInsignias;
import com.tatanstudios.abba.modelos.inicio.bloques.versiculos.ModeloInicioDevocional;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;

import java.util.List;

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
    private List<ModeloInicioVideos> modeloInicioVideos;

    private List<ModeloInicioImagenes> modeloInicioImagenes;
    private ModeloInicioComparteApp modeloInicioComparteApp;
    private List<ModeloInicioInsignias> modeloInicioInsignias;


    public ModeloVistasInicio(int tipoVista, ModeloInicioDevocional modeloInicioDevocional,
                              List<ModeloInicioVideos> modeloInicioVideos,
                              List<ModeloInicioImagenes> modeloInicioImagenes,
                              ModeloInicioComparteApp modeloInicioComparteApp,
                              List<ModeloInicioInsignias> modeloInicioInsignias
    ) {
        this.tipoVista = tipoVista;
        this.modeloInicioDevocional = modeloInicioDevocional;
        this.modeloInicioVideos = modeloInicioVideos;
        this.modeloInicioImagenes = modeloInicioImagenes;
        this.modeloInicioComparteApp = modeloInicioComparteApp;
        this.modeloInicioInsignias = modeloInicioInsignias;
    }

    public int getTipoVista() {
        return tipoVista;
    }


    public List<ModeloInicioVideos> getModeloInicioVideos() {
        return modeloInicioVideos;
    }

    public ModeloInicioDevocional getModeloInicioDevocional() {
        return modeloInicioDevocional;
    }


    public List<ModeloInicioImagenes> getModeloInicioImagenes() {
        return modeloInicioImagenes;
    }


    public ModeloInicioComparteApp getModeloInicioComparteApp() {
        return modeloInicioComparteApp;
    }

    public List<ModeloInicioInsignias> getModeloInicioInsignias() {
        return modeloInicioInsignias;
    }
}
