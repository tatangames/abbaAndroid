package com.tatanstudios.abba.network;

import com.tatanstudios.abba.modelos.usuario.ModeloUsuario;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {


    @GET("app/refran/login")
    Observable<ModeloUsuario> getRefranLogin();


    @POST("app/login")
    @FormUrlEncoded
    Observable<ModeloUsuario> inicioSesion(@Field("correo") String correo,
                                             @Field("password") String password,
                                             @Field("idfirebase") String idfirebase);



    // registro de nuevo cliente
    @POST("app/registro/usuario")
    @FormUrlEncoded
    Observable<ModeloUsuario> registroUsuario(@Field("nombre") String nombre,
                                                @Field("apellido") String apellido,
                                                @Field("edad") String edad,
                                                @Field("genero") int idGenero,
                                                @Field("iglesia") int idIglesia,
                                                @Field("correo") String correo,
                                                @Field("password") String password,
                                                @Field("onesignal") String idOneSignal,
                                                @Field("version") String version);

}
