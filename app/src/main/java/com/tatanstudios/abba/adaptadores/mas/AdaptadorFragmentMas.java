package com.tatanstudios.abba.adaptadores.mas;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.extras.IOnRecyclerViewClickListener;
import com.tatanstudios.abba.fragmentos.menu.FragmentMas;
import com.tatanstudios.abba.modelos.mas.ModeloFraMasConfig;
import com.tatanstudios.abba.modelos.mas.ModeloFraMasPerfil;
import com.tatanstudios.abba.modelos.mas.ModeloFragmentMas;

import java.util.ArrayList;
import java.util.List;

public class AdaptadorFragmentMas extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    // adaptador para carrito de compras

    private Context context;
    public ArrayList<ModeloFragmentMas> modeloFragmentMas;
    private FragmentMas fragmentMas;

    public AdaptadorFragmentMas(Context context, ArrayList<ModeloFragmentMas> modeloFragmentMas, FragmentMas fragmentMas){
        this.context = context;
        this.fragmentMas = fragmentMas;
        this.modeloFragmentMas = modeloFragmentMas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ModeloFragmentMas.TIPO_PERFIL) {
            View view = inflater.inflate(R.layout.cardview_letra, parent, false);
            return new PerfilViewHolder(view);
        }
        else if (viewType == ModeloFragmentMas.TIPO_ITEM_NORMAL) {
            View view = inflater.inflate(R.layout.cardview_fragment_mas, parent, false);
            return new ItemNormalViewHolder(view);

        } else if (viewType == ModeloFragmentMas.TIPO_LINEA_SEPARACION) {
            View view = inflater.inflate(R.layout.cardview_linea_separacion, parent, false);
            return new LineaSeparacionViewHolder(view);
        }

        throw new IllegalArgumentException("Tipo de vista desconocido");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModeloFragmentMas modelo = modeloFragmentMas.get(position);

        if (holder instanceof PerfilViewHolder) {
            // Configurar el ViewHolder para un ítem normal
            ModeloFraMasPerfil m = modelo.getModeloFraMasPerfil();

            ((PerfilViewHolder) holder).txtLetra.setText(m.getLetra());
            ((PerfilViewHolder) holder).txtNombre.setText(m.getNombrePerfil());

            holder.itemView.setOnClickListener(view -> {
                // Acciones cuando se toca la vista de un ítem
                fragmentMas.editarPerfil();
            });

        }
        else if (holder instanceof ItemNormalViewHolder) {
            // Configurar el ViewHolder para un ítem normal
             //((ItemNormalViewHolder) holder).bind(modelo);


            ModeloFraMasConfig m = modelo.getModeloFraMasConfig();

            ((ItemNormalViewHolder) holder).txtPerfil.setText(m.getNombre());

            switch (m.getIdentificador()){

                case 1:
                    ((ItemNormalViewHolder) holder).imgPerfil.setImageResource(R.drawable.icono_campana);
                    break;

                case 2:
                    ((ItemNormalViewHolder) holder).imgPerfil.setImageResource(R.drawable.icono_location);
                    break;
                default:
                    ((ItemNormalViewHolder) holder).imgPerfil.setImageResource(R.drawable.icono_global);
                    break;
            }


            holder.itemView.setOnClickListener(view -> {
                // Acciones cuando se toca la vista de un ítem normal
                handleItemClick(modelo);
            });



        } else if (holder instanceof LineaSeparacionViewHolder) {
            // Configurar el ViewHolder para una línea de separación




        }
    }



    private static class PerfilViewHolder extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación

        TextView txtLetra;
        TextView txtNombre;

        public PerfilViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí

            txtLetra = itemView.findViewById(R.id.txtLetra);
            txtNombre = itemView.findViewById(R.id.txtNombre);

        }
    }




    // Definir ViewHolder para ítem normal
    private static class ItemNormalViewHolder extends RecyclerView.ViewHolder{
        // Definir los elementos de la interfaz gráfica según el layout de ítem normal

        TextView txtPerfil;
        ImageView imgPerfil;

        public ItemNormalViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí

            imgPerfil = itemView.findViewById(R.id.imgIcono);
            txtPerfil = itemView.findViewById(R.id.txtTexto);

        }

        // Agregar métodos para enlazar datos, si es necesario
    }

    // Definir ViewHolder para línea de separación
    private static class LineaSeparacionViewHolder extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación



        public LineaSeparacionViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí


        }
    }

    @Override
    public int getItemCount() {
        if(modeloFragmentMas != null){
            return modeloFragmentMas.size();
        }else{
            return 0;
        }
    }

    public int getItemViewType(int position) {
        return modeloFragmentMas.get(position).getTipoVista();
    }




    // Método para manejar el clic en un ítem normal
    private void handleItemClick(ModeloFragmentMas _modelo) {
        // Realizar acciones según el modelo del ítem normal
        // Por ejemplo, mostrar detalles, etc.

        ModeloFraMasConfig m = _modelo.getModeloFraMasConfig();
        fragmentMas.verPosicion(m.getIdentificador());
    }













}