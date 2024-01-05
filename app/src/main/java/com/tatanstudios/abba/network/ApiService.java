package com.tatanstudios.abba.network;

import com.tatanstudios.abba.modelos.perfil.ModeloAjustes;
import com.tatanstudios.abba.modelos.usuario.ModeloUsuario;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {




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
                                                @Field("edad") String fechaNacimiento,
                                                @Field("genero") int idGenero,
                                                @Field("iglesia") int idIglesia,
                                                @Field("correo") String correo,
                                                @Field("password") String password,
                                                @Field("onesignal") String idOneSignal,
                                                @Field("version") String version);

    @POST("app/solicitar/listado/opcion/perfil")
    @FormUrlEncoded
    Observable<ModeloAjustes> informacionListadoAjuste(@Field("iduser") String idUsuario);


    @POST("app/solicitar/informacion/perfil")
    @FormUrlEncoded
    Observable<ModeloUsuario> informacionPerfil(@Field("iduser") String idUsuario);


    @POST("app/actualizar/perfil/usuario")
    @FormUrlEncoded
    Observable<ModeloUsuario> actualizarPerfilUsuario(@Field("iduser") String idUsuario,
                                              @Field("nombre") String nombre,
                                              @Field("apellido") String apellido,
                                              @Field("fechanac") String fechaNacimiento,
                                              @Field("correo") String correo);














}
