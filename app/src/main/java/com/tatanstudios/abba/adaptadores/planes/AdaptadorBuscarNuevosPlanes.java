package com.tatanstudios.abba.adaptadores.planes;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.fragmentos.menu.FragmentMas;
import com.tatanstudios.abba.fragmentos.planes.FragmentBuscarPlanes;
import com.tatanstudios.abba.modelos.mas.ModeloFraMasConfig;
import com.tatanstudios.abba.modelos.mas.ModeloFraMasPerfil;
import com.tatanstudios.abba.modelos.mas.ModeloFragmentMas;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesTitulo;
import com.tatanstudios.abba.modelos.planes.planesmodelo.ModeloVistasBuscarPlanes;

import java.util.ArrayList;

public class AdaptadorBuscarNuevosPlanes extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    // adaptador para carrito de compras

    private Context context;
    public ArrayList<ModeloVistasBuscarPlanes> modeloVistasBuscarPlanes;
    private FragmentBuscarPlanes fragmentBuscarPlanes;

    private ColorStateList colorStateListBlack, colorStateListWhite;

    RequestOptions opcionesGlide = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .placeholder(R.drawable.camaradefecto)
            .priority(Priority.NORMAL);

    public AdaptadorBuscarNuevosPlanes(Context context, ArrayList<ModeloVistasBuscarPlanes> modeloVistasBuscarPlanes, FragmentBuscarPlanes fragmentBuscarPlanes){
        this.context = context;
        this.fragmentBuscarPlanes = fragmentBuscarPlanes;
        this.modeloVistasBuscarPlanes = modeloVistasBuscarPlanes;

        int colorWhite = context.getColor(R.color.white);
        colorStateListWhite = ColorStateList.valueOf(colorWhite);

        int colorBlack = context.getColor(R.color.black);
        colorStateListBlack = ColorStateList.valueOf(colorBlack);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ModeloVistasBuscarPlanes.TIPO_TITULO) {
            View view = inflater.inflate(R.layout.cardview_titulo_buscar_planes, parent, false);
            return new HolderVistaTitulo(view);

        } else if (viewType == ModeloVistasBuscarPlanes.TIPO_PLANES) {
            View view = inflater.inflate(R.layout.cardview_mis_planes, parent, false);
            return new HolderVistaPlanes(view);
        }

        throw new IllegalArgumentException("Tipo de vista desconocido");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModeloVistasBuscarPlanes modelo = modeloVistasBuscarPlanes.get(position);

        if (holder instanceof HolderVistaTitulo) {
            // Configurar el ViewHolder para un ítem normal
            ModeloPlanesTitulo m = modelo.getModeloPlanesTitulo();

            ((HolderVistaTitulo) holder).txtTitulo.setText(m.getTitulo());

            holder.itemView.setOnClickListener(view -> {
                // Acciones cuando se toca la vista de un ítem
                //fragmentMas.editarPerfil();
            });

        }
        else if (holder instanceof HolderVistaPlanes) {

            ModeloPlanes m = modelo.getModeloPlanes();

            ((HolderVistaPlanes) holder).textViewTitulo.setText(m.getTitulo());



            holder.itemView.setOnClickListener(view -> {
                // Acciones cuando se toca la vista de un ítem normal
                //handleItemClick(modelo);
            });



        }
    }



    // HOLDER PARA VISTA TITULO
    private static class HolderVistaTitulo extends RecyclerView.ViewHolder{

        private TextView txtTitulo;
        private ImageView imgFlechaDerecha;

        public HolderVistaTitulo(@NonNull View itemView) {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.textViewTitulo);
            imgFlechaDerecha = itemView.findViewById(R.id.flechaVerTodos);

        }
    }



    private static class HolderVistaPlanes extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación

        private ImageView imageView;
        private TextView textViewTitulo;
        private ProgressBar progressBar;

        public HolderVistaPlanes(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí

            imageView = itemView.findViewById(R.id.imageView);
            textViewTitulo = itemView.findViewById(R.id.textViewTitulo);
            progressBar = itemView.findViewById(R.id.progressBar);


        }
    }

    @Override
    public int getItemCount() {
        if(modeloVistasBuscarPlanes != null){
            return modeloVistasBuscarPlanes.size();
        }else{
            return 0;
        }
    }

    public int getItemViewType(int position) {
        return modeloVistasBuscarPlanes.get(position).getTipoVista();
    }




    // Método para manejar el clic en un ítem normal
    private void handleItemClick(ModeloFragmentMas _modelo) {
        // Realizar acciones según el modelo del ítem normal
        // Por ejemplo, mostrar detalles, etc.

        //ModeloFraMasConfig m = _modelo.getModeloFraMasConfig();
        //modeloVistasBuscarPlanes.verPosicion(m.getIdentificador());
    }













}