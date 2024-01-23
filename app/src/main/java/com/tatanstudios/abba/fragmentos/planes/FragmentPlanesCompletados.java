package com.tatanstudios.abba.fragmentos.planes;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
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
import com.tatanstudios.abba.activitys.planes.completado.PlanesBloquesVistaActivity;
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

    private int currentPage = 1;
    private int lastPage = 1;
    private boolean unaVezVisibilidad = true;
    private boolean unaVezAdapter = true;

    private AdaptadorPlanesCompletados adapter;

    private boolean isLoading, puedeCargarYa = false;

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


        // Configura el LinearLayoutManager y el ScrollListener para manejar la paginación
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                /*int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (!isLoadingg() && !isLastPage()) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= 0) {
                        if(puedeCargarYa) {
                            apiBuscarMisPlanesCompletados();
                        }

                    }
                }*/

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (puedeCargarYa && !isLastPage()) {
                    // Verificar si no se está cargando y si ha llegado al final de la lista
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && dy > 0) {
                        // Iniciar la carga de más elementos
                        // isLoading = true;
                        // Cargar más elementos aquí

                        apiBuscarMisPlanesCompletados();
                    }
                }

            }
        });

        apiBuscarMisPlanesCompletados();

        return vista;
    }




    private boolean isLastPage(){
        if(currentPage == lastPage){
            return true;
        }else{
            return false;
        }
    }





    private void apiBuscarMisPlanesCompletados(){

        if(!isLoading){
            isLoading = true;

            progressBar.setVisibility(View.VISIBLE);

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

                                        progressBar.setVisibility(View.GONE);


                                        if(apiRespuesta.getSuccess() == 1){

                                            if(apiRespuesta.getHayinfo() == 1){
                                                if (!apiRespuesta.getData().getData().isEmpty()) {
                                                    List<ModeloPlanes> newData = apiRespuesta.getData().getData();

                                                    lastPage = apiRespuesta.getData().getLastPage();

                                                    if(unaVezAdapter){
                                                        setearAdapter(newData);
                                                    }
                                                    else{
                                                        adapter.addData(newData);
                                                    }

                                                    if(lastPage > 1){
                                                        currentPage++;
                                                    }

                                                    unaVezAdapter = false;
                                                    isLoading = false;
                                                    unaVezVisibilidad = false;
                                                    puedeCargarYa = true;
                                                }
                                            }else{
                                                if(unaVezVisibilidad){
                                                    unaVezVisibilidad = false;
                                                    recyclerView.setVisibility(View.GONE);
                                                    txtSinPlanes.setVisibility(View.VISIBLE);
                                                }
                                            }

                                        }else{
                                            mensajeSinConexion();
                                            isLoading = false;
                                        }
                                    },
                                    throwable -> {
                                        mensajeSinConexion();
                                        isLoading = false;
                                        String errorMessage = throwable.getMessage();
                                        Log.i("PORTADA", errorMessage);
                                    }
                            ));
        }
    }

    private void setearAdapter(List<ModeloPlanes> modeloPlanes) {
        adapter = new AdaptadorPlanesCompletados(getContext(), modeloPlanes, this);
        recyclerView.setAdapter(adapter);
    }


    public void verBloquePlanesVista(int id){

        Intent intent = new Intent(getActivity(), PlanesBloquesVistaActivity.class);
        intent.putExtra("ID", id);
        startActivity(intent);
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
