package com.tatanstudios.abba.activitys.login;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.extras.OnFragmentInteractionTema;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ReseteoPasswordActivity extends AppCompatActivity {


    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextInputLayout inputContrasena;
    private TextInputEditText edtContrasena;
    private RelativeLayout rootRelative;
    private ImageView imgFlechaAtras;

    private ProgressBar progressBar;

    private TokenManager tokenManager;
    private Button btnEnviar;

    private ColorStateList colorStateTintGrey, colorStateTintWhite, colorStateTintBlack;

    private int colorBlanco, colorBlack = 0;


    private boolean temaActual; // false: light  true: dark;

    private boolean boolDialogEnviar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reseteo_password);

        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        btnEnviar = findViewById(R.id.btnEnviar);

        inputContrasena = findViewById(R.id.inputContrasena);

        edtContrasena = findViewById(R.id.edtContrasena);
        rootRelative = findViewById(R.id.rootRelative);

        tokenManager = TokenManager.getInstance(getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        boolDialogEnviar = true;

        int colorProgress = ContextCompat.getColor(this, R.color.colorProgress);

        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        colorBlanco = ContextCompat.getColor(this, R.color.white);
        colorBlack = ContextCompat.getColor(this, R.color.black);
        int colorGris = ContextCompat.getColor(this, R.color.colorGrisBtnDisable);

        temaActual = isDarkModeEnabled();

        colorStateTintGrey = ColorStateList.valueOf(colorGris);
        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);


        btnEnviar.setEnabled(false);




        // volver atras
        imgFlechaAtras.setOnClickListener(v -> {
            vistaAtras();
        });

        btnEnviar.setOnClickListener(v -> {
            closeKeyboard();
            verificarDatos();
        });

        String valiContrasena = getString(R.string.contrasena_minimo_6);

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
                        activarBoton();
                    }else{
                        inputContrasena.setError(valiContrasena);
                        desactivarBoton();
                    }
                }else{
                    inputContrasena.setError(null);
                }
            }
        });

        // Registrar un callback para gestionar los eventos de pulsación del botón de retroceso
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                vistaAtras();
            }
        };

        // Agregar el callback al BackPressedDispatcher
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void activarBoton(){

        btnEnviar.setEnabled(true);

        if(temaActual){ // Dark
            btnEnviar.setBackgroundTintList(colorStateTintWhite);
            btnEnviar.setTextColor(colorBlack);
        }else{
            btnEnviar.setBackgroundTintList(colorStateTintBlack);
            btnEnviar.setTextColor(colorBlanco);
        }
    }

    private void desactivarBoton(){
        btnEnviar.setEnabled(false);

        if(temaActual){ // Dark
            btnEnviar.setBackgroundTintList(colorStateTintGrey);
            btnEnviar.setTextColor(colorBlanco);
        }else{
            btnEnviar.setBackgroundTintList(colorStateTintGrey);
            btnEnviar.setTextColor(colorBlanco);
        }
    }


    private void verificarDatos(){

        if(boolDialogEnviar){
            boolDialogEnviar = false;

            progressBar.setVisibility(View.VISIBLE);

            String txtPassword = Objects.requireNonNull(edtContrasena.getText()).toString();

            compositeDisposable.add(
                    service.actualizarPasswordReseteo(txtPassword)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);
                                        boolDialogEnviar = true;

                                        if(apiRespuesta != null) {

                                            if(apiRespuesta.getSuccess() == 1) {
                                              // actualizado
                                                vistaAtras();
                                            }
                                            else{
                                                mensajeSinConexion();
                                            }
                                        }else{
                                            mensajeSinConexion();
                                        }
                                    },
                                    throwable -> {
                                        boolDialogEnviar = true;
                                        mensajeSinConexion();
                                    })
            );
        }
    }

    private boolean isDarkModeEnabled() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        boolean valor;

        switch (currentNightMode){
            case Configuration.UI_MODE_NIGHT_NO:
                valor = false;
                break;

            case Configuration.UI_MODE_NIGHT_YES:
                valor = true;
                break;
            default:
                valor = false;
                break;
        }

        return valor;
    }


    private void vistaAtras(){
        Intent data = new Intent(this, LoginActivity.class);
        startActivity(data);
        finish();
    }


    void mensajeSinConexion(){
        progressBar.setVisibility(View.GONE);
        Toasty.error(this, getString(R.string.error_intentar_de_nuevo)).show();
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