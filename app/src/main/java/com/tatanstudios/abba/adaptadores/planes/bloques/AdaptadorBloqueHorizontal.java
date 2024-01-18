package com.tatanstudios.abba.adaptadores.planes.bloques;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.planes.bloques.SubItemModel;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloques;

import java.util.List;

public class AdaptadorBloqueHorizontal extends RecyclerView.Adapter<AdaptadorBloqueHorizontal.MyViewHolder> {
    private List<ModeloMisPlanesBloques> modeloMisPlanesBloques;

    private Context context;

    public AdaptadorBloqueHorizontal(Context context, List<ModeloMisPlanesBloques> modeloMisPlanesBloques) {
        this.context = context;
        this.modeloMisPlanesBloques = modeloMisPlanesBloques;
    }

    @NonNull
    @Override
    public AdaptadorBloqueHorizontal.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_bloque_horizontal, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorBloqueHorizontal.MyViewHolder holder, int position) {
        ModeloMisPlanesBloques m = modeloMisPlanesBloques.get(position);

        holder.txtContador.setText(String.valueOf(m.getContador()));
        holder.txtFecha.setText(m.getAbreviatura());


        holder.itemView.setOnClickListener(v -> {
            Log.i("PORTADA", "tocadp");
        });
    }

    @Override
    public int getItemCount() {

        if(modeloMisPlanesBloques != null){
            return modeloMisPlanesBloques.size();
        }
        else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtContador;
        private TextView txtFecha;

        public MyViewHolder(View itemView){
            super(itemView);

            txtContador = itemView.findViewById(R.id.textviewContador);
            txtFecha = itemView.findViewById(R.id.textviewFecha);
        }


    }


}