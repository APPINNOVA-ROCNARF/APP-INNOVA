package com.rocnarf.rocnarf.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;
import java.lang.reflect.Field;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.ClienteService;
import com.rocnarf.rocnarf.dao.ClientesDao;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.dao.SincronizacionDao;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.ClientesCupoCredito;
import com.rocnarf.rocnarf.models.ClientesResponse;
import com.rocnarf.rocnarf.models.Cobro;
import com.rocnarf.rocnarf.models.Factura;
import com.rocnarf.rocnarf.models.FacturaDetalle;
import com.rocnarf.rocnarf.models.FacturasNotaDebitos;
import com.rocnarf.rocnarf.models.FacturasNotaDebitosEstadistica;
import com.rocnarf.rocnarf.models.FichaMedico;
import com.rocnarf.rocnarf.models.NotaCredito;
import com.rocnarf.rocnarf.models.Sincronizacion;
import com.rocnarf.rocnarf.models.VentaMensualXCliente;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Completable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ClientesRepository {
    private ClientesDao clientesDao;
    private SincronizacionDao sincronizacionDao;
    private Date fechaLimiteSincronizacion;
    private Sincronizacion sincronizacion;
    private MutableLiveData<Clientes> cliente = new MutableLiveData<>();
    private MutableLiveData<List<Clientes>> listaClientes = new MutableLiveData<>();
    private MutableLiveData<List<Factura>> listaFacturas = new MutableLiveData<>();
    private MutableLiveData<List<FacturasNotaDebitos>> listaFacturasNotaDebitos = new MutableLiveData<>();
    private MutableLiveData<FacturasNotaDebitosEstadistica> FacturasNotaDebitosEstadistica = new MutableLiveData<>();
    private MutableLiveData<List<FacturaDetalle>> listaDetallesFactura =  new MutableLiveData<>();
    private MutableLiveData<List<Cobro>> listaCobros = new MutableLiveData<>();
    private MutableLiveData<List<Cobro>> listaChequeFecha = new MutableLiveData<>();
    private MutableLiveData<ClientesCupoCredito> cupoCreditoLiveData = new MutableLiveData<>();
    private MutableLiveData<List<VentaMensualXCliente>> ventaMensualXClienteMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<FichaMedico> fichaMedicoMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<NotaCredito>> listaNotaCredito = new MutableLiveData<>();

    public ClientesRepository(Context context, String idUsuario ){
        this.clientesDao = RocnarfDatabase.getDatabase(context).ClientesDao();
        this.sincronizacionDao = RocnarfDatabase.getDatabase(context).SincronizacionDao();
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DAY_OF_WEEK, -1);
        fechaLimiteSincronizacion = cal.getTime();
        sincronizacion = sincronizacionDao.get(idUsuario, Common.ENT_CLIENTES);

    }



    public Completable sincronizar(final String idUsuario, final String seccion, final String rolUsuario){

        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(final Completable.CompletableSubscriber completableSubscriber) {
                clientesDao.deleteAll();
                ClienteService service = ApiClient.getClient().create(ClienteService.class);
                Log.d("sincronizar Clientes 1", rolUsuario);

                ///Call<ClientesResponse> call = service.GetClientes(1, 9000, 0, null, null, null, seccion, null, null, rolUsuario, null);
                Call<ClientesResponse> call;

                if (rolUsuario.equals("GR") || rolUsuario.equals("IM") || rolUsuario.equals("KAM")) {
                    call = service.GetClientes(1, 9000, 2, null, null, null, null, null, null, rolUsuario, idUsuario);
                }else{
                    call = service.GetClientes(1, 9000, 0, null, null, null, seccion, null, null, rolUsuario, null);
                }

                    call.enqueue(new Callback<ClientesResponse>() {
                    @Override
                    public void onResponse(Call<ClientesResponse> call, Response<ClientesResponse> response) {
                        if (response.isSuccessful()) {
                            ClientesResponse clientesResponse = response.body();
                            List<Clientes> clientes = clientesResponse.items;

                            // Convertir la respuesta en JSON para mejor visualización
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            String jsonResponse = gson.toJson(clientesResponse);
                            Log.d("API Response", jsonResponse);


                            clientesDao.addClientes(clientes);
                            Log.d("clientesDao jefe", "x" + clientes.size());

                            Sincronizacion sincronizacion = sincronizacionDao.get(idUsuario, Common.ENT_CLIENTES);
                            if (sincronizacion == null) {
                                sincronizacion = new Sincronizacion();
                                sincronizacion.setIdUsuario(idUsuario);
                                sincronizacion.setEntidad(Common.ENT_CLIENTES);
                                sincronizacionDao.insert(sincronizacion);
                            }
                            sincronizacion.setFechaSincronizacion(new Date());
                            sincronizacionDao.update(sincronizacion);
                            Log.d("Clientes jefe completo","comple");
                            completableSubscriber.onCompleted();
                        }
                    }

                    @Override
                    public void onFailure(Call<ClientesResponse> call, Throwable t) {
                        Log.d("sincronizar Clientes", t.getMessage());
                        completableSubscriber.onError(t);
                    }
                });

            }
        });


    }

