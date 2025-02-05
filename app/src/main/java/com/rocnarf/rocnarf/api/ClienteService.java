package com.rocnarf.rocnarf.api;

import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.ClientesCupoCredito;
import com.rocnarf.rocnarf.models.ClientesResponse;
import com.rocnarf.rocnarf.models.Cobro;
import com.rocnarf.rocnarf.models.DetalleNotaCredito;
import com.rocnarf.rocnarf.models.Factura;
import com.rocnarf.rocnarf.models.FacturaDetalle;
import com.rocnarf.rocnarf.models.FacturasNotaDebitos;
import com.rocnarf.rocnarf.models.FcmTokenDivice;
import com.rocnarf.rocnarf.models.FichaMedico;
import com.rocnarf.rocnarf.models.NotaCredito;
import com.rocnarf.rocnarf.models.Rutas;
import com.rocnarf.rocnarf.models.VentaMensualXCliente;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface ClienteService {


    @GET("api/clientes?")
    Call<ClientesResponse> GetClientes(
            @Query("page") int page,
            @Query("pagesize") int pagesize,
            @Query("tipoConsulta") int tipoConsulta,
            @Query("id") String codigoCliente,
            @Query("nombre") String nombre,
            @Query("tipo") String tipo,
            @Query("seccion") String seccion,
            @Query("dueno") String local,
            @Query("ciudad") String ciudad,
            @Query("rol") String rol,
            @Query("IdAsesor") String IdAsesor
    );



    @GET("api/clientes/{id}")
    Call<Clientes> GetCliente(
            @Path("id") String codigoCliente
    );


    @PUT("api/clientes/{id}")
    Call<ClientesResponse> PutClientes(
            @Path("id") String idCliente,
            @Body Clientes cliente
    );


    @GET("api/clientes/credito/{idCliente}")
    Call<ClientesCupoCredito> GetCupoCredito(
            @Path("idCliente") String codigoCliente
    );


    //--------------------------------------------------
    // FACTURAS
    @GET("api/clientes/facturas?")
    Call<List<Factura>> GetFacturas(
            @Query("id") String codigoCliente,
            @Query("seccion") String seccion
    );

    @GET("api/clientes/facturasNotaDebitos?")
    Call<List<FacturasNotaDebitos>> getFacturasNotaDebitos(
            @Query("id") String codigoCliente,
            @Query("seccion") String seccion
    );

    @GET("api/clientes/facturas/{idFactura}/detalles?")
    Call<List<FacturaDetalle>> GetFacturaDetalles(
            @Path("idFactura") String idFactura
    );

    @GET("api/clientes/{idCliente}/facturas/detalles")
    Call<List<FacturaDetalle>> GetDetallesFacturasXCliente(
            @Path("idCliente") String idCliente
    );

    //--------------------------------------------------
    // COBROS

    @GET("api/clientes/facturas/{idFactura}/cobros?")
    Call<List<Cobro>> GetCobrosXFactura(
            @Path("idFactura") String idFactura
    );

    @GET("api/clientes/{idCliente}/cobros")
    Call<List<Cobro>> GetCobrosXCliente(
            @Path("idCliente") String idCliente
    );

    //--------------------------------------------------
    // COBROS
    @GET("api/clientes/ventas/{idCliente}")
    Call<List<VentaMensualXCliente>> GetVentasMensualesXCliente(
            @Path("idCliente") String idCliente
    );


    @GET("api/clientes/consultarfichamedico/{idCliente}")
    Call<FichaMedico> GetFichaMedico(
            @Path("idCliente") String idCliente
    );


    @PUT("api/clientes/fichaMedico/{idCliente}")
    Observable<FichaMedico> PutFichaMedico(
            @Path("idCliente") String idCliente,
            @Body FichaMedico fichaMedico
    );

    @GET("api/clientes/notacredito")
    Call<List<NotaCredito>> getNcXCliente(
            @Query("idFactura") String idFactura,
            @Query("codigo") String idCliente
    );

    @GET("api/clientes/detalleNotacredito?")
    Call<List<DetalleNotaCredito>> getNcDetalle(
            @Query("numero") String numero
    );

    @POST("api/Clientes/addToken")
    Observable<FcmTokenDivice> addToken(
            @Body FcmTokenDivice fcmTokenDivice
    );

}


