package com.tatanstudios.abba.fragmentos.registro;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.login.LoginActivity;
import com.tatanstudios.abba.activitys.principal.PrincipalActivity;
import com.tatanstudios.abba.adaptadores.spinner.AdaptadorSpinnerGenero;
import com.tatanstudios.abba.adaptadores.spinner.AdaptadorSpinnerIglesia;
import com.tatanstudios.abba.adaptadores.spinner.AdaptadorSpinnerPais;
import com.tatanstudios.abba.adaptadores.spinner.AdaptadorSpinnerZona;
import com.tatanstudios.abba.extras.CustomDatePickerDialog;
import com.tatanstudios.abba.extras.OnFragmentInteractionTema;
import com.tatanstudios.abba.modelos.registro.ModeloIglesias;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentRegistro extends Fragment {


    private ImageView imgFlechaAtras;

    private TextInputLayout inputNombre, inputApellido, inputCorreo, inputContrasena;
    private TextInputEditText edtNombre, edtApellido, edtCorreo, edtContrasena;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ProgressBar progressBar;
    private Button btnRegistro;
    private RelativeLayout rootRelative;
    private boolean boolCorreo, boolContrasena = false;
    private TokenManager tokenManager;

    private Spinner spinGenero, spinPais, spinEstado, spinCiudad;

    private List<ModeloIglesias> iglesiasList;

    private int idSpinnerIglesia = 0;


    private static final int ELSalvador_SantaAna_Iglesia1 = 1;

    private TextView txtFecha;

    private static final String CERO = "0";
    private static final String BARRA = "-";


    private String fechaNacimiento = "";
    private boolean hayFecha = false;


    private OnFragmentInteractionTema mListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_registro, container, false);


        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);

        inputNombre = vista.findViewById(R.id.inputNombre);
        inputApellido = vista.findViewById(R.id.inputApellido);
        inputCorreo = vista.findViewById(R.id.inputCorreo);
        inputContrasena = vista.findViewById(R.id.inputContrasena);

        edtNombre = vista.findViewById(R.id.edtNombre);
        edtApellido = vista.findViewById(R.id.edtApellido);
        edtCorreo = vista.findViewById(R.id.edtCorreo);
        edtContrasena = vista.findViewById(R.id.edtContrasena);
        rootRelative = vista.findViewById(R.id.rootRelative);

        spinGenero = vista.findViewById(R.id.generoSpinner);
        spinPais = vista.findViewById(R.id.paisSpinner);
        spinEstado = vista.findViewById(R.id.estadoSpinner);
        spinCiudad = vista.findViewById(R.id.ciudadSpinner);
        txtFecha = vista.findViewById(R.id.txtCalendario);

        iglesiasList = new ArrayList<>();

        btnRegistro = vista.findViewById(R.id.btnRegistro);
        btnRegistro.setEnabled(false);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        if(tokenManager.getToken().getTema() == 1){
            inputNombre.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.white));
            inputNombre.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);
            inputApellido.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.white));
            inputApellido.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);
            inputCorreo.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.white));
            inputCorreo.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);
            inputContrasena.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.white));
            inputContrasena.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_FILLED);
        }


        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);


        txtFecha.setOnClickListener(v -> {
            elegirFecha();
        });



        // volver atras
        imgFlechaAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });


        btnRegistro.setOnClickListener(v -> {
            closeKeyboard();
            confirmarRegistro();
        });

        // llenar los spinner
        llenarSpinner();

        edtNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto está a punto de cambiar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto ha cambiado
            }

            @Override
            public void afterTextChanged(Editable editable) {

                verificarEntradas();

            }
        });

        edtApellido.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto está a punto de cambiar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto ha cambiado
            }

            @Override
            public void afterTextChanged(Editable editable) {

                verificarEntradas();
            }
        });

        edtCorreo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto está a punto de cambiar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto ha cambiado

            }

            @Override
            public void afterTextChanged(Editable editable) {

                String textoIngresado = editable.toString();

                if(!Patterns.EMAIL_ADDRESS.matcher(textoIngresado).matches()){
                    String valiCorreo = getString(R.string.direccion_correo_invalida);
                    inputCorreo.setError(valiCorreo);
                    boolCorreo = false;
                }else{
                    boolCorreo = true;
                    inputCorreo.setError(null);
                }

                verificarEntradas();
            }
        });


        edtContrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto está a punto de cambiar
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Este método se llama para notificar que el texto ha cambiado
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Este método se llama después de que el texto ha cambiado
                String textoIngresado = editable.toString();
                // Hacer algo con el texto ingresado
                if (!textoIngresado.isEmpty()) {

                    if(textoIngresado.length() >= 6){
                        inputContrasena.setError(null);
                        boolContrasena = true;
                    }else{
                        String valiContrasena = getString(R.string.contrasena_minimo_6);
                        inputContrasena.setError(valiContrasena);
                        boolContrasena = false;
                    }
                }else{
                    inputContrasena.setError(null);
                    boolContrasena = false;
                }

                verificarEntradas();
            }
        });


        // *************************************************************************


        return vista;
    }

    private void elegirFecha(){

        closeKeyboard();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);


        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(
                getContext(),
                (view, year1, month1, dayOfMonth1) -> {
                    //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                    final int mesActual = month1 + 1;
                    //Formateo el día obtenido: antepone el 0 si son menores de 10
                    String diaFormateado = (dayOfMonth1 < 10)? CERO + String.valueOf(dayOfMonth1):String.valueOf(dayOfMonth1);
                    //Formateo el mes obtenido: antepone el 0 si son menores de 10
                    String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                    //Muestro la fecha con el formato deseado
                    txtFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year1);

                    fechaNacimiento = year1 + BARRA + mesFormateado + BARRA + diaFormateado;
                    hayFecha = true;

                },
                year,
                month,
                dayOfMonth
        );

        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.si), datePickerDialog);

        datePickerDialog.show();

        //datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.WHITE);


        //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
       /* DatePickerDialog recogerFecha = new DatePickerDialog(getActivity(),  (view, year, month, dayOfMonth) -> {


        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();*/
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionTema) {
            mListener = (OnFragmentInteractionTema) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }


    private boolean conocerTema() {
        // ...
        // Llama a la función de la Activity
        if (mListener != null) {

            boolean info = mListener.onFragmentInteraction();
           // Toasty.info(getContext(), "es: " + info, Toasty.LENGTH_SHORT).show();
            return info;
        }else{
            return false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    private void llenarSpinner(){

        // GENEROS

        String[] listaGeneros = getResources().getStringArray(R.array.generos_array);

        boolean infoTema = conocerTema();

        AdaptadorSpinnerGenero generoAdapter = new AdaptadorSpinnerGenero(getContext(), android.R.layout.simple_spinner_item, listaGeneros, infoTema);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinGenero.setAdapter(generoAdapter);


        // *********** PAISES ****************

        String[] listaPais = getResources().getStringArray(R.array.paises_array);

        AdaptadorSpinnerPais paisAdapter = new AdaptadorSpinnerPais(getContext(), android.R.layout.simple_spinner_item, listaPais);
        paisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinPais.setAdapter(paisAdapter);

        // *********** ESTADOS *****************

        AdaptadorSpinnerZona adapterEstados = new AdaptadorSpinnerZona(getContext(), android.R.layout.simple_spinner_item);
        spinEstado.setAdapter(adapterEstados);


        // *********** IGLESIAS *****************


        AdaptadorSpinnerIglesia adapterIglesia = new AdaptadorSpinnerIglesia(getContext(), android.R.layout.simple_spinner_item);
        spinCiudad.setAdapter(adapterIglesia);


        //****************************************

        spinGenero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                closeKeyboard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Implementa según sea necesario
            }
        });



        // Configura el listener para el primer Spinner (Paises)

        spinPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Actualiza el conjunto de datos del segundo Spinner según la posición seleccionada

                updateAdapterEstado(adapterEstados, position);

                adapterIglesia.clear();
                spinEstado.setSelection(0);
                spinCiudad.setSelection(0);

                closeKeyboard();

                if(position == 0){
                    idSpinnerIglesia = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Implementa según sea necesario
            }
        });



        // Configura el listener para el segundo Spinner (Estados)
        spinEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Actualiza el adaptador del tercer Spinner (Ciudades) según el estado seleccionado
                updateCiudadesAdapter(adapterIglesia, spinPais.getSelectedItemPosition(), position);

                if(position == 0){
                    idSpinnerIglesia = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Implementa según sea necesario
            }
        });


        spinCiudad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // Hay una ciudad seleccionada en el Spinner
                ModeloIglesias iglesiaSeleccionada = iglesiasList.get(position);
                idSpinnerIglesia = iglesiaSeleccionada.getId();
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });
    }


    private void updateAdapterEstado(AdaptadorSpinnerZona adapter, int position) {
        adapter.clear();

        switch (position) {
            case 1: // pais el salvador -> estados
                String[] estadosElSalvador = getResources().getStringArray(R.array.estados_elsalvador_array);
                adapter.addAll(estadosElSalvador);

                break;
            case 2: // pais guatemala -> estados
                String[] estadosElSalvador2 = getResources().getStringArray(R.array.estados_guatemala_array);
                adapter.addAll(estadosElSalvador2);

                break;
            // Agrega más casos según la jerarquía deseada
        }
    }

    private void updateCiudadesAdapter(AdaptadorSpinnerIglesia adapter, int paisPosition, int estadoPosition) {
        adapter.clear();
        iglesiasList.clear();
        if (paisPosition == 0 || estadoPosition == 0) {
            // No hay país o estado seleccionado, no se cargan ciudades
            return;
        }

        switch (paisPosition) {
            case 1: // PAIS EL SALVADOR

                switch (estadoPosition) { // POSICION DE ESTADOS
                    case 1: // santa ana

                        iglesiasList.add(new ModeloIglesias(ELSalvador_SantaAna_Iglesia1, getString(R.string.elsalvador_santaana_id_1)));
                        iglesiasList.add(new ModeloIglesias(2, getString(R.string.elsalvador_santaana_id_2)));

                        break;
                    case 2: // metapan
                        iglesiasList.add(new ModeloIglesias(3, getString(R.string.elsalvador_metapan_id_3)));
                        iglesiasList.add(new ModeloIglesias(4, getString(R.string.elsalvador_metapan_id_4)));
                        iglesiasList.add(new ModeloIglesias(5, getString(R.string.elsalvador_metapan_id_5)));
                        break;
                    // Agrega más casos según la jerarquía deseada
                    default:
                       break;
                }
                break;

            case 2: // PAIS GUATEMALA

                switch (estadoPosition) { // POSICION DE ESTADOS
                    case 1: // chiquimula
                        iglesiasList.add(new ModeloIglesias(6, getString(R.string.guatemala_chiquimula_id_6)));
                        iglesiasList.add(new ModeloIglesias(7, getString(R.string.guatemala_chiquimula_id_7)));
                        break;
                    case 2: // jalapa
                        iglesiasList.add(new ModeloIglesias(8, getString(R.string.guatemala_jalapa_id_8)));
                        iglesiasList.add(new ModeloIglesias(9, getString(R.string.guatemala_jalapa_id_9)));
                        iglesiasList.add(new ModeloIglesias(10, getString(R.string.guatemala_jalapa_id_10)));
                        break;
                    // Agrega más casos según la jerarquía deseada
                    default:
                        break;
                }
                break;
        }

        for (ModeloIglesias iglelista : iglesiasList) {
            adapter.add(iglelista.getNombre());
        }

        ModeloIglesias iglesiaSeleccionada = iglesiasList.get(0);
        idSpinnerIglesia = iglesiaSeleccionada.getId();
    }



    private void verificarEntradas(){

        String txtNombre = Objects.requireNonNull(edtNombre.getText()).toString();
        String txtApellido = Objects.requireNonNull(edtApellido.getText()).toString();
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
        String txtContrasena = Objects.requireNonNull(edtContrasena.getText()).toString();

        if(TextUtils.isEmpty(txtNombre) || TextUtils.isEmpty(txtApellido)
                || TextUtils.isEmpty(txtCorreo) || TextUtils.isEmpty(txtContrasena)){

            desactivarBtnRegistro();
        }else{

            if(boolCorreo && boolContrasena){

                activarBtnRegistro();
            }else{
                desactivarBtnRegistro();
            }
        }
    }


    private void activarBtnRegistro(){
        btnRegistro.setEnabled(true);
        int colorBtnTextoBlanco = ContextCompat.getColor(requireContext(), R.color.white);
        btnRegistro.setBackgroundResource(R.drawable.boton_redondeado_negro);
        btnRegistro.setTextColor(colorBtnTextoBlanco);
    }

    private void desactivarBtnRegistro(){
        btnRegistro.setEnabled(false);
        int colorBtnTextoGris = ContextCompat.getColor(requireContext(), R.color.colorGrisBotonRegistroTexto);
        btnRegistro.setBackgroundResource(R.drawable.boton_redondeado_gris);
        btnRegistro.setTextColor(colorBtnTextoGris);
    }

    private void confirmarRegistro(){

        if(!hayFecha){
            Toasty.error(getContext(), getString(R.string.fecha_nacimiento_es_requerido)).show();
            return;
        }

        if(spinGenero.getSelectedItemPosition() == 0){
            Toasty.error(getContext(), getString(R.string.genero_es_requerido)).show();
            return;
        }


        if(idSpinnerIglesia == 0){
            Toasty.error(getContext(), getString(R.string.iglesia_es_requerido)).show();
            return;
        }

        KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.SUCCESS_TYPE);
        pDialog.setTitleText(getString(R.string.completar_registro));
        pDialog.setContentText("");
        pDialog.setConfirmText(getString(R.string.si));
        pDialog.setContentTextSize(16);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    registrarUsuario();
                });
        pDialog.cancelButtonColor(R.drawable.dialogo_theme_cancel)
                .setContentTextSize(16)
                .setCancelText(getString(R.string.editar))
                .setCancelClickListener(kAlertDialog -> kAlertDialog.dismissWithAnimation());
        pDialog.show();
    }

    private void registrarUsuario(){

        progressBar.setVisibility(View.VISIBLE);

        String version = getString(R.string.version_app);
        String txtNombre = Objects.requireNonNull(edtNombre.getText()).toString();
        String txtApellido = Objects.requireNonNull(edtApellido.getText()).toString();
        int idGenero = spinGenero.getSelectedItemPosition(); // 1: masculino, 2: femenino
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
        String txtContrasena = Objects.requireNonNull(edtContrasena.getText()).toString();
        String idOneSignal = "1234";

        compositeDisposable.add(
                service.registroUsuario(txtNombre, txtApellido, fechaNacimiento, idGenero,
                                idSpinnerIglesia, txtCorreo, txtContrasena, idOneSignal, version)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                            progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                            correoYaRegistrado(txtCorreo);

                                        }
                                        else if(apiRespuesta.getSuccess() == 2){

                                            tokenManager.guardarClienteID(apiRespuesta);
                                            finalizar();

                                        }else{
                                            mensajeSinConexion();
                                        }
                                    }else{
                                        mensajeSinConexion();
                                    }
                                },
                                throwable -> {
                                    mensajeSinConexion();
                                })
        );
    }

    private void correoYaRegistrado(String correo){
        KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.WARNING_TYPE);
        pDialog.setTitleText(getString(R.string.correo_ya_registrado));
        pDialog.setContentText(correo);
        pDialog.setConfirmText(getString(R.string.aceptar));
        pDialog.setContentTextSize(16);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.confirmButtonColor(R.drawable.dialogo_theme_success)
                .setConfirmClickListener(KAlertDialog::dismissWithAnimation);
        pDialog.show();
    }

    void finalizar(){
        Toasty.success(getActivity(), getString(R.string.registrado_correctamente)).show();

        edtNombre.setText("");
        edtApellido.setText("");
        edtCorreo.setText("");
        edtContrasena.setText("");

        inputNombre.setError(null);
        inputApellido.setError(null);
        inputCorreo.setError(null);
        inputContrasena.setError(null);

        // Siguiente Actvity
        Intent intent = new Intent(getActivity(), PrincipalActivity.class);
        startActivity(intent);

        // Animación personalizada de entrada
        getActivity().overridePendingTransition(R.anim.slide_in_right_activity, R.anim.slide_out_left_activity);
        getActivity().finish();
    }

    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(getActivity(), getString(R.string.error_intentar_de_nuevo)).show();
    }


    @Override
    public void onDestroy(){
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        if(compositeDisposable != null){
            compositeDisposable.clear();
        }
        super.onStop();
    }

    private void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
