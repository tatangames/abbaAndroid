package com.tatanstudios.abba.fragmentos.registro;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.principal.PrincipalActivity;
import com.tatanstudios.abba.adaptadores.spinner.AdaptadorSpinnerGenero;
import com.tatanstudios.abba.adaptadores.spinner.AdaptadorSpinnerPais;
import com.tatanstudios.abba.adaptadores.spinner.AdaptadorSpinnerZona;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.http.Field;

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

    private Spinner spinGenero, spinPais, spinZona, iglesiaSpinner;

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
        spinZona = vista.findViewById(R.id.zonaSpinner);
        iglesiaSpinner = vista.findViewById(R.id.iglesiaSpinner);


        btnRegistro = vista.findViewById(R.id.btnRegistro);
        btnRegistro.setEnabled(false);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);


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

    private void llenarSpinner(){

        String[] listaGeneros = getResources().getStringArray(R.array.generos_array);

        AdaptadorSpinnerGenero generoAdapter = new AdaptadorSpinnerGenero(getContext(), android.R.layout.simple_spinner_item, listaGeneros);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinGenero.setAdapter(generoAdapter);


        // ***************************

        String[] listaPais = getResources().getStringArray(R.array.pais_array);

        AdaptadorSpinnerPais paisAdapter = new AdaptadorSpinnerPais(getContext(), android.R.layout.simple_spinner_item, listaPais);
        paisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinPais.setAdapter(paisAdapter);


        // ***************************

        // Define los conjuntos de datos para ambos Spinners
        final String[] pais = getResources().getStringArray(R.array.pais_array);
        final Map<String, String[]> subZonasMap = new HashMap<>();
        subZonasMap.put(pais[0], getResources().getStringArray(R.array.vacio_array));
        subZonasMap.put(pais[1], getResources().getStringArray(R.array.areas_elsalvador_array));
        subZonasMap.put(pais[2], getResources().getStringArray(R.array.areas_guatemala_array));


        ArrayAdapter<String> subZonaAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{});
        subZonaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinZona.setAdapter(subZonaAdapter);


        spinPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Actualiza el conjunto de datos del segundo Spinner según la posición seleccionada
                String selectedGenero = pais[position];
                String[] subGeneros = subZonasMap.get(selectedGenero);
                updateSubGeneroSpinner(spinZona, subGeneros);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Implementa según sea necesario
            }
        });



        //**************************

       /* final String[] zonas = getResources().getStringArray(R.array.pais_array);
        final Map<String, String[]> subZonasMap = new HashMap<>();
        subZonasMap.put(zonas[0], getResources().getStringArray(R.array.vacio_array));
        subZonasMap.put(zonas[1], getResources().getStringArray(R.array.areas_elsalvador_array));
        subZonasMap.put(zonas[2], getResources().getStringArray(R.array.areas_guatemala_array));
        */



    }

    private void updateSubGeneroSpinner(Spinner subZonasSpinner, String[] subZonas) {
        // Actualiza el adaptador del segundo Spinner con el nuevo conjunto de datos
        /*ArrayAdapter<String> subGeneroAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, subGeneros);
        subGeneroAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subGeneroSpinner.setAdapter(subGeneroAdapter);*/

        AdaptadorSpinnerZona zonaAdapter = new AdaptadorSpinnerZona(getContext(), R.layout.spinner_item_layout, subZonas);
        zonaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subZonasSpinner.setAdapter(zonaAdapter);
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

        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();

        KAlertDialog pDialog = new KAlertDialog(getActivity(), KAlertDialog.NORMAL_TYPE);
        pDialog.setTitleText(getString(R.string.es_correcta_esta_direccion));
        pDialog.setContentText(txtCorreo);
        pDialog.setConfirmText(getString(R.string.se_ve_genial));
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
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
        String txtContrasena = Objects.requireNonNull(edtContrasena.getText()).toString();
        String idfirebase = "1234";

        compositeDisposable.add(
                service.registroUsuario(txtNombre, txtApellido, txtCorreo, txtContrasena, idfirebase, version)
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
