package com.tatanstudios.abba.adaptadores.planes.bloques;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.planes.bloques.ItemModel;
import com.tatanstudios.abba.fragmentos.planes.bloques.SubItemModel;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesReHorizontal;

import java.util.List;

public class AdaptadorHorizontal extends RecyclerView.Adapter<AdaptadorHorizontal.ViewHolder> {
    private List<SubItemModel> subItems;

    public AdaptadorHorizontal(List<SubItemModel> subItems) {
        this.subItems = subItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_subitem, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubItemModel subItem = subItems.get(position);

        holder.txtContador.setText("10");
        holder.txtFecha.setText("enero");

        holder.itemView.setOnClickListener(v -> {
            Log.i("PORTADA", "tocadp");
        });
    }

    @Override
    public int getItemCount() {

        Log.i("PORTADA", "conteo: " + subItems.size());

        return subItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtContador;
        private TextView txtFecha;

        ViewHolder(View itemView) {
            super(itemView);
            txtContador = itemView.findViewById(R.id.txtContador);
            txtFecha = itemView.findViewById(R.id.txtFecha);
        }
    }
}