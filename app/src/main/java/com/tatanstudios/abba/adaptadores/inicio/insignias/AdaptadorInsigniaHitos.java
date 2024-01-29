package com.tatanstudios.abba.adaptadores.inicio.insignias;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.inicio.AdaptadorInicioRecyclerImagenes;
import com.tatanstudios.abba.adaptadores.inicio.AdaptadorInicioRecyclerInsignias;
import com.tatanstudios.abba.adaptadores.inicio.AdaptadorInicioRecyclerVideos;
import com.tatanstudios.abba.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abba.fragmentos.inicio.tabs.FragmentTabInicio;
import com.tatanstudios.abba.modelos.inicio.ModeloVistasInicio;
import com.tatanstudios.abba.modelos.inicio.bloques.comparteapp.ModeloInicioComparteApp;
import com.tatanstudios.abba.modelos.inicio.bloques.imagenes.ModeloInicioImagenes;
import com.tatanstudios.abba.modelos.inicio.bloques.insignias.ModeloInicioInsignias;
import com.tatanstudios.abba.modelos.inicio.bloques.separador.ModeloInicioSeparador;
import com.tatanstudios.abba.modelos.inicio.bloques.versiculos.ModeloInicioDevocional;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;
import com.tatanstudios.abba.modelos.insignias.ModeloDescripcionHitos;
import com.tatanstudios.abba.modelos.insignias.ModeloInsigniaHitos;
import com.tatanstudios.abba.modelos.insignias.ModeloVistaHitos;
import com.tatanstudios.abba.network.RetrofitBuilder;

import java.util.List;

public class AdaptadorInsigniaHitos extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ModeloVistaHitos> modeloVistaHitos;
    private Context context;

    private String textoFalta = "";

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorInsigniaHitos(Context context, List<ModeloVistaHitos> modeloVistaHitos, String textoFalta) {
        this.context = context;
        this.modeloVistaHitos = modeloVistaHitos;
        this.textoFalta = textoFalta;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView;

        switch (viewType) {
            case ModeloVistaHitos.TIPO_IMAGEN:
                itemView = inflater.inflate(R.layout.cardview_insignia_hito_descripcion, parent, false);
                return new AdaptadorInsigniaHitos.DescripcionViewHolder(itemView);
            case ModeloVistaHitos.TIPO_RECYCLER:
                itemView = inflater.inflate(R.layout.recyclerview_hitos, parent, false);
                return new AdaptadorInsigniaHitos.RecyclerHitosViewHolder(itemView);

            default:
                throw new IllegalArgumentException("Tipo de vista desconocido");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ModeloVistaHitos mVista = modeloVistaHitos.get(position);

        switch (mVista.getTipoVista()) {
            case ModeloVistaHitos.TIPO_IMAGEN:

                ModeloDescripcionHitos mDescripcion = mVista.getModeloDescripcionHitos();

                AdaptadorInsigniaHitos.DescripcionViewHolder viewHolderDescripcion = (AdaptadorInsigniaHitos.DescripcionViewHolder) holder;

                if(mDescripcion.getTitulo() != null && !TextUtils.isEmpty(mDescripcion.getTitulo())){
                    viewHolderDescripcion.txtTitulo.setText(mDescripcion.getTitulo());
                }

                if(mDescripcion.getDescripcion() != null && !TextUtils.isEmpty(mDescripcion.getDescripcion())){
                    viewHolderDescripcion.txtDescripcion.setText(mDescripcion.getDescripcion());
                    viewHolderDescripcion.txtDescripcion.setVisibility(View.VISIBLE);
                }else{
                    viewHolderDescripcion.txtDescripcion.setVisibility(View.GONE);
                }

                if(mDescripcion.getImagen() != null && !TextUtils.isEmpty(mDescripcion.getImagen())){
                    Glide.with(context)
                            .load(RetrofitBuilder.urlImagenes + mDescripcion.getImagen())
                            .apply(opcionesGlide)
                            .into(viewHolderDescripcion.imgLogo);
                }else{
                    int resourceId = R.drawable.camaradefecto;
                    Glide.with(context)
                            .load(resourceId)
                            .apply(opcionesGlide)
                            .into(viewHolderDescripcion.imgLogo);
                }

                viewHolderDescripcion.txtNivel.setText(String.valueOf(mDescripcion.getNivelvoy()));

                break;
            case ModeloVistaHitos.TIPO_RECYCLER:

                AdaptadorInsigniaHitos.RecyclerHitosViewHolder viewHolderRecycler = (AdaptadorInsigniaHitos.RecyclerHitosViewHolder) holder;

                configurarRecyclerHitos(viewHolderRecycler.recyclerView, mVista.getModeloInsigniaHitos());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return modeloVistaHitos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return modeloVistaHitos.get(position).getTipoVista();
    }


    private void configurarRecyclerHitos(RecyclerView recyclerView, List<ModeloInsigniaHitos> modeloInsigniaHitos) {

        RecyclerView.Adapter adaptadorInterno = new AdaptadorInicioRecyclerHitos(context, modeloInsigniaHitos, textoFalta);
        recyclerView.setAdapter(adaptadorInterno);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
    }


    // BLOQUE DEVOCIONAL
    private static class DescripcionViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgLogo;
        private TextView txtNivel;
        private TextView txtTitulo;
        private TextView txtDescripcion;

        DescripcionViewHolder(View itemView) {
            super(itemView);
            imgLogo = itemView.findViewById(R.id.imgLogo);
            txtNivel = itemView.findViewById(R.id.txtNivel);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtDescripcion = itemView.findViewById(R.id.txtDescripcion);
        }
    }


    private static class RecyclerHitosViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView recyclerView;

        RecyclerHitosViewHolder(View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }







}