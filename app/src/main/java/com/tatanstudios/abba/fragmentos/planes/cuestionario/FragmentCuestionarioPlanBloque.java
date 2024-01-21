package com.tatanstudios.abba.fragmentos.planes.cuestionario;

import static android.content.Context.MODE_PRIVATE;
import static android.text.Html.FROM_HTML_MODE_COMPACT;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tatanstudios.abba.R;
import androidx.core.text.HtmlCompat;

import com.tatanstudios.abba.adaptadores.spinner.AdaptadorSpinnerGenero;
import com.tatanstudios.abba.adaptadores.spinner.AdaptadorSpinnerIglesia;
import com.tatanstudios.abba.adaptadores.spinner.cuestionario.AdaptadorSpinnerTipoLetra;
import com.tatanstudios.abba.modelos.registro.ModeloGeneros;
import com.tatanstudios.abba.modelos.registro.ModeloIglesias;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentCuestionarioPlanBloque extends Fragment {


    private ProgressBar progressBar;
    private TokenManager tokenManager;

    private FloatingActionButton fabButton;

    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView txtHtml;

    private static final String ARG_DATO = "IDBLOQUE";

    private int idBloqueDeta = 0;

    private boolean bottomSheetShowing = false;

    private int tamanoTextoHtml = 18;
    private final int minTextSize = 14;
    private final int maxTextSize = 28;

    private int temaActual = 0;


    private Typeface faceAktivGrotesk;
    private Typeface faceRobotoSerif;
    private Typeface faceRobotoSans;

    private ColorStateList colorStateTintWhite, colorStateTintBlack;
    private int colorBlanco, colorBlack = 0;

    public static FragmentCuestionarioPlanBloque newInstance(int dato) {
        FragmentCuestionarioPlanBloque fragment = new FragmentCuestionarioPlanBloque();
        Bundle args = new Bundle();
        args.putInt(ARG_DATO, dato);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_cuestionario_plan_bloque_contenedor, container, false);

        rootRelative = vista.findViewById(R.id.rootRelative);
        txtHtml = vista.findViewById(R.id.txtCuestionario);
        fabButton = vista.findViewById(R.id.fabButton);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        idBloqueDeta = getArguments().getInt(ARG_DATO, 0);
        temaActual = tokenManager.getToken().getTema();

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        fabButton.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.white)));

        fabButton.setOnClickListener(v -> {
            verOpciones();
        });

        int tipoLetra = tokenManager.getToken().getTipoLetra();

        colorBlanco = ContextCompat.getColor(requireContext(), R.color.white);
        colorBlack = ContextCompat.getColor(requireContext(), R.color.black);

        faceAktivGrotesk = ResourcesCompat.getFont(getContext(), R.font.aktivgro_medium);
        faceRobotoSerif = ResourcesCompat.getFont(getContext(), R.font.roboto_serif);
        faceRobotoSans = ResourcesCompat.getFont(getContext(), R.font.roboto_sans);

        if(tokenManager.getToken().getTema() == 1){ // dark
           txtHtml.setTextColor(colorBlanco);
        }else{
           txtHtml.setTextColor(colorBlack);
        }


        colorStateTintWhite = ColorStateList.valueOf(colorBlanco);
        colorStateTintBlack = ColorStateList.valueOf(colorBlack);


        switch (tipoLetra){
            case 0:
                // AKTIV GROTESK
                txtHtml.setTypeface(faceAktivGrotesk);
                break;
            case 1:
                // ROBOTO SERIF
                txtHtml.setTypeface(faceRobotoSerif);
                break;
            case 2:
                // ROBOTO SANS
                txtHtml.setTypeface(faceRobotoSans);
                break;
            default:
                txtHtml.setTypeface(faceAktivGrotesk);
                break;
        }


        apiBuscarCuestionario();

        return vista;
    }


    private void apiBuscarCuestionario(){

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        compositeDisposable.add(
                service.informacionCuestionarioBloqueDetalle(iduser, idBloqueDeta, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {


                                            setearTexto(apiRespuesta.getTitulo());
                                        }
                                        else if(apiRespuesta.getSuccess() == 2) {
                                            // BLOQUE NO TIENE CUESTIONARIO

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


    // carga opciones para el texto html
    private void verOpciones(){

        if (!bottomSheetShowing) {
            bottomSheetShowing = true;

            List<ModeloGeneros> estilosList = new ArrayList<>();

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext());
            View bottomSheetView = getLayoutInflater().inflate(R.layout.botton_sheet_opciones_html, null);
            bottomSheetDialog.setContentView(bottomSheetView);

            Button btnMenos = bottomSheetDialog.findViewById(R.id.btnMenos);
            Button btnMas = bottomSheetDialog.findViewById(R.id.btnMas);
            Spinner spinEstiloTexto = bottomSheetDialog.findViewById(R.id.estiloSpinner);


            AdaptadorSpinnerTipoLetra adapterModelo = new AdaptadorSpinnerTipoLetra(getContext(), android.R.layout.simple_spinner_item, temaActual);

            estilosList.add(new ModeloGeneros(0, "Roboto Sans"));
            estilosList.add(new ModeloGeneros(1, "Roboto Serif"));
            estilosList.add(new ModeloGeneros(2, "Aktiv Grotesk"));

            for (ModeloGeneros listado : estilosList) {
                adapterModelo.add(listado.getNombre());
            }

            spinEstiloTexto.setAdapter(adapterModelo);

            // CAMBIAR POSICION
            int posTexto = tokenManager.getToken().getTipoLetra();

            if (posTexto >= 0 && posTexto < spinEstiloTexto.getAdapter().getCount()) {
                spinEstiloTexto.setSelection(posTexto);
            }else{
                spinEstiloTexto.setSelection(0);
            }

            // GUARDAR TEXTO SELECCIONADO
            spinEstiloTexto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    if(position == 0){
                        tokenManager.guardarTipoLetraTexto(0);
                        txtHtml.setTypeface(faceAktivGrotesk);
                    }
                    else if(position == 1){
                        tokenManager.guardarTipoLetraTexto(1);
                        txtHtml.setTypeface(faceRobotoSerif);
                    }
                    else if(position == 2){
                        tokenManager.guardarTipoLetraTexto(2);
                        txtHtml.setTypeface(faceRobotoSans);
                    }else{
                        tokenManager.guardarTipoLetraTexto(0);
                        txtHtml.setTypeface(faceAktivGrotesk);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Implementa según sea necesario
                }
            });



            // CAMBIAR ESTILO BOTONES


            if(temaActual == 1){ // dark
                btnMas.setBackgroundTintList(colorStateTintWhite);
                btnMas.setTextColor(colorBlack);

                btnMenos.setBackgroundTintList(colorStateTintWhite);
                btnMenos.setTextColor(colorBlack);
            }else{
                btnMas.setBackgroundTintList(colorStateTintBlack);
                btnMas.setTextColor(colorBlanco);

                btnMenos.setBackgroundTintList(colorStateTintBlack);
                btnMenos.setTextColor(colorBlanco);
            }

            // TAMANOS DE TEXTOS

            btnMenos.setOnClickListener(v ->{
                if (tamanoTextoHtml > minTextSize) {
                    tamanoTextoHtml--;
                    updateTextSize();
                }
            });

            btnMas.setOnClickListener(v -> {
                if (tamanoTextoHtml < maxTextSize) {
                    tamanoTextoHtml++;
                    updateTextSize();
                }
            });

            // Configura un oyente para saber cuándo se cierra el BottomSheetDialog
            bottomSheetDialog.setOnDismissListener(dialog -> {
                bottomSheetShowing = false;
            });

            bottomSheetDialog.show();
        }
    }

    private void updateTextSize() {
        txtHtml.setTextSize(tamanoTextoHtml);
    }

    private void setearTexto(String texto){

        if(texto != null){
            if(!TextUtils.isEmpty(texto)){
                txtHtml.setText(HtmlCompat.fromHtml(texto, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        }
    }


    private void mensajeSinConexion(){
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
}
