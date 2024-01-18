package com.tatanstudios.abba.adaptadores.planes.bloques;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.planes.bloques.SubItemModel;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesReVertical;

import java.util.List;

public class AdaptadorVertical extends RecyclerView.Adapter<AdaptadorVertical.ViewHolder> {
    private List<ModeloMisPlanesReVertical> modeloMisPlanesReVertical;

    public AdaptadorVertical(List<ModeloMisPlanesReVertical> modeloMisPlanesReVertical) {
        this.modeloMisPlanesReVertical = modeloMisPlanesReVertical;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_titulo, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModeloMisPlanesReVertical m = modeloMisPlanesReVertical.get(position);

        holder.txtTitulo.setText(m.getTitulo());


        holder.itemView.setOnClickListener(v -> {

        });
    }

    @Override
    public int getItemCount() {
        return modeloMisPlanesReVertical.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo;
        private CheckBox idCheck;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            idCheck = itemView.findViewById(R.id.idcheck);
        }
    }
}