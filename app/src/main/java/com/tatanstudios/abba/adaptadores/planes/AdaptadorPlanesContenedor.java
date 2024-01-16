package com.tatanstudios.abba.adaptadores.planes;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.planes.PlanesContenedorActivity;
import com.tatanstudios.abba.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;

import java.util.List;


public class AdaptadorPlanesContenedor extends RecyclerView.Adapter<AdaptadorPlanesContenedor.MyViewHolder> {

    private Context context;
    private List<ModeloPlanes> modeloPlanes;
    private PlanesContenedorActivity planesContenedorActivity;

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

        /*holder.btnPlanes.setText(modeloBotoneraPlanes.get(position).getTexto());

        holder.btnPlanes.setBackgroundTintList(colorStateGrey);
        holder.btnPlanes.setTextColor(colorStateWhite);

        holder.btnPlanes.setOnClickListener(v ->{

            int id = modeloBotoneraPlanes.get(position).getId();
            fragmentPlanes.tipoPlan(id);
        });*/

        holder.itemView.setOnClickListener(v -> {
            // Acción a realizar cuando se hace clic en el layout del elemento

            planesContenedorActivity.informacionPlan(m.getId());
            // Puedes usar clickedPosition para realizar acciones específicas según la posición clicada
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