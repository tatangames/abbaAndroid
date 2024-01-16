package com.tatanstudios.abba.adaptadores.planes.buscarplanes;

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
import com.tatanstudios.abba.activitys.planes.PlanesContenedorActivity;
import com.tatanstudios.abba.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.network.RetrofitBuilder;

import java.util.List;


public class AdaptadorPlanesContenedor extends RecyclerView.Adapter<AdaptadorPlanesContenedor.MyViewHolder> {

    private Context context;
    private List<ModeloPlanes> modeloPlanes;
    private PlanesContenedorActivity planesContenedorActivity;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorPlanesContenedor(){}


    public AdaptadorPlanesContenedor(Context context, List<ModeloPlanes> modeloPlanes, PlanesContenedorActivity planesContenedorActivity) {
        this.context = context;
        this.modeloPlanes = modeloPlanes;
        this.planesContenedorActivity = planesContenedorActivity;
    }

    @NonNull
    @Override
    public AdaptadorPlanesContenedor.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_planes_contenedor, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPlanesContenedor.MyViewHolder holder, int position) {

        ModeloPlanes m = modeloPlanes.get(position);

        if(m.getTitulo() != null && !TextUtils.isEmpty(m.getTitulo())){
            holder.txtTitulo.setText(m.getTitulo());
        }else{
            holder.txtTitulo.setText("");
        }

        if(m.getSubtitulo() != null && !TextUtils.isEmpty(m.getSubtitulo())){
            holder.txtSubTitulo.setText(m.getSubtitulo());
        }else{
            holder.txtSubTitulo.setText("");
        }

        if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){
            Glide.with(context)
                    .load(RetrofitBuilder.urlImagenes + m.getImagen())
                    .apply(opcionesGlide)
                    .into(holder.imgPlan);
        }else{
            int resourceId = R.drawable.camaradefecto;
            Glide.with(context)
                    .load(resourceId)
                    .apply(opcionesGlide)
                    .into(holder.imgPlan);
        }

        // ver plan seleccionado
        holder.itemView.setOnClickListener(v -> {
            planesContenedorActivity.informacionPlan(m.getId());
        });
    }

    @Override
    public int getItemCount() {
        if(modeloPlanes != null){
            return modeloPlanes.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ShapeableImageView imgPlan;
        private TextView txtTitulo;
        private TextView txtSubTitulo;
        private TextView btnVer;

        private IOnRecyclerViewClickListener listener;


        public MyViewHolder(View itemView){
            super(itemView);

            imgPlan = itemView.findViewById(R.id.imageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            txtSubTitulo = itemView.findViewById(R.id.txtSubtitulo);
            btnVer = itemView.findViewById(R.id.txtVer);

        }


    }

}