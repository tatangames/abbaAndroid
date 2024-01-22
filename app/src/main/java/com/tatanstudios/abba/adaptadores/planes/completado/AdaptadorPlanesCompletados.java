package com.tatanstudios.abba.adaptadores.planes.completado;

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
import com.tatanstudios.abba.fragmentos.planes.FragmentPlanesCompletados;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.network.RetrofitBuilder;

import java.util.List;


public class AdaptadorPlanesCompletados extends RecyclerView.Adapter<AdaptadorPlanesCompletados.MyViewHolder> {

    private Context context;
    private List<ModeloPlanes> modeloPlanes;
    private FragmentPlanesCompletados fragmentPlanesCompletados;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorPlanesCompletados(Context context, List<ModeloPlanes> modeloPlanes, FragmentPlanesCompletados fragmentPlanesCompletados) {
        this.context = context;
        this.modeloPlanes = modeloPlanes;
        this.fragmentPlanesCompletados = fragmentPlanesCompletados;
    }

    @NonNull
    @Override
    public AdaptadorPlanesCompletados.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_planes_completados, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorPlanesCompletados.MyViewHolder holder, int position) {

        ModeloPlanes m = modeloPlanes.get(position);

        /*if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){
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
        }*/

        /*if(m.getTitulo() != null && !TextUtils.isEmpty(m.getTitulo())){
            holder.txtTitulo.setText(m.getTitulo());
        }else{
            holder.txtTitulo.setText("");
        }*/

        holder.txtTitulo.setText(String.valueOf(m.getContador()));

        // ver plan seleccionado
        holder.itemView.setOnClickListener(v -> {

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

        public MyViewHolder(View itemView){
            super(itemView);

            imgPlan = itemView.findViewById(R.id.imageView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
        }
    }

}