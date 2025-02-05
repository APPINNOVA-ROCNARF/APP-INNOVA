package com.rocnarf.rocnarf.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rocnarf.rocnarf.R;

import java.util.ArrayList;

public class PanelProductoAdapter extends BaseAdapter {

    private ArrayList<PanelProductoObjeto> listconsulta_panel_objeto;
    private LayoutInflater layoutInflater;

    public PanelProductoAdapter(Context context, ArrayList<PanelProductoObjeto> listconsulta_formulario_objeto) {
        this.listconsulta_panel_objeto = listconsulta_formulario_objeto;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return listconsulta_panel_objeto.size();
    }

    public Object getItem(int indice) {
        return listconsulta_panel_objeto.get(indice);
    }

    public long getItemId(int indice) {
        return indice;
    }

    static class row_panel_cliente {
        TextView LblIdProducto;
        TextView LblNombreProducto;
        TextView LblCantidad;
        TextView LblBonificacion;
        TextView LblPrecio;
        TextView LblTotal;
        ImageButton ImgDelete;
    }

    public View getView(int indice, View view, ViewGroup viewGroup) {
        row_panel_cliente row_panel_cliente;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.row_pedido_producto, null);
            row_panel_cliente = new row_panel_cliente();
            row_panel_cliente.LblIdProducto = (TextView) view.findViewById(R.id.tv_idproducto_row_pedido_producto);
            row_panel_cliente.LblNombreProducto = (TextView) view.findViewById(R.id.tv_producto_row_pedido_producto);
            row_panel_cliente.LblCantidad = (TextView) view.findViewById(R.id.tv_cantidad_row_pedido_producto);
            row_panel_cliente.LblBonificacion = (TextView) view.findViewById(R.id.tv_bonificacion_row_pedido_producto);
            row_panel_cliente.LblPrecio = (TextView) view.findViewById(R.id.tv_precio_row_pedido_producto);
            row_panel_cliente.LblTotal = (TextView) view.findViewById(R.id.tv_total_row_pedido_producto);
            row_panel_cliente.ImgDelete = (ImageButton) view.findViewById(R.id.ib_eliminar_row_pedido_producto);
            row_panel_cliente.ImgDelete.setFocusable(false);
            view.setTag(row_panel_cliente);
        } else
            row_panel_cliente = (row_panel_cliente) view.getTag();
        row_panel_cliente.LblIdProducto.setText(listconsulta_panel_objeto.get(indice).getIdProducto());
        row_panel_cliente.LblNombreProducto.setText(listconsulta_panel_objeto.get(indice).getNombreProducto());
        row_panel_cliente.LblCantidad.setText(listconsulta_panel_objeto.get(indice).getCantidad());
        row_panel_cliente.LblBonificacion.setText(listconsulta_panel_objeto.get(indice).getBonificacion());
        row_panel_cliente.LblPrecio.setText(listconsulta_panel_objeto.get(indice).getPrecio());
        row_panel_cliente.LblTotal.setText(listconsulta_panel_objeto.get(indice).getTotal());
        return view;
    }
}
