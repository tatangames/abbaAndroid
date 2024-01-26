package com.tatanstudios.abba.adaptadores.inicio;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorHorizontal;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorPlanBloque;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorPreguntas;
import com.tatanstudios.abba.fragmentos.inicio.tabs.FragmentTabInicio;
import com.tatanstudios.abba.fragmentos.planes.bloques.ItemModel;
import com.tatanstudios.abba.fragmentos.planes.bloques.SubItemModel;
import com.tatanstudios.abba.fragmentos.planes.cuestionario.FragmentPreguntasPlanBloque;
import com.tatanstudios.abba.modelos.inicio.ModeloVistasInicio;
import com.tatanstudios.abba.modelos.inicio.bloques.imagenes.ModeloInicioImagenes;
import com.tatanstudios.abba.modelos.inicio.bloques.insignias.ModeloInicioInsignias;
import com.tatanstudios.abba.modelos.inicio.bloques.versiculos.ModeloInicioDevocional;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;
import com.tatanstudios.abba.modelos.misplanes.preguntas.ModeloPreguntas;
import com.tatanstudios.abba.modelos.misplanes.preguntas.ModeloVistasPreguntas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorInicio extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ModeloVistasInicio> modeloVistasInicios;
    private Context context;
    private FragmentTabInicio fragmentTabInicio;
    private boolean tema;
    private boolean menuAbierto = false;
    public AdaptadorInicio(Context context, List<ModeloVistasInicio> modeloVistasInicios, FragmentTabInicio fragmentTabInicio,
                           boolean tema) {
        this.context = context;
        this.modeloVistasInicios = modeloVistasInicios;
        this.fragmentTabInicio = fragmentTabInicio;
        this.tema = tema;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;

        switch (viewType) {
            case ModeloVistasInicio.TIPO_DEVOCIONAL:
                itemView = inflater.inflate(R.layout.cardview_inicio_devocional, parent, false);
                return new AdaptadorInicio.DevocionalViewHolder(itemView);
            case ModeloVistasInicio.TIPO_VIDEOS:
                itemView = inflater.inflate(R.layout.cardview_inicio_videos_recycler, parent, false);
                return new AdaptadorInicio.RecyclerVideoViewHolder(itemView);

            case ModeloVistasInicio.TIPO_IMAGENES:
                itemView = inflater.inflate(R.layout.cardview_inicio_imagenes_recycler, parent, false);
                return new AdaptadorInicio.RecyclerImagenesViewHolder(itemView);

            case ModeloVistasInicio.TIPO_COMPARTEAPP:
                itemView = inflater.inflate(R.layout.cardview_inicio_comparteapp, parent, false);
                return new AdaptadorInicio.ComparteAppViewHolder(itemView);

            case ModeloVistasInicio.TIPO_INSIGNIAS:
                itemView = inflater.inflate(R.layout.cardview_inicio_insignias_recycler, parent, false);
                return new AdaptadorInicio.RecyclerInsigniasViewHolder(itemView);

            default:
                throw new IllegalArgumentException("Tipo de vista desconocido");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ModeloVistasInicio modeloVistasInicio = modeloVistasInicios.get(position);

        switch (modeloVistasInicio.getTipoVista()) {
            case ModeloVistasInicio.TIPO_DEVOCIONAL:

                ModeloInicioDevocional m = modeloVistasInicio.getModeloInicioDevocional();

                AdaptadorInicio.DevocionalViewHolder viewHolderDevocional = (AdaptadorInicio.DevocionalViewHolder) holder;

                String textoCortado = obtenerTextoCortado(m.getDevocuestionario(), 400);
                viewHolderDevocional.txtDevocional.setText(HtmlCompat.fromHtml(textoCortado, HtmlCompat.FROM_HTML_MODE_LEGACY));


                viewHolderDevocional.imgCompartir.setOnClickListener(v -> {
                    compartirTexto(HtmlCompat.fromHtml(textoCortado, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                });

                viewHolderDevocional.imgOpciones.setOnClickListener(v -> {

                    if (!menuAbierto) {
                        // Marcar que el menú está abierto
                        menuAbierto = true;

                        // Crea un PopupMenu
                        PopupMenu popupMenu = new PopupMenu(context, viewHolderDevocional.imgOpciones);

                        // Infla el menú en el PopupMenu
                        popupMenu.inflate(R.menu.menu_opciones_devocionales);

                        // Establece un listener para manejar los clics en los elementos del menú
                        popupMenu.setOnMenuItemClickListener(item -> {
                            // Marcar que el menú está cerrado
                            menuAbierto = false;

                            if (item.getItemId() == R.id.opcion1) {
                                // Maneja la selección de la opción 1
                                return true;
                            } else if (item.getItemId() == R.id.opcion2) {
                                // Maneja la selección de la opción 2
                                return true;
                            } else if (item.getItemId() == R.id.opcion3) {
                                // Maneja la selección de la opción 3
                                return true;
                            } else {
                                return false;
                            }
                        });

                        // Agrega un listener para detectar cuando se cierra el menú
                        popupMenu.setOnDismissListener(menu -> {
                            // Marcar que el menú está cerrado
                            menuAbierto = false;
                        });

                        // Muestra el menú emergente
                        popupMenu.show();
                    }
                });







                break;
            case ModeloVistasInicio.TIPO_VIDEOS:
                AdaptadorInicio.RecyclerVideoViewHolder viewHolderVideo = (AdaptadorInicio.RecyclerVideoViewHolder) holder;
                configurarRecyclerVideos(viewHolderVideo.recyclerViewVideos, modeloVistasInicio.getModeloInicioVideos());
                break;

            case ModeloVistasInicio.TIPO_IMAGENES:
                AdaptadorInicio.RecyclerImagenesViewHolder viewHolderImagenes = (AdaptadorInicio.RecyclerImagenesViewHolder) holder;
                configurarRecyclerImagenes(viewHolderImagenes.recyclerViewImagenes, modeloVistasInicio.getModeloInicioImagenes());
                break;
            case ModeloVistasInicio.TIPO_COMPARTEAPP:

                AdaptadorInicio.ComparteAppViewHolder viewHolderComparteApp = (AdaptadorInicio.ComparteAppViewHolder) holder;
                viewHolderComparteApp.txtTitulo.setText("hoho");
                break;

            case ModeloVistasInicio.TIPO_INSIGNIAS:

                AdaptadorInicio.RecyclerInsigniasViewHolder viewHolderInsignias = (AdaptadorInicio.RecyclerInsigniasViewHolder) holder;
                configurarRecyclerInsignias(viewHolderInsignias.recyclerViewInsignias, modeloVistasInicio.getModeloInicioInsignias());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return modeloVistasInicios.size();
    }

    @Override
    public int getItemViewType(int position) {
        return modeloVistasInicios.get(position).getTipoVista();
    }


    private void configurarRecyclerVideos(RecyclerView recyclerView, List<ModeloInicioVideos> modeloInicioVideos) {

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerVideos(modeloInicioVideos);
        recyclerView.setAdapter(adaptadorInterno);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }


    private void configurarRecyclerImagenes(RecyclerView recyclerView, List<ModeloInicioImagenes> modeloInicioImagenes) {

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerImagenes(modeloInicioImagenes);
        recyclerView.setAdapter(adaptadorInterno);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }



    private void configurarRecyclerInsignias(RecyclerView recyclerView, List<ModeloInicioInsignias> modeloInicioInsignias) {

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerInsignias(modeloInicioInsignias);
        recyclerView.setAdapter(adaptadorInterno);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }



    // BLOQUE DEVOCIONAL
    private static class DevocionalViewHolder extends RecyclerView.ViewHolder {

        private TextView txtDevocional;
        private ImageView imgCompartir;
        private ImageView imgOpciones;

        DevocionalViewHolder(View itemView) {
            super(itemView);
            txtDevocional = itemView.findViewById(R.id.txtDevocional);
            imgCompartir = itemView.findViewById(R.id.imgShare);
            imgOpciones = itemView.findViewById(R.id.imgOpciones);


        }
    }




    // BLOQUE VIDEOS
    private static class RecyclerVideoViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewVideos;

        RecyclerVideoViewHolder(View itemView) {
            super(itemView);
            recyclerViewVideos = itemView.findViewById(R.id.recyclerViewVideos);
        }
    }


    // BLOQUE IMAGENES
    private static class RecyclerImagenesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewImagenes;

        RecyclerImagenesViewHolder(View itemView) {
            super(itemView);
            recyclerViewImagenes = itemView.findViewById(R.id.recyclerViewImagenes);
        }
    }


    // BLOQUE COMPARTE APP
    private static class ComparteAppViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPortada;
        private TextView txtTitulo;
        private TextView txtDescripcion;

        ComparteAppViewHolder(View itemView) {
            super(itemView);
            imgPortada = itemView.findViewById(R.id.iconImageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
        }
    }


    // BLOQUE INSIGNIAS
    private static class RecyclerInsigniasViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewInsignias;

        RecyclerInsigniasViewHolder(View itemView) {
            super(itemView);
            recyclerViewInsignias = itemView.findViewById(R.id.recyclerViewInsignias);
        }
    }


    private String obtenerTextoCortado(String texto, int maxCaracteres) {
        // Elimina las etiquetas HTML del texto para contar solo los caracteres visibles
        String textoSinHTML =  HtmlCompat.fromHtml(texto, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();


        // Limita el texto a la longitud máxima
        if (textoSinHTML.length() > maxCaracteres) {
            textoSinHTML = textoSinHTML.substring(0, maxCaracteres) + "...";
        }

        return textoSinHTML;
    }


    private void compartirTexto(String texto) {
        // Crea un Intent para compartir texto
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, texto);

        // Inicia el Intent para mostrar el diálogo de compartir
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.compartir_con)));
    }

}