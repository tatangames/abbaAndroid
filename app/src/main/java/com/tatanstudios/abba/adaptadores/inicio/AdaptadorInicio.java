package com.tatanstudios.abba.adaptadores.inicio;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
                return new AdaptadorInicio.RecyclerViewHolder(itemView);
            default:
                throw new IllegalArgumentException("Tipo de vista desconocido");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ModeloVistasInicio modeloVistasInicio = modeloVistasInicios.get(position);

        switch (modeloVistasInicio.getTipoVista()) {
            case ModeloVistasInicio.TIPO_DEVOCIONAL:


                AdaptadorInicio.DevocionalViewHolder viewHolderDevocional = (AdaptadorInicio.DevocionalViewHolder) holder;
                viewHolderDevocional.txtDevocional.setText("holis");


                break;
            case ModeloVistasInicio.TIPO_VIDEOS:
               // AdaptadorPlanBloque.RecyclerViewHolder recyclerViewHolder = (AdaptadorPlanBloque.RecyclerViewHolder) holder;
               // configurarRecyclerView(recyclerViewHolder.recyclerView, item.getSubItems());
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






    // BLOQUE DEVOCIONAL
    private static class DevocionalViewHolder extends RecyclerView.ViewHolder {

        TextView txtDevocional;

        DevocionalViewHolder(View itemView) {
            super(itemView);
            txtDevocional = itemView.findViewById(R.id.txtDevocional);
        }
    }




    // BLOQUE VIDEOS
    private static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerViewVideos;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            recyclerViewVideos = itemView.findViewById(R.id.recyclerViewVideos);
        }
    }





}