package com.tatanstudios.abba.adaptadores.planes.buscarplanes;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.planes.FragmentBuscarPlanes;
import com.tatanstudios.abba.fragmentos.planes.FragmentMisPlanes;
import com.tatanstudios.abba.modelos.mas.ModeloFragmentMas;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesTitulo;
import com.tatanstudios.abba.modelos.planes.misplanes.ModeloVistasMisPlanes;
import com.tatanstudios.abba.modelos.planes.planesmodelo.ModeloVistasBuscarPlanes;
import com.tatanstudios.abba.network.RetrofitBuilder;

import java.util.ArrayList;

public class AdaptadorMisPlanes extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private Context context;
    public ArrayList<ModeloVistasMisPlanes> modeloVistasMisPlanes;
    private FragmentMisPlanes fragmentMisPlanes;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorMisPlanes(Context context, ArrayList<ModeloVistasMisPlanes> modeloVistasMisPlanes, FragmentMisPlanes fragmentMisPlanes){
        this.context = context;
        this.fragmentMisPlanes = fragmentMisPlanes;
        this.modeloVistasMisPlanes = modeloVistasMisPlanes;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ModeloVistasMisPlanes.TIPO_CONTINUAR) {
            View view = inflater.inflate(R.layout.cardview_mis_planes_continuar, parent, false);
            return new HolderVistaPlanesContinuar(view);

        } else if (viewType == ModeloVistasBuscarPlanes.TIPO_PLANES) {
            View view = inflater.inflate(R.layout.cardview_mis_planes, parent, false);
            return new HolderVistaPlanes(view);
        }

        throw new IllegalArgumentException("Tipo de vista desconocido");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModeloVistasMisPlanes modelo = modeloVistasMisPlanes.get(position);

        if (holder instanceof HolderVistaPlanesContinuar) {
            // Configurar el ViewHolder para un ítem normal
            ModeloPlanes m = modelo.getModeloPlanes();

            // La imagen es la portada, pero se obtiene por imagen
            if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){
                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + m.getImagen())
                        .apply(opcionesGlide)
                        .into(((HolderVistaPlanesContinuar) holder).imgPlan);
            }else{
                int resourceId = R.drawable.camaradefecto;
                Glide.with(context)
                        .load(resourceId)
                        .apply(opcionesGlide)
                        .into(((HolderVistaPlanesContinuar) holder).imgPlan);
            }

            ((HolderVistaPlanesContinuar) holder).txtTitulo.setText(m.getTitulo());

            holder.itemView.setOnClickListener(v -> {
                fragmentMisPlanes.verBloquePlanes(m.getId());
            });

        }
        else if (holder instanceof HolderVistaPlanes) {

            ModeloPlanes m = modelo.getModeloPlanes();


            if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){
                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + m.getImagen())
                        .apply(opcionesGlide)
                        .into(((HolderVistaPlanes) holder).imgPlan);
            }else{
                int resourceId = R.drawable.camaradefecto;
                Glide.with(context)
                        .load(resourceId)
                        .apply(opcionesGlide)
                        .into(((HolderVistaPlanes) holder).imgPlan);
            }


            ((HolderVistaPlanes) holder).txtTitulo.setText(m.getTitulo());

            // FALTA AJUSTARLA
            if(m.getBarraProgreso() == 1){
                ((HolderVistaPlanes) holder).progressBar.setVisibility(View.VISIBLE);
            }else{
                ((HolderVistaPlanes) holder).progressBar.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(v -> {
                fragmentMisPlanes.verBloquePlanes(m.getId());
            });
        }
    }



    // HOLDER PARA VISTA TITULO
    private static class HolderVistaPlanesContinuar extends RecyclerView.ViewHolder{

        private ShapeableImageView imgPlan;
        private TextView txtTitulo;

        public HolderVistaPlanesContinuar(@NonNull View itemView) {
            super(itemView);

            imgPlan = itemView.findViewById(R.id.imageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);

        }
    }



    private static class HolderVistaPlanes extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación

        private ShapeableImageView imgPlan;
        private TextView txtTitulo;
        private ProgressBar progressBar;

        public HolderVistaPlanes(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí

            imgPlan = itemView.findViewById(R.id.imageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            progressBar = itemView.findViewById(R.id.progressBar);

        }
    }

    @Override
    public int getItemCount() {
        if(modeloVistasMisPlanes != null){
            return modeloVistasMisPlanes.size();
        }else{
            return 0;
        }
    }

    public int getItemViewType(int position) {
        return modeloVistasMisPlanes.get(position).getTipoVista();
    }













}