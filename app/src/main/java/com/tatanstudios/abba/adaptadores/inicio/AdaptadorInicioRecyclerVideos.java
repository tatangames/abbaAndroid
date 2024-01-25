package com.tatanstudios.abba.adaptadores.inicio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.planes.bloques.AdaptadorHorizontal;
import com.tatanstudios.abba.fragmentos.planes.bloques.SubItemModel;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;

import java.util.List;

public class AdaptadorInicioRecyclerVideos extends RecyclerView.Adapter<AdaptadorInicioRecyclerVideos.ViewHolder> {

    private List<ModeloInicioVideos> modeloInicioVideos;

    public AdaptadorInicioRecyclerVideos(List<ModeloInicioVideos> modeloInicioVideos) {
        this.modeloInicioVideos = modeloInicioVideos;
    }

    @NonNull
    @Override
    public AdaptadorInicioRecyclerVideos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_inicio_video, parent, false);
        return new AdaptadorInicioRecyclerVideos.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInicioRecyclerVideos.ViewHolder holder, int position) {
        ModeloInicioVideos m = modeloInicioVideos.get(position);

       holder.txtTitulo.setText("videeeo");


    }

    @Override
    public int getItemCount() {


        return modeloInicioVideos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);

        }
    }
}