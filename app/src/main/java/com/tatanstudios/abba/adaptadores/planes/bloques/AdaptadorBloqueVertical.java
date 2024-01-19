package com.tatanstudios.abba.adaptadores.planes.bloques;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.planes.PlanesBloquesActivity;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloqueDetalle;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesReVertical;

import java.util.List;

public class AdaptadorBloqueVertical extends RecyclerView.Adapter<AdaptadorBloqueVertical.ViewHolder> {
    private List<ModeloMisPlanesBloqueDetalle> modeloMisPlanesBloqueDetalles;
    private Context context;
    private PlanesBloquesActivity planesBloquesActivity;

    private boolean tema;

    private ColorStateList colorStateWhite, colorStateBlack;


    public AdaptadorBloqueVertical(Context context, List<ModeloMisPlanesBloqueDetalle> modeloMisPlanesBloqueDetalles,
                                   PlanesBloquesActivity planesBloquesActivity, int temaInt) {
        this.context = context;
        this.modeloMisPlanesBloqueDetalles = modeloMisPlanesBloqueDetalles;
        this.planesBloquesActivity = planesBloquesActivity;
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
        View itemView = inflater.inflate(R.layout.cardview_bloque_item_vertical, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModeloMisPlanesBloqueDetalle m = modeloMisPlanesBloqueDetalles.get(position);

        if(tema){ // DARK
            holder.txtTitulo.setTextColor(colorStateWhite);
            int colorTint = ContextCompat.getColor(context, R.color.white);
            CompoundButtonCompat.setButtonTintList(holder.idCheck, ColorStateList.valueOf(colorTint));
        }else{
            holder.txtTitulo.setTextColor(colorStateBlack);
            int colorTint = ContextCompat.getColor(context, R.color.black);
            CompoundButtonCompat.setButtonTintList(holder.idCheck, ColorStateList.valueOf(colorTint));
        }

        if(m.getCompletado() == 1){
            holder.idCheck.setChecked(true);
        }else{
            holder.idCheck.setChecked(false);
        }

        holder.txtTitulo.setText(m.getTitulo());
        holder.idCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                int valor = 0;
                if(isChecked){ valor = 1; }
                m.setCompletado(valor);

                planesBloquesActivity.actualizarCheck(m.getId(), valor);
            }
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
        private CheckBox idCheck;

        ViewHolder(View itemView) {
            super(itemView);
            txtTitulo = itemView.findViewById(R.id.txtTitulo);
            idCheck = itemView.findViewById(R.id.idcheck);
        }
    }
}