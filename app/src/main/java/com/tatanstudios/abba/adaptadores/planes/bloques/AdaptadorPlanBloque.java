package com.tatanstudios.abba.adaptadores.planes.bloques;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;

import com.tatanstudios.abba.fragmentos.planes.bloques.ItemModel;
import com.tatanstudios.abba.fragmentos.planes.bloques.SubItemModel;

import java.util.List;

public class AdaptadorPlanBloque extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ItemModel> items;

    private Context context;

    public AdaptadorPlanBloque(Context context, List<ItemModel> items) {
        this.items = items;
        this.context = context;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;

        switch (viewType) {
            case ItemModel.TIPO_IMAGEN:
                itemView = inflater.inflate(R.layout.cardview_bloque_portada, parent, false);
                return new ImagenViewHolder(itemView);
            case ItemModel.TIPO_RECYCLER:
                itemView = inflater.inflate(R.layout.cardview_recycler_horizontal, parent, false);
                return new RecyclerViewHolder(itemView);
            default:
                throw new IllegalArgumentException("Tipo de vista desconocido");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemModel item = items.get(position);

        switch (item.getTipo()) {
            case ItemModel.TIPO_IMAGEN:
                ImagenViewHolder imagenViewHolder = (ImagenViewHolder) holder;
                imagenViewHolder.imagen.setImageResource(item.getImagenResId());
                break;
            case ItemModel.TIPO_RECYCLER:
                RecyclerViewHolder recyclerViewHolder = (RecyclerViewHolder) holder;
                configurarRecyclerView(recyclerViewHolder.recyclerView, item.getSubItems());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getTipo();
    }

    private void configurarRecyclerView(RecyclerView recyclerView, List<SubItemModel> subItems) {


        RecyclerView.Adapter adaptadorInterno = new AdaptadorHorizontal(subItems);
        recyclerView.setAdapter(adaptadorInterno);

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.HORIZONTAL, false));

    }

    // ViewHolder para el tipo de vista de imagen
    private static class ImagenViewHolder extends RecyclerView.ViewHolder {
        ImageView imagen;

        ImagenViewHolder(View itemView) {
            super(itemView);
            imagen = itemView.findViewById(R.id.imageView);
        }
    }

    // ViewHolder para el tipo de vista de RecyclerView interno
    private static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        RecyclerView recyclerView;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }

    public void actualizarDatos(List<ItemModel> nuevosDatos) {
        // Actualiza la lista de datos y notifica al adaptador sobre el cambio
        this.items = nuevosDatos;
        notifyDataSetChanged();
    }


    

}