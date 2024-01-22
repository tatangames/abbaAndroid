package com.tatanstudios.abba.adaptadores.planes.completado;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.planes.PlanesBloquesActivity;
import com.tatanstudios.abba.activitys.planes.completado.PlanesBloquesVistaActivity;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloqueDetalle;

import java.util.List;

public class AdaptadorBloqueVerticalVista extends RecyclerView.Adapter<AdaptadorBloqueVerticalVista.ViewHolder> {
    private List<ModeloMisPlanesBloqueDetalle> modeloMisPlanesBloqueDetalles;
    private Context context;
    private PlanesBloquesVistaActivity planesBloquesVistaActivity;

    private boolean tema;

    private ColorStateList colorStateWhite, colorStateBlack;


    public AdaptadorBloqueVerticalVista(Context context, List<ModeloMisPlanesBloqueDetalle> modeloMisPlanesBloqueDetalles,
                                        PlanesBloquesVistaActivity planesBloquesVistaActivity, int temaInt) {
        this.context = context;
        this.modeloMisPlanesBloqueDetalles = modeloMisPlanesBloqueDetalles;
        this.planesBloquesVistaActivity = planesBloquesVistaActivity;
        if(temaInt == 1){
            this.tema = true;
        }else{
            this.tema = false;
        }


        int colorBlanco = ContextCompat.getColor(context, R.color.white);
        int colorNegro = ContextCompat.getColor(context, R.color.black);

        colorStateWhite = ColorStateList.valueOf(colorBlanco);
        colorStateBlack = ColorStateList.valueOf(colorNegro);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.cardview_bloque_item_vertical_vista, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModeloMisPlanesBloqueDetalle m = modeloMisPlanesBloqueDetalles.get(position);

        if(tema){ // DARK
            holder.txtTitulo.setTextColor(colorStateWhite);
        }else{
            holder.txtTitulo.setTextColor(colorStateBlack);
        }

        holder.txtTitulo.setText(m.getTitulo());

        holder.txtTitulo.setOnClickListener(v -> {
            int idBlockDeta = m.getId();
            int tienePreguntas = m.getTienePreguntas();
            planesBloquesVistaActivity.redireccionarCuestionario(idBlockDeta, tienePreguntas);
        });
    }

    @Override
    public int getItemCount() {
        if(modeloMisPlanesBloqueDetalles != null){
            return modeloMisPlanesBloqueDetalles.size();
        }else{
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitulo;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
        }
    }
}