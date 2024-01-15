package com.tatanstudios.abba.activitys.perfil;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.login.LoginActivity;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ActualizarPasswordActivity extends AppCompatActivity {


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

    private TextView txtToolbar;

    private OnBackPressedDispatcher onBackPressedDispatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_password);

        imgFlechaAtras = findViewById(R.id.imgFlechaAtras);
        btnEnviar = findViewById(R.id.btnEnviar);
        txtToolbar = findViewById(R.id.txtToolbar);

        inputContrasena = findViewById(R.id.inputContrasena);

        edtContrasena = findViewById(R.id.edtContrasena);
        rootRelative = findViewById(R.id.rootRelative);

        txtToolbar.setText(getString(R.string.editar));

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

        // Obtén una instancia de OnBackPressedDispatcher.
        onBackPressedDispatcher = getOnBackPressedDispatcher();

        colorBlanco = ContextCompat.getColor(this, R.color.white);
        colorBlack = ContextCompat.getColor(this, R.color.black);
        int colorGris = ContextCompat.getColor(this, R.color.colorGrisBtnDisable);

        temaActual = isDarkModeEnabled();

        colorStateTintGrey = ColorStateList.valueOf(colorGris);
        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);


        btnEnviar.setEnabled(false);

        btnEnviar.setBackgroundTintList(colorStateTintGrey);
        btnEnviar.setTextColor(colorBlanco);


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
            String iduser = tokenManager.getToken().getId();

            compositeDisposable.add(
                    service.actualizarPassword(iduser, txtPassword)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(apiRespuesta -> {

                                        progressBar.setVisibility(View.GONE);
                                        boolDialogEnviar = true;

                                        if(apiRespuesta != null) {

                                            if(apiRespuesta.getSuccess() == 1) {
                                                // actualizado
                                                vistaAtrasActualizado();
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
        onBackPressedDispatcher.onBackPressed();
    }

    private void vistaAtrasActualizado(){
        Toasty.success(this, getString(R.string.actualizado), Toasty.LENGTH_SHORT).show();
        onBackPressedDispatcher.onBackPressed();
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