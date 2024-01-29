package com.tatanstudios.abba.adaptadores.inicio.insignias;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abba.fragmentos.inicio.tabs.FragmentTabInicio;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;
import com.tatanstudios.abba.modelos.insignias.ModeloInsigniaHitos;
import com.tatanstudios.abba.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorInicioRecyclerHitos extends RecyclerView.Adapter<AdaptadorInicioRecyclerHitos.ViewHolder> {

    private List<ModeloInsigniaHitos> modeloInsigniaHitos;
    private Context context;
    private String textoFalta;

    public AdaptadorInicioRecyclerHitos(Context context, List<ModeloInsigniaHitos> modeloInsigniaHitos, String textoFalta) {
        this.context = context;
        this.modeloInsigniaHitos = modeloInsigniaHitos;
        this.textoFalta = textoFalta;
    }

    @NonNull
    @Override
    public AdaptadorInicioRecyclerHitos.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_insignia_hito_recycler, parent, false);
        return new AdaptadorInicioRecyclerHitos.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorInicioRecyclerHitos.ViewHolder holder, int position) {
        ModeloInsigniaHitos m = modeloInsigniaHitos.get(position);

        if(m.getFechaFormat() != null && !TextUtils.isEmpty(m.getFechaFormat())){
            holder.txtTitulo.setText(m.getFechaFormat());
        }

        // son los normales ganado
        if(m.getEsNextLevel() == 0){


            String formatoFinal = m.getTextoCompletado() + " " + m.getFechaFormat();
            holder.txtTitulo.setText(formatoFinal);
            holder.txtNivel.setText(String.valueOf(m.getNivel()));

        }else{
            // es el del siguiente nivel
            String formatoFinal = textoFalta + " " + m.getHitCuantoFalta();
            holder.txtTitulo.setText(formatoFinal);

            holder.txtNivel.setText(String.valueOf(m.getCualNextLevel()));
        }
    }

    @Override
    public int getItemCount() {
        return modeloInsigniaHitos.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitulo;
        private TextView txtNivel;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtNivel = itemView.findViewById(R.id.txtNivel);
        }


    }
}