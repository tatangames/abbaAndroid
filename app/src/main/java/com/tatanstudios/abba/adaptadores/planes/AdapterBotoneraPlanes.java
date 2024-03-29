package com.tatanstudios.abba.adaptadores.planes;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.menu.FragmentPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloBotoneraPlanes;

import java.util.List;


public class AdapterBotoneraPlanes extends RecyclerView.Adapter<AdapterBotoneraPlanes.MyViewHolder> {

    // para agregar lista de direcciones

    private Context context;
    private List<ModeloBotoneraPlanes> modeloBotoneraPlanes;

    private FragmentPlanes fragmentPlanes;

    public AdapterBotoneraPlanes(){}

    private int botonSeleccionado = 0;

    private ColorStateList colorStateGrey, colorStateWhite, colorStateBlack;

    private int tema;

    public AdapterBotoneraPlanes(Context context, List<ModeloBotoneraPlanes> modeloBotoneraPlanes, FragmentPlanes fragmentPlanes, int tema) {
        this.context = context;
        this.modeloBotoneraPlanes = modeloBotoneraPlanes;
        this.fragmentPlanes = fragmentPlanes;

        int colorGris = ContextCompat.getColor(context, R.color.colorGrisBtnDisable);
        int colorBlanco = ContextCompat.getColor(context, R.color.white);
        int colorNegro = ContextCompat.getColor(context, R.color.black);

        colorStateGrey = ColorStateList.valueOf(colorGris);
        colorStateWhite = ColorStateList.valueOf(colorBlanco);
        colorStateBlack = ColorStateList.valueOf(colorNegro);
    }

    @NonNull
    @Override
    public AdapterBotoneraPlanes.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.cardview_botonera_planes, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBotoneraPlanes.MyViewHolder holder, int position) {

        holder.btnPlanes.setText(modeloBotoneraPlanes.get(position).getTexto());

        if (position == botonSeleccionado) {
            // boton seleccionado

            if(tema == 1){ // dark
                holder.btnPlanes.setBackgroundTintList(colorStateWhite);
                holder.btnPlanes.setTextColor(colorStateBlack);
            }else{
                holder.btnPlanes.setBackgroundTintList(colorStateBlack);
                holder.btnPlanes.setTextColor(colorStateWhite);
            }


        } else {
            // boton no seleccionado

            if(tema == 1){ // dark
                holder.btnPlanes.setBackgroundTintList(colorStateGrey);
                holder.btnPlanes.setTextColor(colorStateWhite);
            }else{
                holder.btnPlanes.setBackgroundTintList(colorStateGrey);
                holder.btnPlanes.setTextColor(colorStateWhite);
            }
        }



        holder.btnPlanes.setOnClickListener(v ->{

            botonSeleccionado = position;
            notifyDataSetChanged();

            int id = modeloBotoneraPlanes.get(position).getId();
            fragmentPlanes.tipoPlan(id);
        });
    }

    @Override
    public int getItemCount() {
        if(modeloBotoneraPlanes != null){
            return modeloBotoneraPlanes.size();
        }else{
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView btnPlanes;

        public MyViewHolder(View itemView){
            super(itemView);

            btnPlanes = itemView.findViewById(R.id.btnItem);
        }
    }

}