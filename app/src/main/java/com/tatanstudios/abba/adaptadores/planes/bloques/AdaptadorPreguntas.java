package com.tatanstudios.abba.adaptadores.planes.bloques;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.mas.AdaptadorFragmentMas;
import com.tatanstudios.abba.fragmentos.planes.FragmentMisPlanes;
import com.tatanstudios.abba.fragmentos.planes.cuestionario.FragmentPreguntasPlanBloque;
import com.tatanstudios.abba.modelos.misplanes.preguntas.ModeloPreguntas;
import com.tatanstudios.abba.modelos.misplanes.preguntas.ModeloVistasPreguntas;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.misplanes.ModeloVistasMisPlanes;
import com.tatanstudios.abba.modelos.planes.planesmodelo.ModeloVistasBuscarPlanes;
import com.tatanstudios.abba.network.RetrofitBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdaptadorPreguntas extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {


    private Context context;
    public ArrayList<ModeloVistasPreguntas> modeloVistasPreguntas;
    private FragmentPreguntasPlanBloque fragmentPreguntasPlanBloque;

    private String tituloP = "";
    private String descripcionP = "";

    ColorStateList colorStateListBlack, colorStateListWhite;
    private boolean temaActual;



    private Map<Integer, TextInputLayout> txtInputMap = new HashMap<>();
    private Map<Integer, Integer> txtInputMapRequerido = new HashMap<>();



    public AdaptadorPreguntas(Context context, ArrayList<ModeloVistasPreguntas> modeloVistasPreguntas,
                              FragmentPreguntasPlanBloque fragmentPreguntasPlanBloque, String tituloP, String descripcionP, boolean temaActual){
        this.context = context;
        this.fragmentPreguntasPlanBloque = fragmentPreguntasPlanBloque;
        this.modeloVistasPreguntas = modeloVistasPreguntas;
        this.tituloP = tituloP;
        this.temaActual = temaActual;
        this.descripcionP = descripcionP;

        int colorWhite = context.getColor(R.color.white);
        colorStateListWhite = ColorStateList.valueOf(colorWhite);

        int colorBlack = context.getColor(R.color.black);
        colorStateListBlack = ColorStateList.valueOf(colorBlack);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ModeloVistasPreguntas.TIPO_IMAGEN) {
            View view = inflater.inflate(R.layout.cardview_bloque_portada_preguntas, parent, false);
            return new HolderVistaImagen(view);

        }
        else if (viewType == ModeloVistasPreguntas.TIPO_TITULOP) {
            View view = inflater.inflate(R.layout.cardview_titulo_principal_pregunta, parent, false);
            return new HolderVistaTitular(view);

        } else if (viewType == ModeloVistasPreguntas.TIPO_PREGUNTA) {
            View view = inflater.inflate(R.layout.cardview_bloque_pregunta, parent, false);
            return new HolderVistaBloquePregunta(view);
        } else if (viewType == ModeloVistasPreguntas.TIPO_BOTON) {
            View view = inflater.inflate(R.layout.cardview_btn_guardar_preguntas, parent, false);
            return new HolderVistaBotonGuardar(view);
        }

        throw new IllegalArgumentException("Tipo de vista desconocido");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ModeloVistasPreguntas modelo = modeloVistasPreguntas.get(position);

        // TITULAR
        if (holder instanceof HolderVistaTitular) {

            if(tituloP != null && !TextUtils.isEmpty(tituloP)){
                ((HolderVistaTitular) holder).txtTitulo.setText(HtmlCompat.fromHtml(tituloP, HtmlCompat.FROM_HTML_MODE_LEGACY));
                ((HolderVistaTitular) holder).txtTitulo.setVisibility(View.VISIBLE);
            }else{
                ((HolderVistaTitular) holder).txtTitulo.setVisibility(View.GONE);
            }

            if(descripcionP != null && !TextUtils.isEmpty(descripcionP)){
                ((HolderVistaTitular) holder).txtDescription.setText(HtmlCompat.fromHtml(descripcionP, HtmlCompat.FROM_HTML_MODE_LEGACY));
                ((HolderVistaTitular) holder).txtDescription.setVisibility(View.VISIBLE);
            }else{
                ((HolderVistaTitular) holder).txtDescription.setVisibility(View.GONE);
            }


            ((HolderVistaTitular) holder).txtTitulo.setOnClickListener(v -> {
                fragmentPreguntasPlanBloque.redireccionarBiblia();
            });

        }

        // BLOQUE PREGUNTAS
        else if (holder instanceof HolderVistaBloquePregunta) {

            ModeloPreguntas m = modelo.getModeloPreguntas();
            ((HolderVistaBloquePregunta) holder).txtPregunta.setText(m.getTitulo());


            txtInputMap.put(m.getId(), ((HolderVistaBloquePregunta) holder).txtInput);

            txtInputMapRequerido.put(m.getId(), m.getRequerido());

            if(m.getTexto() != null && !TextUtils.isEmpty(m.getTexto())){
                ((HolderVistaBloquePregunta) holder).txtEdt.setText(m.getTexto());
            }


            // CAMBIO DE IMAGEN
            if(m.getIdImagenPregunta() == 1){ // biblia
                ((HolderVistaBloquePregunta) holder).iconImageView.setImageResource(R.drawable.p_biblia);
            }else if(m.getIdImagenPregunta() == 2){ // cuaderno
                ((HolderVistaBloquePregunta) holder).iconImageView.setImageResource(R.drawable.p_cuaderno);
            }else if(m.getIdImagenPregunta() == 3){ // iglesia
                ((HolderVistaBloquePregunta) holder).iconImageView.setImageResource(R.drawable.p_iglesia);
            }else if(m.getIdImagenPregunta() == 4){ // trabajo
                ((HolderVistaBloquePregunta) holder).iconImageView.setImageResource(R.drawable.p_trabajo);
            }else{
                ((HolderVistaBloquePregunta) holder).iconImageView.setImageResource(R.drawable.p_biblia);
            }



            ((HolderVistaBloquePregunta) holder).txtEdt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if(TextUtils.isEmpty(s)){
                        if(m.getRequerido() == 1){
                            String texto = context.getString(R.string.campo_requerido);
                            ((HolderVistaBloquePregunta) holder).txtInput.setError(texto);
                        }
                    }else{
                        ((HolderVistaBloquePregunta) holder).txtInput.setError(null);
                    }

                }
            });
        }

        else if (holder instanceof HolderVistaBotonGuardar) {

            if(temaActual){ // dark
                ((HolderVistaBotonGuardar) holder).btnGuardar.setBackgroundTintList(colorStateListWhite);
                ((HolderVistaBotonGuardar) holder).btnGuardar.setTextColor(colorStateListBlack);
            }else{
                ((HolderVistaBotonGuardar) holder).btnGuardar.setBackgroundTintList(colorStateListBlack);
                ((HolderVistaBotonGuardar) holder).btnGuardar.setTextColor(colorStateListWhite);
            }

            if(fragmentPreguntasPlanBloque.isYaHabiaGuardado()){
                ((HolderVistaBotonGuardar) holder).btnGuardar.setText(context.getString(R.string.actualizar));
            }else{
                ((HolderVistaBotonGuardar) holder).btnGuardar.setText(context.getString(R.string.guardar));
            }

            ((HolderVistaBotonGuardar) holder).btnGuardar.setOnClickListener(v -> {

                if(fragmentPreguntasPlanBloque.isYaHabiaGuardado()){
                    fragmentPreguntasPlanBloque.verificarDatosActualizar();
                }else{
                    fragmentPreguntasPlanBloque.verificarDatosGuardar();
                }

            });

        }
    }


    private static class HolderVistaImagen extends RecyclerView.ViewHolder{



        public HolderVistaImagen(@NonNull View itemView) {
            super(itemView);

        }
    }


    // HOLDER PARA VISTA TITULO
    private static class HolderVistaTitular extends RecyclerView.ViewHolder{

        private TextView txtTitulo;
        private TextView txtDescription;

        public HolderVistaTitular(@NonNull View itemView) {
            super(itemView);

            txtTitulo = itemView.findViewById(R.id.txtTituloP);
            txtDescription = itemView.findViewById(R.id.texto);
        }
    }



    private static class HolderVistaBloquePregunta extends RecyclerView.ViewHolder {
        // Definir los elementos de la interfaz gráfica según el layout de la línea de separación

        private TextView txtPregunta;
        private TextInputLayout txtInput;
        private TextInputEditText txtEdt;

        private ImageView iconImageView;

        public HolderVistaBloquePregunta(@NonNull View itemView) {
            super(itemView);
            // Inicializar elementos de la interfaz gráfica aquí

            txtPregunta = itemView.findViewById(R.id.txtPregunta);
            txtInput = itemView.findViewById(R.id.inputNombre);
            txtEdt = itemView.findViewById(R.id.edtNombre);
            iconImageView = itemView.findViewById(R.id.iconImageView);

        }
    }



    private static class HolderVistaBotonGuardar extends RecyclerView.ViewHolder{

        private Button btnGuardar;

        public HolderVistaBotonGuardar(@NonNull View itemView) {
            super(itemView);

            btnGuardar = itemView.findViewById(R.id.btnGuardar);
        }
    }


    @Override
    public int getItemCount() {
        if(modeloVistasPreguntas != null){
            return modeloVistasPreguntas.size();
        }else{
            return 0;
        }
    }

    public int getItemViewType(int position) {
        return modeloVistasPreguntas.get(position).getTipoVista();
    }


    public boolean getBoolFromEditText(int uniqueId) {



        if (txtInputMap.containsKey(uniqueId)) {
            TextInputLayout editTextLayout = txtInputMap.get(uniqueId);
            TextInputEditText editText = (TextInputEditText) editTextLayout.getEditText();

            if(editText != null){
                if(TextUtils.isEmpty(editText.getText().toString())){
                    // buscar clave
                    if (txtInputMapRequerido.containsKey(uniqueId)) {

                        // obtener el valor
                        if(txtInputMapRequerido.get(uniqueId) == 1){
                            editTextLayout.setError(context.getString(R.string.campo_requerido));
                            return true;
                        }
                    }
                }
            }

            return false;
        } else {
            return false;
        }
    }


    public String getTextoFromEditText(int uniqueId) {

        if (txtInputMap.containsKey(uniqueId)) {
            TextInputLayout editTextLayout = txtInputMap.get(uniqueId);
            TextInputEditText editText = (TextInputEditText) editTextLayout.getEditText();

            if(editText != null){
                return editText.getText().toString();
            }

            return "";
        } else {
            return "";
        }
    }






}