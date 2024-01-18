package com.tatanstudios.abba.adaptadores.planes.bloques;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.planes.PlanesBloquesActivity;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloqueDetalle;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloques;

import java.util.List;

public class AdaptadorBloqueHorizontal extends RecyclerView.Adapter<AdaptadorBloqueHorizontal.MyViewHolder> {
    private List<ModeloMisPlanesBloques> modeloMisPlanesBloques;

    private Context context;
    private RecyclerView recyclerView;

    private ColorStateList colorStateGrey, colorStateWhite, colorStateBlack;

    private boolean tema;
    private int hayDiaActual, idUltimoBloque;

    private PlanesBloquesActivity planesBloquesActivity;

    private boolean primerBloqueDrawable = true;

    public AdaptadorBloqueHorizontal(Context context, List<ModeloMisPlanesBloques>
            modeloMisPlanesBloques, RecyclerView recyclerView, int tema, int hayDiaActual, int idUltimoBloque,
                                     PlanesBloquesActivity planesBloquesActivity) {
        this.context = context;
        this.modeloMisPlanesBloques = modeloMisPlanesBloques;
        this.recyclerView = recyclerView;
        this.hayDiaActual = hayDiaActual;
        this.idUltimoBloque = idUltimoBloque;
        this.planesBloquesActivity = planesBloquesActivity;

        if(tema == 1){ // dark
            this.tema = true;
        }else{
            this.tema = false;
        }

        int colorGris = ContextCompat.getColor(context, R.color.colorGrisBtnDisable);
        int colorBlanco = ContextCompat.getColor(context, R.color.white);
        int colorNegro = ContextCompat.getColor(context, R.color.black);

        colorStateGrey = ColorStateList.valueOf(colorGris);
        colorStateWhite = ColorStateList.valueOf(colorBlanco);
        colorStateBlack = ColorStateList.valueOf(colorNegro);
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

            if(m.getEstaPresionado()){

                if(tema){

                    // TEMA DARK


                    holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_dark_white_on));
                    holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_gris));
                    holder.txtFecha.setTextColor(colorStateWhite);


                }else{

                    // TEMA LIGHT

                    holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_light_negro_on));
                    holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_gris));
                    holder.txtFecha.setTextColor(colorStateWhite);

                }

            }else{


                if(tema){

                    // TEMA DARK

                    if(hayDiaActual == 1){
                        if(m.getMismodia() == 1){
                            if(primerBloqueDrawable){
                                primerBloqueDrawable = false;
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_dark_white_on));
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_gris));
                                holder.txtFecha.setTextColor(colorStateWhite);

                                planesBloquesActivity.llenarDatosAdapterVertical(modeloMisPlanesBloques.get(position).getModeloMisPlanesBloqueDetalles());

                            }else{
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_dark_gris_off));
                                holder.txtFecha.setTextColor(colorStateWhite);
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_vacia));
                            }

                        }else{
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_dark_gris_off));
                            holder.txtFecha.setTextColor(colorStateWhite);
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_vacia));
                        }


                    }else{
                        if(idUltimoBloque == m.getId()){
                            if(primerBloqueDrawable){
                                primerBloqueDrawable = false;
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_dark_white_on));
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_gris));
                                holder.txtFecha.setTextColor(colorStateWhite);

                                planesBloquesActivity.llenarDatosAdapterVertical(modeloMisPlanesBloques.get(position).getModeloMisPlanesBloqueDetalles());

                            }else{
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_dark_gris_off));
                                holder.txtFecha.setTextColor(colorStateWhite);
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_vacia));
                            }
                        }else{
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_dark_gris_off));
                            holder.txtFecha.setTextColor(colorStateWhite);
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_vacia));
                        }
                    }


                }else{

                    // TEMA LIGHT

                    if(hayDiaActual == 1){
                        if(m.getMismodia() == 1){
                            if(primerBloqueDrawable){
                                primerBloqueDrawable = false;
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_light_negro_on));
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_gris));
                                holder.txtFecha.setTextColor(colorStateWhite);

                                planesBloquesActivity.llenarDatosAdapterVertical(modeloMisPlanesBloques.get(position).getModeloMisPlanesBloqueDetalles());

                            }else{
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_light_gris_off));
                                holder.txtFecha.setTextColor(colorStateBlack);
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_vacia));
                            }

                        }else{
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_light_gris_off));
                            holder.txtFecha.setTextColor(colorStateBlack);
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_vacia));
                        }
                    }else{
                        if(idUltimoBloque == m.getId()){
                            if(primerBloqueDrawable){
                                primerBloqueDrawable = false;
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_light_negro_on));
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_gris));
                                holder.txtFecha.setTextColor(colorStateWhite);

                                planesBloquesActivity.llenarDatosAdapterVertical(modeloMisPlanesBloques.get(position).getModeloMisPlanesBloqueDetalles());

                            }else{
                                holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_light_gris_off));
                                holder.txtFecha.setTextColor(colorStateBlack);
                                holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_vacia));
                            }
                        }else{
                            holder.constraintLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_light_gris_off));
                            holder.txtFecha.setTextColor(colorStateBlack);
                            holder.txtFecha.setBackground(ContextCompat.getDrawable(context, R.drawable.caja_redondeado_vacia));
                        }
                    }
                }
            }


        holder.txtContador.setText(String.valueOf(m.getContador()));
        holder.txtFecha.setText(m.getAbreviatura());
        holder.itemView.setOnClickListener(v -> {

            for (ModeloMisPlanesBloques modelo : modeloMisPlanesBloques) {
                modelo.setEstaPresionado(false);
            }

            planesBloquesActivity.llenarDatosAdapterVertical(modeloMisPlanesBloques.get(position).getModeloMisPlanesBloqueDetalles());
            m.setEstaPresionado(true);
            notifyDataSetChanged();
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
        private ConstraintLayout constraintLayout;

        public MyViewHolder(View itemView){
            super(itemView);

            txtContador = itemView.findViewById(R.id.textviewContador);
            txtFecha = itemView.findViewById(R.id.textviewFecha);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }


    // MOVER DE POSICION AL RECYCLER
    public void moverPosicionRecycler(int posicion) {
        recyclerView.scrollToPosition(posicion);
    }


}