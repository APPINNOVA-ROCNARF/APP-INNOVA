package com.rocnarf.rocnarf.api;

import com.rocnarf.rocnarf.HistorialComentariosResponse;
import com.rocnarf.rocnarf.RecetasXResponse;
import com.rocnarf.rocnarf.models.ArchivosGuiaProducto;
import com.rocnarf.rocnarf.models.CarteraCliente;
import com.rocnarf.rocnarf.models.ComentariosClientes;
import com.rocnarf.rocnarf.models.DetalleNotaCredito;
import com.rocnarf.rocnarf.models.EspecialidadMedicos;
import com.rocnarf.rocnarf.models.Estadistica;
import com.rocnarf.rocnarf.models.FichaMedico;
import com.rocnarf.rocnarf.models.GuiaProductos;
import com.rocnarf.rocnarf.models.GuiaProductosResponse;
import com.rocnarf.rocnarf.models.HistorialViatico;
import com.rocnarf.rocnarf.models.HistorialVisitas;
import com.rocnarf.rocnarf.models.LiquidacionObsequio;
import com.rocnarf.rocnarf.models.LiquidacionObsequioResponse;
import com.rocnarf.rocnarf.models.MedicosCumpleanyos;
import com.rocnarf.rocnarf.models.MedicosCumpleanyosResponse;
import com.rocnarf.rocnarf.models.MedicosEvaluacion;
import com.rocnarf.rocnarf.models.ParametrosResponse;
import com.rocnarf.rocnarf.models.Pedido;
import com.rocnarf.rocnarf.models.PedidoResponse;
import com.rocnarf.rocnarf.models.PedidosPendienteComentarios;
import com.rocnarf.rocnarf.models.PedidosPendienteComentariosResponse;
import com.rocnarf.rocnarf.models.PedidosPendienteDetalleResponse;
import com.rocnarf.rocnarf.models.PedidosPendienteResponse;
import com.rocnarf.rocnarf.models.Periodo;
import com.rocnarf.rocnarf.models.Planes;
import com.rocnarf.rocnarf.models.PlanesResponse;
import com.rocnarf.rocnarf.models.PoliticasResponse;
import com.rocnarf.rocnarf.models.PreguntasFrecuentesResponse;
import com.rocnarf.rocnarf.models.Promocionado;
import com.rocnarf.rocnarf.models.Quejas;
import com.rocnarf.rocnarf.models.QuejasConsulta;
import com.rocnarf.rocnarf.models.ReporteVentasResponse;
import com.rocnarf.rocnarf.models.Usuario;
import com.rocnarf.rocnarf.models.Viatico;
import com.rocnarf.rocnarf.models.ViaticoAdd;
import com.rocnarf.rocnarf.models.VisitaClientes;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface PlanesService {

    @POST("api/planes/addObsequio")
    Observable<LiquidacionObsequio> Post(
            @Body LiquidacionObsequio liquidacionObsequio
    );

    @GET("api/planes")
    Call<PlanesResponse> Get(
            @Query("solicitudPremio") Boolean solicitudPremio
    );

    @GET("api/visitas?")
    Call<HistorialComentariosResponse> GetComentarios(
            @Query("idCliente") String idCliente,
            @Query("estado") String estado
    );

    @GET("api/visitas/comentariosClientes")
    Call<HistorialComentariosResponse> GetComentariosClientes(
            @Query("idAsesor") String idAsesor,
            @Query("idCliente") String idCliente
    );

    @GET("api/planes/getAll")
    Call<LiquidacionObsequioResponse> GetObsequios(
            @Query("idAsesor") String idAsesor
    );

    @PUT("api/planes/liquidacionObsequio/{id}")
    Observable<LiquidacionObsequio> updateObsequio(
            @Path("id") int id,
            @Body LiquidacionObsequio liquidacionObsequio
    );

    @GET("api/planes/getGuiaProductos")
    Call<GuiaProductosResponse> GetGuiaProductos(
            @Query("status") Boolean status
    );

    @GET("api/planes/getPreguntas")
    Call<PreguntasFrecuentesResponse> GetPreguntas(
            @Query("status") Boolean status
    );

    @GET("api/planes/getPoliticas")
    Call<PoliticasResponse> GetPoliticas(
            @Query("status") Boolean status
    );

    @GET("api/visitas/promocionado?")
    Call<Promocionado> GetPromocionado(
            @Path("idVisita") int id
    );

    @GET("api/clientes/detalleNotacredito?")
    Call<List<DetalleNotaCredito>> getNcDetalle(
            @Query("numero") String numero
    );

    @GET("api/visitas/historialVisitas/{idCliente}")
    Call<List<HistorialVisitas>> getHistorialVisita(
            @Path("idCliente") String id
    );

    @GET("api/visitas/promocionado/")
    Call<List<Promocionado>> GetPromocionado(
            @Query("IdVisita") String IdVisita
    );

    @GET("api/parametros/{parametro}")
    Call<ParametrosResponse> GetJefes(
             @Path("parametro") String jefes
    );

    @GET("api/clientes/consultarfichamedicoAll/{idCliente}")
    Call<EspecialidadMedicos> GetEspecialidadesMEdicas(
            @Path("idCliente") String idCliente
    );

    @POST("api/visitas/addComentarios")
    Observable<ComentariosClientes> PostAddComentarios(
            @Body ComentariosClientes comentariosClientes
    );

    @GET("api/pedidos/pedidos-pendientes")
    Call<PedidosPendienteResponse> GetPedidosPendiente(
            @Query("idAsesor") String idAsesor
    );

    @GET("api/pedidos/pedidos-pendientes-detalle")
    Call<PedidosPendienteDetalleResponse> GetPedidosPendienteDetalle(
            @Query("factura") String factura,
            @Query("despacho") String despacho
    );

    @GET("api/pedidos/pedidos-pendientes-comentarios")
    Call<PedidosPendienteComentariosResponse> GetPedidosPendienteComentario(
            @Query("factura") String factura
    );

    @GET("api/planes/getReporteVentas")
    Call<ReporteVentasResponse> GetReporteVentas(
            @Query("status") Boolean status
    );

    @GET("api/Clientes/recetas")
    Call<RecetasXResponse> GetRecetasX(
            @Query("tipo") String tipo,
            @Query("idMedico") String idMedico
    );

    @GET("api/Clientes/recetasMercado")
    Call<RecetasXResponse> GetRecetasXMercado(
            @Query("idMedico") String idMedico,
            @Query("Mercado") String Mercado
    );

    @GET("api/Clientes/medicoEvaluacion")
    Call<MedicosEvaluacion> GetMedicosCategoria(
            @Query("idMedico") String idMedico,
            @Query("seccion") String seccion
    );

    @GET("api/Clientes/carteraCliente")
    Call<List<CarteraCliente>>GetCarteraCliente(
            @Query("seccion") String seccion,
            @Query("rol") String rol
    );
    @GET("api/Clientes/cumpleanosMedico")
    Call<MedicosCumpleanyosResponse>GetCumpleanyos(
            @Query("seccion") String seccion );

    @GET("api/planes/getArchivosGuiaProductos/{id}")
    Call<List<ArchivosGuiaProducto>> GetArchivosGuiaProductos(
            @Path("id") int id
    );

    @GET("api/clientes/getEstadistica/{id}")
    Call<Estadistica> GetEstadistica(
            @Path("id") String idAsesor
    );

    @GET("api/clientes/getAllUsuarioQuejas/{idUsuario}")
    Call<List<QuejasConsulta>> GetQuejasUsuario(
            @Path("idUsuario") String idUsuario
    );

    @POST("api/clientes/addQuejas")
    Observable<Quejas> AddQuejas(
            @Body Quejas quejas
    );

    @GET("api/viatico/getPeriodoActual")
    Call<List<Periodo>> GetPeriodos(
            @Query("idUsuario") String idUsuario
    );

    @GET("api/viatico/getAllAsesor")
    Call<List<Viatico>>GetViaticoAsesor(
            @Query("opcion") int opcion,
            @Query("idAsesor") String idAsesor,
            @Query("idViatico") int idViatico,
            @Query("idCiclo") int idCiclo

    );

    @POST("api/viatico/addVia")
    Observable<ViaticoAdd> AddViatico(
            @Body ViaticoAdd viatico
    );

    @GET("api/viatico/getHistorialViatico")
    Call<List<HistorialViatico>> GetHistorialViatico(
            @Query("idAsesor") String idAsesor
    );

    @GET("api/viatico/imagen/{idUsuario}")
    Call<List<HistorialViatico>> GetImagenViatico(
            @Query("idAsesor") String idAsesor
    );

    @DELETE("api/viatico/{id}")
    Call<Void> Delete (
            @Path("id") int id
    );

    @PUT("api/viatico/{id}")
    Observable<ViaticoAdd> updateViatico(
            @Path("id") int id,
            @Body ViaticoAdd viatico
    );

}
