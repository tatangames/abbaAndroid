package com.tatanstudios.abba.adaptadores.planes.misplanes;

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
import com.tatanstudios.abba.activitys.planes.PlanesBloquesActivity;
import com.tatanstudios.abba.fragmentos.planes.FragmentMisPlanes;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesBloques;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesPortada;
import com.tatanstudios.abba.modelos.misplanes.ModeloVistasPlanesbloques;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.misplanes.ModeloVistasMisPlanes;
import com.tatanstudios.abba.modelos.planes.planesmodelo.ModeloVistasBuscarPlanes;
import com.tatanstudios.abba.network.RetrofitBuilder;

import java.util.ArrayList;

public class AdaptadorPlanBloque extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context context;
    public ArrayList<ModeloVistasPlanesbloques> modeloVistasPlanesbloques;
    private PlanesBloquesActivity planesBloquesActivity;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorPlanBloque(Context context, ArrayList<ModeloVistasPlanesbloques> modeloVistasPlanesbloques, PlanesBloquesActivity planesBloquesActivity){
        this.context = context;
        this.planesBloquesActivity = planesBloquesActivity;
        this.modeloVistasPlanesbloques = modeloVistasPlanesbloques;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ModeloVistasPlanesbloques.TIPO_PORTADA) {
            View view = inflater.inflate(R.layout.cardview_bloque_portada, parent, false);
            return new HolderVistaImagenPortada(view);
        }

        throw new IllegalArgumentException("Tipo de vista desconocido");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModeloVistasPlanesbloques modelo = modeloVistasPlanesbloques.get(position);

        if (holder instanceof HolderVistaImagenPortada) {
            // Configurar el ViewHolder para un ítem normal
            ModeloMisPlanesPortada m = modelo.getModeloMisPlanesPortada();

            // La imagen es la portada, pero se obtiene por imagen
            if(m.getImagen() != null && !TextUtils.isEmpty(m.getImagen())){
                Glide.with(context)
                        .load(RetrofitBuilder.urlImagenes + m.getImagen())
                        .apply(opcionesGlide)
                        .into(((HolderVistaImagenPortada) holder).imgPortada);
            }else{
                int resourceId = R.drawable.camaradefecto;
                Glide.with(context)
                        .load(resourceId)
                        .apply(opcionesGlide)
                        .into(((HolderVistaImagenPortada) holder).imgPortada);
            }


        }
        else if (holder instanceof HolderVistaRecyclerHorizontal) {


        }
    }



    // HOLDER VISTA IMAGEN PORTADA
    private static class HolderVistaImagenPortada extends RecyclerView.ViewHolder{

        private ImageView imgPortada;

        public HolderVistaImagenPortada(@NonNull View itemView) {
            super(itemView);

            imgPortada = itemView.findViewById(R.id.imgPortada);

        }
    }



    // HOLDER VISTA RECYCLER HORIZONTAL
    private static class HolderVistaRecyclerHorizontal extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación


        public HolderVistaRecyclerHorizontal(@NonNull View itemView) {
            super(itemView);


        }
    }


    // HOLDER VISTA RECYCLER VERTICAL
    private static class HolderVistaRecyclerVertical extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación



        public HolderVistaRecyclerVertical(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí



        }
    }

    @Override
    public int getItemCount() {
        if(modeloVistasPlanesbloques != null){
            return modeloVistasPlanesbloques.size();
        }else{
            return 0;
        }
    }

    public int getItemViewType(int position) {
        return modeloVistasPlanesbloques.get(position).getTipoVista();
    }













}