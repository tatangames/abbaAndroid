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

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorHorizontal;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorPlanBloque;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorPreguntas;
import com.tatanstudios.abba.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abba.fragmentos.inicio.tabs.FragmentTabInicio;
import com.tatanstudios.abba.fragmentos.planes.bloques.ItemModel;
import com.tatanstudios.abba.fragmentos.planes.bloques.SubItemModel;
import com.tatanstudios.abba.fragmentos.planes.cuestionario.FragmentPreguntasPlanBloque;
import com.tatanstudios.abba.modelos.inicio.ModeloVistasInicio;
import com.tatanstudios.abba.modelos.inicio.bloques.comparteapp.ModeloInicioComparteApp;
import com.tatanstudios.abba.modelos.inicio.bloques.imagenes.ModeloInicioImagenes;
import com.tatanstudios.abba.modelos.inicio.bloques.insignias.ModeloInicioInsignias;
import com.tatanstudios.abba.modelos.inicio.bloques.separador.ModeloInicioSeparador;
import com.tatanstudios.abba.modelos.inicio.bloques.versiculos.ModeloInicioDevocional;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;
import com.tatanstudios.abba.modelos.misplanes.preguntas.ModeloPreguntas;
import com.tatanstudios.abba.modelos.misplanes.preguntas.ModeloVistasPreguntas;
import com.tatanstudios.abba.network.RetrofitBuilder;

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
    private ModeloInicioSeparador modeloInicioSeparador;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorInicio(Context context, List<ModeloVistasInicio> modeloVistasInicios, FragmentTabInicio fragmentTabInicio,
                           boolean tema, ModeloInicioSeparador modeloInicioSeparador) {
        this.context = context;
        this.modeloVistasInicios = modeloVistasInicios;
        this.fragmentTabInicio = fragmentTabInicio;
        this.tema = tema;
        this.modeloInicioSeparador = modeloInicioSeparador;
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
        ModeloVistasInicio mVista = modeloVistasInicios.get(position);

        switch (mVista.getTipoVista()) {
            case ModeloVistasInicio.TIPO_DEVOCIONAL:

                ModeloInicioDevocional m = mVista.getModeloInicioDevocional();

                AdaptadorInicio.DevocionalViewHolder viewHolderDevocional = (AdaptadorInicio.DevocionalViewHolder) holder;

                String textoCortado = obtenerTextoCortado(m.getDevocuestionario(), 400);
                viewHolderDevocional.txtDevocional.setText(HtmlCompat.fromHtml(textoCortado, HtmlCompat.FROM_HTML_MODE_LEGACY));

                viewHolderDevocional.imgCompartir.setOnClickListener(v -> {
                    compartirTexto(HtmlCompat.fromHtml(m.getDevocuestionario(), HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
                });

                viewHolderDevocional.txtDevocional.setOnClickListener(v -> {
                    fragmentTabInicio.redireccionarCuestionario(m.getDevoidblockdeta());
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

                                // IR A CUESTIONARIO Y PREGUNTAS
                                fragmentTabInicio.redireccionarCuestionario(m.getDevoidblockdeta());

                                return true;
                            } else if (item.getItemId() == R.id.opcion2) {

                                // INFORMACION DEL PLAN
                                fragmentTabInicio.redireccionarInfoPlanVista(m.getDevoidblockdeta());


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
                viewHolderVideo.txtToolbar.setText(context.getString(R.string.videos));

                if(modeloInicioSeparador.getHayMasDe5Videos() == 1){
                    viewHolderVideo.imgFlechaDerecha.setVisibility(View.VISIBLE);
                }else{
                    viewHolderVideo.imgFlechaDerecha.setVisibility(View.GONE);
                }

                viewHolderVideo.imgFlechaDerecha.setOnClickListener(v -> {
                    if(modeloInicioSeparador.getHayMasDe5Videos() == 1){

                        fragmentTabInicio.vistaTodosLosVideos();
                    }
                });

                configurarRecyclerVideos(viewHolderVideo.recyclerViewVideos, mVista.getModeloInicioVideos());
                break;

            case ModeloVistasInicio.TIPO_IMAGENES:
                AdaptadorInicio.RecyclerImagenesViewHolder viewHolderImagenes = (AdaptadorInicio.RecyclerImagenesViewHolder) holder;

                viewHolderImagenes.txtToolbar.setText(context.getString(R.string.imagenes_del_dia));

                if(modeloInicioSeparador.getHayMasDe5Imagenes() == 1){
                    viewHolderImagenes.imgFlechaDerecha.setVisibility(View.VISIBLE);
                }else{
                    viewHolderImagenes.imgFlechaDerecha.setVisibility(View.GONE);
                }

                viewHolderImagenes.imgFlechaDerecha.setOnClickListener(v -> {
                    if(modeloInicioSeparador.getHayMasDe5Imagenes() == 1){
                        fragmentTabInicio.vistaTodosLasImagenes();
                    }
                });


                configurarRecyclerImagenes(viewHolderImagenes.recyclerViewImagenes, mVista.getModeloInicioImagenes());
                break;
            case ModeloVistasInicio.TIPO_COMPARTEAPP:
                AdaptadorInicio.ComparteAppViewHolder viewHolderComparteApp = (AdaptadorInicio.ComparteAppViewHolder) holder;

                ModeloInicioComparteApp mComparte = mVista.getModeloInicioComparteApp();

                if(mComparte.getTitulo() != null && !TextUtils.isEmpty(mComparte.getTitulo())){
                    viewHolderComparteApp.txtTitulo.setText(mComparte.getTitulo());
                    viewHolderComparteApp.txtTitulo.setVisibility(View.VISIBLE);
                }else{
                    viewHolderComparteApp.txtTitulo.setVisibility(View.GONE);
                }

                if(mComparte.getDescripcion() != null && !TextUtils.isEmpty(mComparte.getDescripcion())){
                    viewHolderComparteApp.txtDescripcion.setText(mComparte.getDescripcion());
                    viewHolderComparteApp.txtDescripcion.setVisibility(View.VISIBLE);
                }else{
                    viewHolderComparteApp.txtDescripcion.setVisibility(View.GONE);
                }

                if(mComparte.getImagen() != null && !TextUtils.isEmpty(mComparte.getImagen())){
                    Glide.with(context)
                            .load(RetrofitBuilder.urlImagenes + mComparte.getImagen())
                            .apply(opcionesGlide)
                            .into(viewHolderComparteApp.imgPortada);
                }else{
                    int resourceId = R.drawable.camaradefecto;
                    Glide.with(context)
                            .load(resourceId)
                            .apply(opcionesGlide)
                            .into(viewHolderComparteApp.imgPortada);
                }

                viewHolderComparteApp.setListener((view, position1) -> {
                    fragmentTabInicio.compartirAplicacion();
                });

                break;

            case ModeloVistasInicio.TIPO_INSIGNIAS:

                AdaptadorInicio.RecyclerInsigniasViewHolder viewHolderInsignias = (AdaptadorInicio.RecyclerInsigniasViewHolder) holder;

                viewHolderInsignias.txtToolbar.setText(context.getString(R.string.insignias));


                if(modeloInicioSeparador.getHayMasDe5Insignias() == 1){
                    viewHolderInsignias.imgFlechaDerecha.setVisibility(View.VISIBLE);
                }else{
                    viewHolderInsignias.imgFlechaDerecha.setVisibility(View.GONE);
                }

                viewHolderInsignias.imgFlechaDerecha.setOnClickListener(v -> {
                    if(modeloInicioSeparador.getHayMasDe5Insignias() == 1){
                        fragmentTabInicio.vistaTodosLasInsignias();
                    }
                });


                configurarRecyclerInsignias(viewHolderInsignias.recyclerViewInsignias, mVista.getModeloInicioInsignias());
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

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerVideos(context, modeloInicioVideos, fragmentTabInicio);
        recyclerView.setAdapter(adaptadorInterno);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }


    private void configurarRecyclerImagenes(RecyclerView recyclerView, List<ModeloInicioImagenes> modeloInicioImagenes) {

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerImagenes(context, modeloInicioImagenes, fragmentTabInicio);

        recyclerView.setAdapter(adaptadorInterno);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    }



    private void configurarRecyclerInsignias(RecyclerView recyclerView, List<ModeloInicioInsignias> modeloInicioInsignias) {

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerInsignias(context, modeloInicioInsignias, fragmentTabInicio);
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
        private RecyclerView recyclerViewVideos;
        private TextView txtToolbar;

        private ImageView imgFlechaDerecha;

        RecyclerVideoViewHolder(View itemView) {
            super(itemView);
            recyclerViewVideos = itemView.findViewById(R.id.recyclerViewVideos);
            txtToolbar = itemView.findViewById(R.id.txtToolbar);
            imgFlechaDerecha = itemView.findViewById(R.id.imgFlechaDerecha);
        }
    }


    // BLOQUE IMAGENES
    private static class RecyclerImagenesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewImagenes;
        private TextView txtToolbar;
        private ImageView imgFlechaDerecha;


        RecyclerImagenesViewHolder(View itemView) {
            super(itemView);
            recyclerViewImagenes = itemView.findViewById(R.id.recyclerViewImagenes);
            txtToolbar = itemView.findViewById(R.id.txtToolbar);
            imgFlechaDerecha = itemView.findViewById(R.id.imgFlechaDerecha);
        }
    }


    // BLOQUE COMPARTE APP
    private static class ComparteAppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ShapeableImageView imgPortada;
        private TextView txtTitulo;
        private TextView txtDescripcion;

        private ImageView imgShare;

        IOnRecyclerViewClickListener listener;

        public void setListener(IOnRecyclerViewClickListener listener) {
            this.listener = listener;
            itemView.setOnClickListener(this);
        }


        ComparteAppViewHolder(View itemView) {
            super(itemView);
            imgPortada = itemView.findViewById(R.id.iconImageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
            imgShare = itemView.findViewById(R.id.imgShare);
        }

        @Override
        public void onClick(View v) {
            listener.onClick(v, getBindingAdapterPosition());
        }
    }


    // BLOQUE INSIGNIAS
    private static class RecyclerInsigniasViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerViewInsignias;
        private TextView txtToolbar;
        private ImageView imgFlechaDerecha;

        RecyclerInsigniasViewHolder(View itemView) {
            super(itemView);
            recyclerViewInsignias = itemView.findViewById(R.id.recyclerViewInsignias);
            txtToolbar = itemView.findViewById(R.id.txtToolbar);
            imgFlechaDerecha = itemView.findViewById(R.id.imgFlechaDerecha);
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