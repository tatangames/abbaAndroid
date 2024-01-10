package com.tatanstudios.abba.fragmentos.login;



import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;


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
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.developer.kalert.KAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tatanstudios.abba.R;
import com.tatanstudios.abba.activitys.principal.PrincipalActivity;
import com.tatanstudios.abba.extras.OnFragmentInteractionTema;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.Objects;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentOlvidePassword extends Fragment {

    private ApiService service;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TextInputLayout inputCorreo;
    private TextInputEditText edtCorreo;
    private RelativeLayout rootRelative;
    private ImageView imgFlechaAtras;

    private ProgressBar progressBar;

    private TokenManager tokenManager;
    private Button btnEnviarCorreo;

    private ColorStateList colorStateTintGrey, colorStateTintWhite, colorStateTintBlack;

    private int colorBlanco, colorBlack = 0;

    private OnFragmentInteractionTema mListener;

    private boolean temaActual; // false: light  true: dark;

    private boolean boolDialogEnviar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_olvide_password, container, false);

        imgFlechaAtras = vista.findViewById(R.id.imgFlechaAtras);
        btnEnviarCorreo = vista.findViewById(R.id.btnEnviar);

        inputCorreo = vista.findViewById(R.id.inputCorreo);

        edtCorreo = vista.findViewById(R.id.edtCorreo);
        rootRelative = vista.findViewById(R.id.rootRelative);

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


        btnEnviarCorreo.setEnabled(false);

        if(temaActual){ // dark
            btnEnviarCorreo.setBackgroundTintList(colorStateTintGrey);
            btnEnviarCorreo.setTextColor(colorBlanco);
        }else{
            btnEnviarCorreo.setBackgroundTintList(colorStateTintGrey);
            btnEnviarCorreo.setTextColor(colorBlanco);
        }


        // volver atras
        imgFlechaAtras.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnEnviarCorreo.setOnClickListener(v -> {
            verificarDatos();
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

        edtCorreo.addTextChangedListener(textWatcher);

        return vista;
    }


    private void verificarDatos(){

        if(boolDialogEnviar){
            boolDialogEnviar = false;
            String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();
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
            });

            pDialog.cancelButtonColor(R.drawable.kalert_dialog_corners_cancelar);
            pDialog.setCancelClickListener(getString(R.string.cancelar), sDialog -> {
                sDialog.dismissWithAnimation();
                boolDialogEnviar = true;
            });
            pDialog.show();
        }
    }



    private void verificarEntrada(){
        String txtCorreo = Objects.requireNonNull(edtCorreo.getText()).toString();

        if(!Patterns.EMAIL_ADDRESS.matcher(txtCorreo).matches()){
            String valiCorreo = getString(R.string.direccion_correo_invalida);
            inputCorreo.setError(valiCorreo);
           desactivarBoton();
        }else{
            inputCorreo.setError(null);
            activarBoton();
        }
    }

    private void activarBoton(){

        btnEnviarCorreo.setEnabled(true);

        if(temaActual){ // Dark
            btnEnviarCorreo.setBackgroundTintList(colorStateTintWhite);
            btnEnviarCorreo.setTextColor(colorBlack);
        }else{
            btnEnviarCorreo.setBackgroundTintList(colorStateTintBlack);
            btnEnviarCorreo.setTextColor(colorBlanco);
        }
    }

    private void desactivarBoton(){
        btnEnviarCorreo.setEnabled(false);

        if(temaActual){ // Dark
            btnEnviarCorreo.setBackgroundTintList(colorStateTintGrey);
            btnEnviarCorreo.setTextColor(colorBlanco);
        }else{
            btnEnviarCorreo.setBackgroundTintList(colorStateTintGrey);
            btnEnviarCorreo.setTextColor(colorBlanco);
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
