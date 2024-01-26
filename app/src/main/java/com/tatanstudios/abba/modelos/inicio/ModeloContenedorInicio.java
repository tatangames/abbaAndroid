package com.tatanstudios.abba.modelos.inicio;

import com.google.gson.annotations.SerializedName;
import com.tatanstudios.abba.modelos.inicio.bloques.imagenes.ModeloInicioImagenes;
import com.tatanstudios.abba.modelos.inicio.bloques.insignias.ModeloInicioInsignias;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloqueDetalle;

import java.util.List;

public class ModeloContenedorInicio {


    @SerializedName("success")
    public int success;



    //*******************

    // ESTO LO DICE EL ADMINISTRADOR QUE SI SE MOSTRARA
    @SerializedName("mostrarbloquedevocional")
    public int mostrarbloquedevocional;

    @SerializedName("mostrarbloquevideo")
    public int mostrarbloquevideo;

    @SerializedName("mostrarbloqueimagenes")
    public int mostrarbloqueimagenes;

    @SerializedName("mostrarbloquecomparte")
    public int mostrarbloquecomparte;


    @SerializedName("mostrarbloqueinsignias")
    public int mostrarbloqueinsignias;



    //******************


    @SerializedName("devohaydevocional")
    private int devohaydevocional;

    @SerializedName("devocuestionario")
    private String devocuestionario;

    @SerializedName("devoidblockdeta")
    private int devoidblockdeta;


    // *********************

    @SerializedName("arrayfinalvideo")
    public List<ModeloInicioVideos> modeloInicioVideos;

    @SerializedName("videohayvideos")
    private int videohayvideos;


    // *********************


    @SerializedName("imageneshayhoy")
    private int imageneshayhoy;


    public int getImageneshayhoy() {
        return imageneshayhoy;
    }

    @SerializedName("arrayfinalimagenes")
    public List<ModeloInicioImagenes> modeloInicioImagenes;


    //**********************


    @SerializedName("comparteappimagen")
    private String comparteappimagen;


    @SerializedName("comparteapptitulo")
    private String comparteapptitulo;

    @SerializedName("comparteappdescrip")
    private String comparteappdescrip;



    //*********************

    @SerializedName("insigniashay")
    private int insigniashay;

    @SerializedName("arrayfinalinsignias")
    public List<ModeloInicioInsignias> modeloInicioInsignias;


    //********************

    @SerializedName("videomayor5")
    private int videomayor5;

    @SerializedName("imagenesmayor5")
    private int imagenesmayor5;

    @SerializedName("insigniasmayor5")
    private int insigniasmayor5;







    public int getVideomayor5() {
        return videomayor5;
    }

    public int getImagenesmayor5() {
        return imagenesmayor5;
    }

    public int getInsigniasmayor5() {
        return insigniasmayor5;
    }

    public List<ModeloInicioInsignias> getModeloInicioInsignias() {
        return modeloInicioInsignias;
    }

    public int getInsigniashay() {
        return insigniashay;
    }

    public String getComparteappimagen() {
        return comparteappimagen;
    }

    public String getComparteapptitulo() {
        return comparteapptitulo;
    }

    public String getComparteappdescrip() {
        return comparteappdescrip;
    }

    public List<ModeloInicioImagenes> getModeloInicioImagenes() {
        return modeloInicioImagenes;
    }

    public int getMostrarbloquedevocional() {
        return mostrarbloquedevocional;
    }

    public int getMostrarbloquevideo() {
        return mostrarbloquevideo;
    }

    public int getMostrarbloqueimagenes() {
        return mostrarbloqueimagenes;
    }

    public int getMostrarbloquecomparte() {
        return mostrarbloquecomparte;
    }

    public int getMostrarbloqueinsignias() {
        return mostrarbloqueinsignias;
    }

    public int getVideohayvideos() {
        return videohayvideos;
    }

    public List<ModeloInicioVideos> getModeloInicioVideos() {
        return modeloInicioVideos;
    }

    public int getDevohaydevocional() {
        return devohaydevocional;
    }

    public String getDevocuestionario() {
        return devocuestionario;
    }

    public int getDevoidblockdeta() {
        return devoidblockdeta;
    }



    public int getSuccess() {
        return success;
    }


}