//    public Completable SincronizaCupos(final String idUsuario, final String seccion){
//
//    }

    public List<Clientes> getClientes(String sector, String idAsesor, String origen, String rol){


        List<Clientes> lista = new ArrayList<>();

        if(rol !=null && (rol.equals("GR") || rol.equals("IM") || rol.equals("KAM") ))
        {
            lista = this.clientesDao.getByJefes(null, null, origen,  null, null, idAsesor);

        }else{
            lista = this.clientesDao.get(null, null, origen, sector, null, null, idAsesor);
        }


//        if (sincronizacion != null) {
//            if (fechaLimiteSincronizacion.before(sincronizacion.getFechaSincronizacion())) {
//                codigoCliente = codigoCliente == null ? codigoCliente : "%" + codigoCliente.trim() + "%";
//                nombre = nombre == null ? nombre : "%" + nombre.trim() + "%";
//                representante = representante == null ? representante : "%" + representante.trim() + "%";
//                ciudad = ciudad == null ? ciudad : "%" + ciudad.trim() + "%";
//                tipo = tipo == null ? tipo : tipo.trim();
//                lista = this.clientesDao.get(codigoCliente, nombre, tipo, sector, representante, ciudad, idAsesor);
//            }
//        }

        return lista;
    }

    public List<Clientes> getClientesCumples(String seccion) {
        List<Clientes> lista = new ArrayList<>();
        lista = this.clientesDao.getByCumple(seccion);

        // Log para verificar los datos recuperados
        if (lista != null && !lista.isEmpty()) {
            Log.d("getClientesCumples", "Número de clientes recuperados: " + lista.size());
            for (Clientes cliente : lista) {
                Log.d("getClientesCumples", "Datos del cliente:");

                // Usar reflexión para obtener y registrar todos los campos
                for (Field field : cliente.getClass().getDeclaredFields()) {
                    field.setAccessible(true); // Permitir acceso a campos privados
                    try {
                        Log.d("getClientesCumples", field.getName() + " = " + field.get(cliente));
                    } catch (IllegalAccessException e) {
                        Log.e("getClientesCumples", "No se pudo acceder al campo: " + field.getName(), e);
                    }
                }
            }
        } else {
            Log.d("getClientesCumples", "No se encontraron clientes para la sección: " + seccion);
        }

        return lista;
    }



    public List<Clientes> getClientesSinGeo(String sector, String idAsesor, String origen){

        List<Clientes> lista = new ArrayList<>();
        lista = this.clientesDao.getSinGeo(null, null, origen, sector, null, null, idAsesor);
        return lista;
    }

    public LiveData<Clientes> getById(final String idCliente){
        if (sincronizacion != null && fechaLimiteSincronizacion.before(sincronizacion.getFechaSincronizacion())) {
            cliente.setValue(this.clientesDao.getById(idCliente));
        }else{
            ClienteService service = ApiClient.getClient().create(ClienteService.class);
            Call<Clientes> call = service.GetCliente(idCliente);
            call.enqueue(new Callback<Clientes>() {
                @Override
                public void onResponse(Call<Clientes> call, Response<Clientes> response) {
                    if (response.body() != null){
                        cliente.setValue(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Clientes> call, Throwable t) {
                    cliente.setValue(clientesDao.getById(idCliente));
                }
            });

        }
        return cliente;
    }

    public LiveData<Clientes> getClientesIdLocal(String idCliente) {
        cliente.setValue(clientesDao.getById(idCliente));
        return cliente;
    }

    public String getEstadoVisita(String idCliente, String idAsesor){
        return this.clientesDao.getEstadoVisita(idCliente, idAsesor);
    }


    public LiveData<List<Factura>> getFacturas(final String idCliente, final String seccion){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<List<Factura>> call = service.GetFacturas(idCliente, seccion);
        call.enqueue(new Callback<List<Factura>>() {
            @Override
            public void onResponse(Call<List<Factura>> call, Response<List<Factura>> response) {
                if (response.isSuccessful()){
                    listaFacturas.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<Factura>> call, Throwable t) {

            }
        });

        return  listaFacturas;
    }

    public LiveData<List<FacturasNotaDebitos>> getFacturasNotaDebitos(final String idCliente, final String seccion){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<List<FacturasNotaDebitos>> call = service.getFacturasNotaDebitos(idCliente, seccion);
        call.enqueue(new Callback<List<FacturasNotaDebitos>>() {
            @Override
            public void onResponse(Call<List<FacturasNotaDebitos>> call, Response<List<FacturasNotaDebitos>> response) {
                if (response.isSuccessful()){
                    listaFacturasNotaDebitos.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<FacturasNotaDebitos>> call, Throwable t) {

            }
        });

        return  listaFacturasNotaDebitos;
    }

    public LiveData<FacturasNotaDebitosEstadistica> getFacturasNotaDebitosEstadistica(final String idCliente, final String seccion){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<FacturasNotaDebitosEstadistica> call = service.getFacturasNotaDebitosEstadistica(idCliente, seccion);
        call.enqueue(new Callback<FacturasNotaDebitosEstadistica>() {
            @Override
            public void onResponse(Call<FacturasNotaDebitosEstadistica> call, Response<FacturasNotaDebitosEstadistica> response) {
                if (response.isSuccessful()){
                    FacturasNotaDebitosEstadistica.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<FacturasNotaDebitosEstadistica> call, Throwable t) {

            }
        });

        return  FacturasNotaDebitosEstadistica;
    }

    public LiveData<List<FacturaDetalle>> getFacturaDetalles(final String idFactura){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<List<FacturaDetalle>> call = service.GetFacturaDetalles(idFactura);
        call.enqueue(new Callback<List<FacturaDetalle>>() {
            @Override
            public void onResponse(Call<List<FacturaDetalle>> call, Response<List<FacturaDetalle>> response) {
                if (response.isSuccessful()){
                    listaDetallesFactura.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<FacturaDetalle>> call, Throwable t) {

            }
        });

        return  listaDetallesFactura;
    }

    public LiveData<List<FacturaDetalle>> getDetallesFacturasXCliente(final String idCliente){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<List<FacturaDetalle>> call = service.GetDetallesFacturasXCliente(idCliente);
        call.enqueue(new Callback<List<FacturaDetalle>>() {
            @Override
            public void onResponse(Call<List<FacturaDetalle>> call, Response<List<FacturaDetalle>> response) {
                if (response.isSuccessful()){
                    listaDetallesFactura.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<FacturaDetalle>> call, Throwable t) {

            }
        });

        return  listaDetallesFactura;
    }


    public LiveData<List<Cobro>> geGetCobrosXFactura(final String idFactura){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<List<Cobro>> call = service.GetCobrosXFactura(idFactura,1);
        call.enqueue(new Callback<List<Cobro>>() {
            @Override
            public void onResponse(Call<List<Cobro>> call, Response<List<Cobro>> response) {
                if (response.isSuccessful()){
                    listaCobros.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<Cobro>> call, Throwable t) {

            }
        });

        return  listaCobros;
    }

    public LiveData<List<Cobro>> GetChequeFechaXFactura(final String idFactura){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<List<Cobro>> call = service.GetCobrosXFactura(idFactura,2);
        call.enqueue(new Callback<List<Cobro>>() {
            @Override
            public void onResponse(Call<List<Cobro>> call, Response<List<Cobro>> response) {
                if (response.isSuccessful()){
                    listaCobros.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<Cobro>> call, Throwable t) {

            }
        });

        return  listaCobros;
    }


    public LiveData<List<Cobro>> getCobrosXCliente(final String idCliente){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<List<Cobro>> call = service.GetCobrosXCliente(idCliente,1);
        call.enqueue(new Callback<List<Cobro>>() {
            @Override
            public void onResponse(Call<List<Cobro>> call, Response<List<Cobro>> response) {
                if (response.isSuccessful()){
                    listaCobros.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Cobro>> call, Throwable t) {

            }
        });

        return  listaCobros;
    }

    public LiveData<List<Cobro>> getChequeFechaXCliente(final String idCliente){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<List<Cobro>> call = service.GetCobrosXCliente(idCliente,2);
        call.enqueue(new Callback<List<Cobro>>() {
            @Override
            public void onResponse(Call<List<Cobro>> call, Response<List<Cobro>> response) {
                if (response.isSuccessful()){
                    listaChequeFecha.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Cobro>> call, Throwable t) {

            }
        });

        return  listaChequeFecha;
    }

    public LiveData<List<NotaCredito>> getNcXCliente(final String idFactura, final String idCliente){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<List<NotaCredito>> call = service.getNcXCliente(idFactura,idCliente);
        call.enqueue(new Callback<List<NotaCredito>>() {
            @Override
            public void onResponse(Call<List<NotaCredito>> call, Response<List<NotaCredito>> response) {
                if (response.isSuccessful()){
                    listaNotaCredito.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<NotaCredito>> call, Throwable t) {

            }
        });

        return  listaNotaCredito;
    }




    public LiveData<ClientesCupoCredito> getCupoCredito(final String idCliente){

        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<ClientesCupoCredito> call = service.GetCupoCredito(idCliente);
        call.enqueue(new Callback<ClientesCupoCredito>() {
            @Override
            public void onResponse(Call<ClientesCupoCredito> call, Response<ClientesCupoCredito> response) {
                if (response.isSuccessful()){
                    cupoCreditoLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<ClientesCupoCredito> call, Throwable t) {

            }
        });

        return  cupoCreditoLiveData;
    }

    public  LiveData<List<VentaMensualXCliente>> getVentasMensualesXCliente(final String idCliente){
        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<List<VentaMensualXCliente>> call = service.GetVentasMensualesXCliente(idCliente);
        call.enqueue(new Callback<List<VentaMensualXCliente>>() {
            @Override
            public void onResponse(Call<List<VentaMensualXCliente>> call, Response<List<VentaMensualXCliente>> response) {
                if (response.isSuccessful()) {
                    ventaMensualXClienteMutableLiveData.setValue(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<VentaMensualXCliente>> call, Throwable t) {

            }
        });
        return  ventaMensualXClienteMutableLiveData;
    }


    public LiveData<FichaMedico> getFichaMedica(final String idCliente){
        ClienteService service = ApiClient.getClient().create(ClienteService.class);
        Call<FichaMedico> call = service.GetFichaMedico(idCliente);
        call.enqueue(new Callback<FichaMedico>() {
            @Override
            public void onResponse(Call<FichaMedico> call, Response<FichaMedico> response) {
                if (response.isSuccessful()) {
                    fichaMedicoMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<FichaMedico> call, Throwable t) {

            }
        });
        return fichaMedicoMutableLiveData;
    }


    public Completable updateFichaMedico(final FichaMedico fichaMedico){
        return Completable.create(new Completable.CompletableOnSubscribe() {
            @Override
            public void call(final Completable.CompletableSubscriber completableSubscriber) {
                ClienteService service = ApiClient.getClient().create(ClienteService.class);
                service.PutFichaMedico(fichaMedico.getIdCliente(), fichaMedico)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<FichaMedico>() {
                            @Override
                            public void onCompleted() {
                                completableSubscriber.onCompleted();
                            }

                            @Override
                            public void onError(Throwable e) {

                                completableSubscriber.onError(e);
                            }

                            @Override
                            public void onNext(FichaMedico fichaMedico) {

                            }
                        });

            }
        });
    }



}
