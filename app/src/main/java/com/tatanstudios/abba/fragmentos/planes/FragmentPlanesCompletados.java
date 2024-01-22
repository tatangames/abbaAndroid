package com.tatanstudios.abba.fragmentos.planes;

import static android.content.Context.MODE_PRIVATE;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tatanstudios.abba.R;
import com.tatanstudios.abba.adaptadores.planes.completado.AdaptadorPlanesCompletados;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.completados.ModeloPlanesCompletosPaginateRequest;
import com.tatanstudios.abba.network.ApiService;
import com.tatanstudios.abba.network.RetrofitBuilder;
import com.tatanstudios.abba.network.TokenManager;

import java.util.List;

import es.dmoral.toasty.Toasty;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentPlanesCompletados extends Fragment {

    private ProgressBar progressBar;
    private TokenManager tokenManager;
    private RecyclerView recyclerView;
    private ApiService service;
    private RelativeLayout rootRelative;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private TextView txtSinPlanes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_planes_completados, container, false);

        recyclerView = vista.findViewById(R.id.recyclerView);
        rootRelative = vista.findViewById(R.id.rootRelative);
        txtSinPlanes = vista.findViewById(R.id.txtSinPlanes);

        tokenManager = TokenManager.getInstance(getActivity().getSharedPreferences("prefs", MODE_PRIVATE));
        service = RetrofitBuilder.createServiceAutentificacion(ApiService.class, tokenManager);

        int colorProgress = ContextCompat.getColor(requireContext(), R.color.colorProgress);

        progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100, 100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootRelative.addView(progressBar, params);
        // Aplicar el ColorFilter al Drawable del ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(colorProgress, PorterDuff.Mode.SRC_IN);

        apiBuscarMisPlanesCompletados();

        return vista;
    }

    private int currentPage = 1;

    private void apiBuscarMisPlanesCompletados(){

        String iduser = tokenManager.getToken().getId();
        int idiomaPlan = tokenManager.getToken().getIdiomaTextos();

        ModeloPlanesCompletosPaginateRequest paginationRequest = new ModeloPlanesCompletosPaginateRequest();
        paginationRequest.setPage(currentPage);
        paginationRequest.setLimit(10); // Por ejemplo, 10 elementos por página
        paginationRequest.setIdiomaplan(idiomaPlan);
        paginationRequest.setIduser(Integer.valueOf(iduser));

        compositeDisposable.add(
                service.listadoMisPlanesCompletados(paginationRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        apiRespuesta -> {


                            if(apiRespuesta.getSuccess() == 1){

                                if(apiRespuesta.getHayinfo() == 1){
                                    List<ModeloPlanes> newData = apiRespuesta.getData().getData();

                                    for (ModeloPlanes m : newData){

                                    }
                                }else{
                                    recyclerView.setVisibility(View.GONE);
                                    txtSinPlanes.setVisibility(View.VISIBLE);
                                }

                            }else{
                                mensajeSinConexion();
                            }

                            //}
                            //adapter.addData(newData);

                            // Incrementar la página actual para la próxima carga
                            currentPage++;
                        },
                        throwable -> {
                            // Manejar errores de la petición
                            // ...

                            String errorMessage = throwable.getMessage();
                            Log.e("PORTADA", "Error en la petición: " + errorMessage);
                            Log.i("PORTADA", "que pedo");
                        }
                ));



       /* compositeDisposable.add(
                service.listadoMisPlanesCompletados(iduser, idiomaPlan)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry()
                        .subscribe(apiRespuesta -> {

                                    progressBar.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);

                                    if(apiRespuesta != null) {

                                        if(apiRespuesta.getSuccess() == 1) {


                                            if(apiRespuesta.getHayinfo() == 1){

                                                setearAdapter(apiRespuesta.getModeloPlanes());
                                            }else{
                                                recyclerView.setVisibility(View.GONE);
                                                txtSinPlanes.setVisibility(View.VISIBLE);
                                            }
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
        );*/
    }

    private void setearAdapter(List<ModeloPlanes> modeloPlanes) {
        AdaptadorPlanesCompletados adapter = new AdaptadorPlanesCompletados(getContext(), modeloPlanes, this);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    public void verBloquePlanes(int id){

        /*Intent intent = new Intent(getActivity(), PlanesBloquesActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);*/
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
