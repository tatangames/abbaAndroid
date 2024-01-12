package com.tatanstudios.abba.fragmentos.login;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.login.ReseteoPasswordActivity;
import com.tatanstudios.abba.extras.OnFragmentInteractionTema;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentCodigoPassword extends Fragment {

    private TextView txtTitulo, txtReintento;
    private String correo = "";


    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextInputLayout inputCodigo;
    private TextInputEditText edtCodigo;
    private RelativeLayout rootRelative;
    private ImageView imgFlechaAtras;

    private ProgressBar progressBar;

    private TokenManager tokenManager;
    private Button btnEnviarCodigo;

    private ColorStateList colorStateTintGrey, colorStateTintWhite, colorStateTintBlack;

    private int colorBlanco, colorBlack = 0;

    private OnFragmentInteractionTema mListener;

    private boolean temaActual; // false: light  true: dark;

    private boolean boolDialogEnviar;

    private CountDownTimer countDownTimer;

    private boolean puedeReenviarCodigo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_codigo_password, container, false);

        txtTitulo = vista.findViewById(R.id.txtMensaje);
        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);
        btnEnviarCodigo = vista.findViewById(R.id.btnEnviar);

        inputCodigo = vista.findViewById(R.id.inputCodigo);

        edtCodigo = vista.findViewById(R.id.edtCodigo);
        rootRelative = vista.findViewById(R.id.rootRelative);
        txtReintento = vista.findViewById(R.id.txtReintento);

        puedeReenviarCodigo = false;

        Bundle bundle = getArguments();
        if(bundle != null) {
            correo = bundle.getString("CORREO");
        }

        String mensaje = String.format(getString(R.string.ingresar_el_codigo_que_se), correo);
        txtTitulo.setText(mensaje);


        service = RetrofitBuilder.createServiceNoAuth(ApiService.class);
        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));

        boolDialogEnviar = true;

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.GONE);

        colorBlanco = ContextCompat.getColor(requireContext(), R.color.white);
        colorBlack = ContextCompat.getColor(requireContext(), R.color.black);
        int colorGris = ContextCompat.getColor(requireContext(), R.color.colorGrisBtnDisable);

        temaActual = mListener.onFragmentInteraction();

        colorStateTintGrey = ColorStateList.valueOf(colorGris);
        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);


        btnEnviarCodigo.setEnabled(false);

        if(temaActual){ // dark
            btnEnviarCodigo.setBackgroundTintList(colorStateTintGrey);
            btnEnviarCodigo.setTextColor(colorBlanco);
        }else{
            btnEnviarCodigo.setBackgroundTintList(colorStateTintGrey);
            btnEnviarCodigo.setTextColor(colorBlanco);
        }


        // volver atras
        imgFlechaAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnEnviarCodigo.setOnClickListener(v -> {
            closeKeyboard();
            verificarDatos();
        });

        txtReintento.setOnClickListener(v -> {
           if(puedeReenviarCodigo){
               puedeReenviarCodigo = false;
               closeKeyboard();
               apiReenviarCodigo();
           }
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                verificarEntrada();
            }
        };

        edtCodigo.addTextChangedListener(textWatcher);
        return vista;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configurar y comenzar el contador (10 segundos en este ejemplo)
        iniciarContador();
    }

    private void iniciarContador() {

        String texto = getString(R.string.reintentar_en);

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long tiempoRestante) {
                // Actualizar el TextView con el tiempo restante
                txtReintento.setText(texto + ": " + tiempoRestante / 1000);
            }

            @Override
            public void onFinish() {
                // Actualizar el TextView cuando el contador llega a cero
                txtReintento.setText(getString(R.string.reenviar_codigo));
                puedeReenviarCodigo = true;
            }
        };

        // Iniciar el contador
        countDownTimer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Detener el contador para evitar fugas de memoria
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void apiReenviarCodigo(){

        progressBar.setVisibility(View.VISIBLE);

        int idioma = tokenManager.getToken().getIdioma();

        compositeDisposable.add(
                service.solicitarCodigoPassword(correo, idioma)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {
                                           // correo no encontrado, pero no caera aqui
                                        }
                                        else if(apiRespuesta.getSuccess() == 2){
                                            reintentoEnviado();
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

    private void reintentoEnviado(){
        Toasty.success(getContext(), getString(R.string.codigo_enviado), Toasty.LENGTH_SHORT).show();
        iniciarContador();
    }

    private void verificarDatos(){

        if(boolDialogEnviar){
            boolDialogEnviar = false;
            String txtCorreo = Objects.requireNonNull(edtCodigo.getText()).toString();
            KAlertDialog pDialog = new KAlertDialog(getContext(), KAlertDialog.NORMAL_TYPE, false);

            pDialog.setTitleText(getString(R.string.enviar_codigo));
            pDialog.setTitleTextGravity(Gravity.CENTER);
            pDialog.setTitleTextSize(19);

            pDialog.setContentText(txtCorreo);
            pDialog.setContentTextAlignment(View.TEXT_ALIGNMENT_VIEW_START, Gravity.START);
            pDialog.setContentTextSize(17);

            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.confirmButtonColor(R.drawable.kalert_dialog_corners_confirmar);
            pDialog.setConfirmClickListener(getString(R.string.enviar), sDialog -> {
                sDialog.dismissWithAnimation();
                boolDialogEnviar = true;
                apiEnviarCodigo();
            });

            pDialog.cancelButtonColor(R.drawable.kalert_dialog_corners_cancelar);
            pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
                sDialog.dismissWithAnimation();
                boolDialogEnviar = true;
            });
            pDialog.show();
        }
    }


    // VERIFICAR EN SERVER CODIGO, CORREO QUE COINCIDA
    private void apiEnviarCodigo(){

        progressBar.setVisibility(View.VISIBLE);

        String txtCodigo = Objects.requireNonNull(edtCodigo.getText()).toString();

        compositeDisposable.add(
                service.verificarCodigoCorreo(txtCodigo, correo)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {
                                            // verificado

                                            String idUser = apiRespuesta.getId();
                                            vistaCambiarPassword(idUser);

                                        }
                                        else if(apiRespuesta.getSuccess() == 2){
                                            // codigo no coincide
                                            codigoNoValido();
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

    private void vistaCambiarPassword(String idUser){

        Intent reseteo = new Intent(getContext(), ReseteoPasswordActivity.class);
        reseteo.putExtra("ID", idUser);
        startActivity(reseteo);
        getActivity().finish();
    }




    private void codigoNoValido(){
        Toasty.error(getContext(), getString(R.string.codigo_incorrecto), Toasty.LENGTH_SHORT).show();
    }

    private void verificarEntrada(){
        String txtCodigo = Objects.requireNonNull(edtCodigo.getText()).toString();

        if(TextUtils.isEmpty(txtCodigo)){
            desactivarBoton();
        }else{
            activarBoton();
        }
    }

    private void activarBoton(){

        btnEnviarCodigo.setEnabled(true);

        if(temaActual){ // Dark
            btnEnviarCodigo.setBackgroundTintList(colorStateTintWhite);
            btnEnviarCodigo.setTextColor(colorBlack);
        }else{
            btnEnviarCodigo.setBackgroundTintList(colorStateTintBlack);
            btnEnviarCodigo.setTextColor(colorBlanco);
        }
    }

    private void desactivarBoton(){
        btnEnviarCodigo.setEnabled(false);

        if(temaActual){ // Dark
            btnEnviarCodigo.setBackgroundTintList(colorStateTintGrey);
            btnEnviarCodigo.setTextColor(colorBlanco);
        }else{
            btnEnviarCodigo.setBackgroundTintList(colorStateTintGrey);
            btnEnviarCodigo.setTextColor(colorBlanco);
        }
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


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
