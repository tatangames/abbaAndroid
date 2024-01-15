package com.tatanstudios.abba.network;

import com.tatanstudios.abba.modelos.perfil.ModeloAjustes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesContenedor;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesTitulo;
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


    // solicitar codigo para recuperacion de contraseña
    @POST("app/solicitar/codigo/contrasena")
    @FormUrlEncoded
    Observable<ModeloUsuario> solicitarCodigoPassword(@Field("correo") String correo,
                                                      @Field("idioma") int idioma);

    // verificar en servidor si codigo y correo coinciden

    @POST("app/verificar/codigo/recuperacion")
    @FormUrlEncoded
    Observable<ModeloUsuario> verificarCodigoCorreo(@Field("codigo") String codigo,
                                                      @Field("correo") String correo);


    // actualizar una nueva contrasena, con usuario se obtiene usuario
    @POST("app/actualizar/nueva/contrasena/reseteo")
    @FormUrlEncoded
    Observable<ModeloUsuario> actualizarPasswordReseteo(@Field("password") String password);


    // actualizar contraseana en editar perfil

    @POST("app/actualizar/contrasena/")
    @FormUrlEncoded
    Observable<ModeloUsuario> actualizarPassword(@Field("iduser") String iduser,
                                                 @Field("password") String password);



    // buscar planes que
    @POST("app/buscar/planes/nuevos")
    @FormUrlEncoded
    Observable<ModeloPlanesContenedor> buscarPlanesNuevos(@Field("iduser") String iduser,
                                                          @Field("idiomaplan") int idiomaplan);




}
