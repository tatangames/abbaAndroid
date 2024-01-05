package com.tatanstudios.abba.activitys.perfil;

import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.Calendar;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class EditarPerfilActivity extends AppCompatActivity {

    private TextView txtToolbar, txtFechaNac;
    private ImageView imgFlechaAtras;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    private TextInputLayout inputNombre, inputApellido, inputCorreo, inputContrasena;
    private TextInputEditText edtNombre, edtApellido, edtCorreo, edtContrasena;

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    private ProgressBar progressBar;
    private Button btnActualizar;
    private RelativeLayout rootRelative;

    private boolean boolCorreo = true;
    private String fechaNacimiento = "";


    private TokenManager tokenManager;


    private static final String CERO = "0";
    private static final String BARRA = "-";

    //Calendario para obtener fecha & hora
    public final Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    private final int mes = c.get(Calendar.MONTH);
    private final int dia = c.get(Calendar.DAY_OF_MONTH);
    private final int anio = c.get(Calendar.YEAR);

    private LinearLayout linearContenedor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        txtToolbar = findViewById(R.id.txtToolbar);
        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        btnActualizar = findViewById(R.id.btnActualizar);
        rootRelative = findViewById(R.id.rootRelative);
        txtFechaNac = findViewById(R.id.txtCalendario);

        inputNombre = findViewById(R.id.inputNombre);
        inputApellido = findViewById(R.id.inputApellido);
        inputCorreo = findViewById(R.id.inputCorreo);

        edtNombre = findViewById(R.id.edtNombre);
        edtApellido = findViewById(R.id.edtApellido);
        edtCorreo = findViewById(R.id.edtCorreo);
        linearContenedor = findViewById(R.id.linearContenedor);



        txtToolbar.setText(getString(R.string.editar_perfil));

        int colorProgress = ContextCompat.getColor(this, R.color.colorProgress);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));

        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);



        imgFlechaAtras.setOnClickListener(v -> {
            volverAtras();
        });
        // Obtén una instancia de OnBackPressedDispatcher.
        onBackPressedDispatcher = getOnBackPressedDispatcher();

        txtFechaNac.setOnClickListener(v -> {
            elegirFecha();
        });

        btnActualizar.setOnClickListener(v -> {
            actualizarPerfil();
        });

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
















        solicitarPerfil();
    }

    private void solicitarPerfil(){

        String iduser = tokenManager.getToken().getId();

        compositeDisposable.add(
                service.informacionPerfil(iduser)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    linearContenedor.setVisibility(View.VISIBLE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {

                                           edtNombre.setText(apiRespuesta.getNombre());
                                           edtApellido.setText(apiRespuesta.getApellido());
                                           txtFechaNac.setText(apiRespuesta.getFechaNacimiento());
                                           edtCorreo.setText(apiRespuesta.getCorreo());
                                           fechaNacimiento = apiRespuesta.getFechaNacimientoRaw();
                                        }
                                        else{
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



    private void elegirFecha(){
        //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
        DatePickerDialog recogerFecha = new DatePickerDialog(this,  (view, year, month, dayOfMonth) -> {

            //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
            final int mesActual = month + 1;
            //Formateo el día obtenido: antepone el 0 si son menores de 10
            String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
            //Formateo el mes obtenido: antepone el 0 si son menores de 10
            String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
            //Muestro la fecha con el formato deseado
            txtFechaNac.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);

            fechaNacimiento = year + BARRA + mesFormateado + BARRA + diaFormateado;
        },anio, mes, dia);
        //Muestro el widget
        recogerFecha.show();
    }


    private void verificarEntradas(){

        String txtNombre = Objects.requireNonNull(edtNombre.getText()).toString();
        String txtApellido = Objects.requireNonNull(edtApellido.getText()).toString();
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();

        if(TextUtils.isEmpty(txtNombre) || TextUtils.isEmpty(txtApellido)
                || TextUtils.isEmpty(txtCorreo)){

            desactivarBtnActualizar();
        }else{

            if(boolCorreo){
                activarBtnActualizar();
            }else{
                desactivarBtnActualizar();
            }
        }
    }


    private void activarBtnActualizar(){
        btnActualizar.setEnabled(true);
        int colorBtnTextoBlanco = ContextCompat.getColor(this, R.color.white);
        btnActualizar.setBackgroundResource(R.drawable.boton_redondeado_negro);
        btnActualizar.setTextColor(colorBtnTextoBlanco);
    }

    private void desactivarBtnActualizar(){
        btnActualizar.setEnabled(false);
        int colorBtnTextoGris = ContextCompat.getColor(this, R.color.colorGrisBotonRegistroTexto);
        btnActualizar.setBackgroundResource(R.drawable.boton_redondeado_gris);
        btnActualizar.setTextColor(colorBtnTextoGris);
    }


    private void actualizarPerfil(){

    }









    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(this, getString(R.string.error_intentar_de_nuevo)).show();
    }

    private void volverAtras(){
        onBackPressedDispatcher.onBackPressed();
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
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}