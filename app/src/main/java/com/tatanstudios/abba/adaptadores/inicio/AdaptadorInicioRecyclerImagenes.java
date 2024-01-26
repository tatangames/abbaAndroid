package com.tatanstudios.abba.adaptadores.inicio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.modelos.inicio.bloques.imagenes.ModeloInicioImagenes;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;

import java.util.List;

public class AdaptadorInicioRecyclerImagenes extends RecyclerView.Adapter<AdaptadorInicioRecyclerImagenes.ViewHolder> {

    private List<ModeloInicioImagenes> modeloInicioImagenes;

    public AdaptadorInicioRecyclerImagenes(List<ModeloInicioImagenes> modeloInicioImagenes) {
        this.modeloInicioImagenes = modeloInicioImagenes;
    }

    @NonNull
    @Override
    public AdaptadorInicioRecyclerImagenes.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_inicio_imagenes, parent, false);
        return new AdaptadorInicioRecyclerImagenes.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInicioRecyclerImagenes.ViewHolder holder, int position) {
        ModeloInicioImagenes m = modeloInicioImagenes.get(position);




    }

    @Override
    public int getItemCount() {


        return modeloInicioImagenes.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconImageView;

        ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);

        }
    }
}