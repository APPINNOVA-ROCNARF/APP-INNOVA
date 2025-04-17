package com.rocnarf.rocnarf.Utils;

import com.rocnarf.rocnarf.models.Planes;

import java.sql.Array;
import java.util.List;

public class Common {
    public static final String DATE_FORMAT = "dd/MM/yy";
    public static final String TIME_STAMP_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ARG_IDUSUARIO = "idUsuario";
    public static final String ARG_ROL = "rol";
    public static final String ARG_SECCIOM = "seccion";
    public static final String ARG_SECCIONES = "secciones";
    public static final String ARG_ESTADO_VIATICO = "estadoviatico";
    public static final String ARG_ESTADO_CICLO = "estadociclo";
    public static final String ARG_NOMBREUSUARIO = "nombre";
    public static final String ARG_IDCLIENTE = "idCliente";
    public static final String ARG_NOMBRE_CLIENTE = "nombreCliente";
    public static final String ARG_USAR_PRECIO_ESPECIAL = "precioEspecial";
    public static final String ARG_NOMBRE_CICLO = "nombreCiclo";
    public static final String ARG_HISTORIAL = "historial";

    public static final String ARG_NOMBRE_CONCEPTO = "nombreConcepto";
    public static final String ARG_ID_CICLO = "idCiclo";
    public static final String ARG_ID_CONCEPTO = "idConcepto";
    public static final String ARG_FROM = "idFrom";

    public static final String ARG_IDFACTURA = "idFactura";
    public static final String ARG_IDPEDIDO = "idPedido";
    public static final String ARG_IDVISITALOCAL = "idVisitaLocal";
    public static final String ARG_NOMPBREPLAN = "nombrePlan";
    public static final String ARG_DESCRIPCIONPLAN = "descripcionPlan";
    public static final String ARG_IDNPLAN = "idPlan";
    public static final String ARG_IDVISITA = "idVisita";
    public static final String ARG_ESTADOVISTA = "estadoVisita";
    public static final String ARG_REVISITA= "revisita";

    public static final String ARG_MERCADO = "mercado";
    public static final String ARG_CATEGORIAMED = "categoriaMed";
    public static final String ARG_FACTURAS_SELECCION = "paraSeleccionarFactura";
    public static final String ARG_FACTURAS_SELECCION_QUEJAS= "paraSeleccionarFacturaQuejas";
    public static final String ARG_FACTURAS_COBRAR = "facturasCobrar";
    public static final String ARG_DESTINO_PEDIDO = "destinoPedido";
    public static final String ARG_TIPO_PEDIDO = "destinoPedido"; // usado en cobros para saber si el cobro es parte de un pedido o es individual
    public static String ARG_SHOW_MODAL_CUMPLE = "N";

    public static final String ARG_ORIGEN_PLANIFICACION_VISITA = "origenPlanificacionVisita";
    public static final String VISITA_DESDE_MAIN = "main";
    public static final String VISITA_DESDE_PANEL = "panel";
    public static final String VISITA_DESDE_MAPA = "mapa";

    //public static final String DOB_FORMAT = "yyyy-MM-dd";

    // Entidades
    public static final String ENT_CLIENTES = "Clientes";
    public static final String ENT_VISITAS = "VisitaClientes";
    public static final String ENT_PRODDUCTOS = "Producto";
    public static final String ENT_ESCALA_BONIFICACION = "EscalaBonificacion";


    // Pedido
    public static final String PED_PENDIENTE = "PEND";
    public static final String PED_SINCRONIZADO = "ACTI";
    public static final String TIPO_PEDIDO_PEDIDO = "PEDID";
    public static final String TIPO_PEDIDO_COBRO = "COBRO";


    public static double redondearDecimales(double valorInicial, int numeroDecimales) {
        double parteEntera, resultado;
        resultado = valorInicial;
        parteEntera = Math.floor(resultado);
        resultado=(resultado-parteEntera)*Math.pow(10, numeroDecimales);
        resultado=Math.round(resultado);
        resultado=(resultado/Math.pow(10, numeroDecimales))+parteEntera;
        return resultado;
    }
}
