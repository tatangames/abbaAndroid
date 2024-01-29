package com.tatanstudios.abba.network;

import com.tatanstudios.abba.modelos.inicio.ModeloContenedorInicio;
import com.tatanstudios.abba.modelos.inicio.bloques.videos.ModeloInicioVideos;
import com.tatanstudios.abba.modelos.insignias.ModeloContenedorInsignias;
import com.tatanstudios.abba.modelos.misplanes.ModeloMisPlanesContenedor;
import com.tatanstudios.abba.modelos.misplanes.preguntas.ModeloPreguntasContenedor;
import com.tatanstudios.abba.modelos.perfil.ModeloAjustes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanes;
import com.tatanstudios.abba.modelos.planes.ModeloPlanesContenedor;
import com.tatanstudios.abba.modelos.planes.completados.ModeloPlanesCompletosPaginateApi;
import com.tatanstudios.abba.modelos.planes.completados.ModeloPlanesCompletadoContenedor;
import com.tatanstudios.abba.modelos.planes.completados.ModeloPlanesCompletosPaginateRequest;
import com.tatanstudios.abba.modelos.planes.cuestionario.ModeloCuestionario;
import com.tatanstudios.abba.modelos.rachas.ModeloRachas;
import com.tatanstudios.abba.modelos.usuario.ModeloUsuario;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
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


    // ver informacion de un plan para poder seleccionarlo
    @POST("app/plan/seleccionado/informacion")
    @FormUrlEncoded
    Observable<ModeloPlanes> informacionPlanSeleccionado(@Field("idplan") int idplan,
                                                          @Field("idiomaplan") int idiomaplan);



    // devuelve lista de planes que no he seleccionado aun, por id contenedor
    @POST("app/plan/listado/planes/contenedor")
    @FormUrlEncoded
    Observable<ModeloPlanesContenedor> listadoPlanesContenedor(@Field("idiomaplan") int idiomaplan,
                                                     @Field("iduser") String iduser,
                                                     @Field("idcontenedor") int idcontenedor);

    // iniciar plan nuevo
    @POST("app/plan/nuevo/seleccionar")
    @FormUrlEncoded
    Observable<ModeloUsuario> seleccionarPlanNuevo(@Field("idplan") int idplan,
                                                               @Field("iduser") String iduser);


    // listado de mis planes seleccionado
    @POST("app/plan/listado/misplanes")
    @FormUrlEncoded
    Observable<ModeloPlanesContenedor> listadoMisPlanes(@Field("iduser") String iduser,
                                                   @Field("idiomaplan") int idiomaplan);


    // bloque de un Plan
    @POST("app/plan/misplanes/informacion/bloque")
    @FormUrlEncoded
    Observable<ModeloMisPlanesContenedor> informacionPlanBloque(@Field("iduser") String iduser,
                                                                @Field("idiomaplan") int idiomaplan,
                                                                @Field("idplan") int idplan);


    // guardar check o actualizar bloque detalle plan
    @POST("app/plan/misplanes/actualizar/check")
    @FormUrlEncoded
    Observable<ModeloUsuario> actualizarPlanBloqueDetalle(@Field("iduser") String iduser,
                                                                @Field("idblockdeta") int idBlockDeta,
                                                                @Field("valor") int valor,
                                                                @Field("idplan") int idplan);


    // buscar informacion del cuestionario de cada bloque detalle
    @POST("app/plan/misplanes/cuestionario/bloque")
    @FormUrlEncoded
    Observable<ModeloCuestionario> informacionCuestionarioBloqueDetalle(@Field("iduser") String iduser,
                                                                        @Field("idblockdeta") int idBlockDeta,
                                                                        @Field("idioma") int idioma);

    // informacion de todas las preguntas de un bloque detalle
    @POST("app/plan/misplanes/preguntas/bloque")
    @FormUrlEncoded
    Observable<ModeloPreguntasContenedor> informacionPreguntasBloqueDetalle(@Field("iduser") String iduser,
                                                                            @Field("idblockdeta") int idBlockDeta,
                                                                            @Field("idioma") int idioma);




    // actualizar las preguntas
    @POST("app/plan/misplanes/preguntas/usuario/actualizar")
    @FormUrlEncoded
    Observable<ModeloPreguntasContenedor> actualizarPreguntasUsuarioPlanes(@Field("iduser") String iduser,
                                                                           @Field("idblockdeta") int idBlockDeta,
                                                                           @FieldMap Map<String, String> listado);


    // listado de planes completados
   /* @POST("app/plan/misplanes/completados")
    @FormUrlEncoded
    Observable<ModeloPlanesContenedor> listadoMisPlanesCompletados(@Field("iduser") String iduser,
                                                                   @Field("idiomaplan") int idiomaplan);
*/


    @POST("app/plan/misplanes/completados")
    Observable<ModeloPlanesCompletosPaginateApi<ModeloPlanesCompletadoContenedor>> listadoMisPlanesCompletados(
            @Body ModeloPlanesCompletosPaginateRequest request);


    @POST("app/plan/misplanes/info/bloque/vista")
    @FormUrlEncoded
    Observable<ModeloMisPlanesContenedor> informacionPlanBloqueVista(@Field("iduser") String iduser,
                                                                @Field("idiomaplan") int idiomaplan,
                                                                @Field("idplan") int idplan);

    @POST("app/inicio/bloque/completa")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> informacionBloqueInicio(@Field("iduser") String iduser,
                                                               @Field("idiomaplan") int idiomaplan);


    // informacion de un plan pero su vista
    @POST("app/plan/informacion/solovista")
    @FormUrlEncoded
    Observable<ModeloPlanes> informacionPlanSoloVista(@Field("idblockdeta") int idblockdeta,
                                                      @Field("idiomaplan") int idiomaplan);


    // es para guardar preguntas, registrar en el plan sino lo estoy, y setear check aunque no se mire
    // en esta parte en inicio cuestionario
    @POST("app/plan/inicio/preguntas/guardar/actualizar")
    @FormUrlEncoded
    Observable<ModeloPreguntasContenedor> guardarPreguntasCuestionarioInicio(@Field("iduser") String iduser,
                                                                        @Field("idblockdeta") int idBlockDeta,
                                                                        @FieldMap Map<String, String> listado);

    // obtener listado de todos los videos
    @POST("app/inicio/todos/losvideos")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> obtenerTodosLosVideos(@Field("iduser") String iduser,
                                                         @Field("idiomaplan") int idiomaplan);

    // obtener listado de todos las imagenes
    @POST("app/inicio/todos/lasimagenes")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> obtenerTodosLasImagenes(@Field("iduser") String iduser);

    // informacion de la insignia seleccionada
    @POST("app/insignia/individual/informacion")
    @FormUrlEncoded
    Observable<ModeloContenedorInsignias> informacionInsigniaSeleccionada(@Field("iduser") String iduser,
                                                                          @Field("idiomaplan") int idiomaplan,
                                                                          @Field("idinsignia") int idinsignia);

    // obtener listado de todas las insignias
    @POST("app/inicio/todos/lasinsignias")
    @FormUrlEncoded
    Observable<ModeloContenedorInicio> obtenerTodosLasInsignias(@Field("iduser") String iduser,
                                                                @Field("idiomaplan") int idiomaplan);







    //Observable<ModeloPlanesContenedor> listadoMisPlanesCompletados(@Body PaginationRequest request);
}
