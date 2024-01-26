package com.tatanstudios.abba.adaptadores.inicio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.modelos.inicio.bloques.imagenes.ModeloInicioImagenes;
import com.tatanstudios.abba.modelos.inicio.bloques.insignias.ModeloInicioInsignias;

import java.util.List;

public class AdaptadorInicioRecyclerInsignias extends RecyclerView.Adapter<AdaptadorInicioRecyclerInsignias.ViewHolder> {

    private List<ModeloInicioInsignias> modeloInicioInsignias;

    public AdaptadorInicioRecyclerInsignias(List<ModeloInicioInsignias> modeloInicioInsignias) {
        this.modeloInicioInsignias = modeloInicioInsignias;
    }

    @NonNull
    @Override
    public AdaptadorInicioRecyclerInsignias.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_inicio_insignias, parent, false);
        return new AdaptadorInicioRecyclerInsignias.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInicioRecyclerInsignias.ViewHolder holder, int position) {
        ModeloInicioInsignias m = modeloInicioInsignias.get(position);




    }

    @Override
    public int getItemCount() {


        return modeloInicioInsignias.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iconImageView;

        ViewHolder(View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);

        }
    }
}