package com.rocnarf.rocnarf;

import android.Manifest;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.PopupMenu;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.viewmodel.ClienteDetalleViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClienteDetalleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClienteDetalleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClienteDetalleFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private String idCliente, idUsuario, nombreCliente;
    private Context context;
    private boolean esFarmacia = false;
    private OnFragmentInteractionListener mListener;
    private ClienteDetalleViewModel clienteDetalleViewModel;

    Clientes clientesResponse;

    private TextView mCodigoView, mNombreClienteView, mRepresentanteView, mDireccionView, mTelefonoView, mTelefono2View, mEmailView;
    private ImageView mDireccionImage;
    private ProgressBar mProgress;
    private ConstraintLayout mLayout;
    private PopupMenu popup;
    private ImageView mPerfil, mPerfilCumple;
    public ClienteDetalleFragment() {
        // Required empty public constructor
    }


    public static ClienteDetalleFragment newInstance(String idCliente, String idUsuario) {
        ClienteDetalleFragment fragment = new ClienteDetalleFragment();
        Bundle args = new Bundle();
        args.putString(Common.ARG_IDCLIENTE, idCliente);
        args.putString(Common.ARG_IDUSUARIO, idUsuario);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCliente = getArguments().getString(Common.ARG_IDCLIENTE);
            idUsuario = getArguments().getString(Common.ARG_IDUSUARIO);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cliente_detalle, container, false);
        mCodigoView = (TextView) view.findViewById(R.id.tv_codigo_fragment_detalle_cliente);
        mNombreClienteView = (TextView) view.findViewById(R.id.tv_nombre_fragment_detalle_cliente);
        mRepresentanteView = (TextView) view.findViewById(R.id.tv_representante_fragment_detalle_cliente);
        mDireccionView = (TextView) view.findViewById(R.id.tv_direccion_fragment_detalle_cliente);
        mDireccionImage = (ImageView)view.findViewById(R.id.iv_direccion_fragment_detalle_cliente);
        mTelefonoView = (TextView) view.findViewById(R.id.tv_telefono_fragment_detalle_cliente);
        mTelefono2View = (TextView) view.findViewById(R.id.tv_telefono2_fragment_detalle_cliente);
        mEmailView = (TextView) view.findViewById(R.id.tv_email_fragment_detalle_cliente);
        mProgress = (ProgressBar) view.findViewById(R.id.pr_list_fragment_detalle_cliente);
        ImageButton mMenuPedidosView = (ImageButton)view.findViewById(R.id.ib_pedido_fragment_cliente_detalle_detalle);
        mLayout = (ConstraintLayout) view.findViewById(R.id.ly_contentido_fragment_detalle_cliente);
        mPerfil = (ImageView) view.findViewById(R.id.imageView4);
//        mPerfilCumple = (ImageView) view.findViewById(R.id.imageView5);
        mMenuPedidosView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showPopup(view, R.menu.menu_pedidos_detalle_cliente);
                String valiZ= mCodigoView.getText().toString().toUpperCase().substring(0, 1);

                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_pedidos_detalle_cliente, popupMenu.getMenu());

                if (valiZ.equals("Z")) {
                    if (esFarmacia)  popupMenu.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
                    if (esFarmacia)  popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                    if (esFarmacia)  popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                    popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                     popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
                     popupMenu.getMenu().findItem(R.id.action_cupos_credito).setVisible(false);
                     popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
                }else {
                    if (esFarmacia)  popupMenu.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
                    if (esFarmacia)  popupMenu.getMenu().findItem(R.id.action_recetas).setVisible(false);
                    if (esFarmacia)  popupMenu.getMenu().findItem(R.id.action_categoria).setVisible(false);

                    if (!esFarmacia)  popupMenu.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
                    if (!esFarmacia)  popupMenu.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
                    if (!esFarmacia)  popupMenu.getMenu().findItem(R.id.action_cupos_credito).setVisible(false);
                    if (!esFarmacia)  popupMenu.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
                }


                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();

                        if (itemId == R.id.action_historial_pedidos) {
                            Intent iFacturas = new Intent(context, ClientesFacturasNotaDebitosActivity.class);
                            iFacturas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            iFacturas.putExtra(Common.ARG_NOMBRE_CLIENTE, nombreCliente);
                            iFacturas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            context.startActivity(iFacturas);
                            return true;
                        } else if (itemId == R.id.action_detalle_productos) {
                            Intent iFacturaDetalle = new Intent(context, FacturaDetalleActivity.class);
                            iFacturaDetalle.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            iFacturaDetalle.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            context.startActivity(iFacturaDetalle);
                            return true;
                        } else if (itemId == R.id.action_cupos_credito) {
                            Intent i = new Intent(context, ClientesCupoCreditoActivity.class);
                            i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            context.startActivity(i);
                            return true;
                        } else if (itemId == R.id.action_totales_mes) {
                            Intent iTotalesXMes = new Intent(context, VentasMensualesClientesActivity.class);
                            iTotalesXMes.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            iTotalesXMes.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            context.startActivity(iTotalesXMes);
                            return true;
                        } else if (itemId == R.id.action_ficha_medico) {
                            Intent iFichaMedico = new Intent(context, MedicoFichaActivity.class);
                            iFichaMedico.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            iFichaMedico.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            context.startActivity(iFichaMedico);
                            return true;
                        } else if (itemId == R.id.action_historial_comentarios) {
                            Intent iComentarios = new Intent(context, HistorialComentariosActivity.class);
                            iComentarios.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            iComentarios.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            context.startActivity(iComentarios);
                            return true;
                        } else if (itemId == R.id.action_historial_visitas) {
                            Intent iVisitas = new Intent(context, HistorialVisitasActivity.class);
                            iVisitas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            iVisitas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            context.startActivity(iVisitas);
                            return true;
                        } else if (itemId == R.id.action_recetas) {
                            Intent iRecetas = new Intent(context, RecetasXActivity.class);
                            iRecetas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            iRecetas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            context.startActivity(iRecetas);
                            return true;
                        } else if (itemId == R.id.action_categoria) {
                            Intent iCategoria = new Intent(context, MedicosCategoriaActivity.class);
                            iCategoria.putExtra(Common.ARG_IDCLIENTE, idCliente);
                            iCategoria.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                            context.startActivity(iCategoria);
                            return true;
                        } else {
                            return false;
                        }
                    }
                });

            }
        });
//        mMenuCobrosView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showPopup(view, R.menu.menu_cobros_detalle_cliente);
//            }
//        });

        ConsultaCliente(idCliente);

        return view;

    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Clientes cliente);
    }


    private void ConsultaCliente(String codigo){

        clienteDetalleViewModel = ViewModelProviders.of(this).get(ClienteDetalleViewModel.class);
        clienteDetalleViewModel.setIdUsuario(idUsuario);
        clienteDetalleViewModel.getByid(codigo).observe(this, new Observer<Clientes>() {
                    @Override
                    public void onChanged(@Nullable final Clientes clientes) {
                        if (clientes != null) {
                            nombreCliente = clientes.getNombreCliente();
                            mNombreClienteView.setText(clientes.getNombreCliente());
                            String seccion = clientes.getSeccion() ==  null ? "":  clientes.getSeccion();
                            seccion += clientes.getSeccion2() ==  null ? "": " " + clientes.getSeccion2();
                            seccion += clientes.getSeccion3() ==  null ? "": " " + clientes.getSeccion3();
                            seccion += clientes.getSeccion4() ==  null ? "": " " + clientes.getSeccion4();
                            seccion += clientes.getSeccion5() ==  null ? "": " " + clientes.getSeccion5();
                            seccion += clientes.getSeccion6() ==  null ? "": " " + clientes.getSeccion6();
                            seccion += clientes.getSeccion7() ==  null ? "": " " + clientes.getSeccion7();
                            seccion += clientes.getSeccion8() ==  null ? "": " " + clientes.getSeccion8();
                            seccion += clientes.getSeccion9() ==  null ? "": " " + clientes.getSeccion9();
                            mCodigoView.setText(clientes.getOrigen().equals("MEDICO") ? clientes.getIdCliente() +  (clientes.getIdEspecialidades() == null ? "": " - " + clientes.getIdEspecialidades() )+ " - " + seccion + " - " + clientes.getClaseMedico()
                                    : clientes.getIdCliente() +  (clientes.getTipo() == null ? "": " - " + clientes.getTipo() )+ " - " + seccion + " - " + clientes.getTipoObserv());
                            mRepresentanteView.setText(clientes.getRepresentante());
                            mDireccionView.setText(clientes.getDireccion());
                            if(clientes.getCumpleAnyos() != null) {

                                if (clientes.getCumpleAnyos()) {
                                    mPerfil.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_torta_cumplanyo_24dp));
                                    //          mPerfil.setVisibility(View.GONE);
                                    //        mPerfilCumple.setVisibility(View.VISIBLE);
                                }
                            }//else{
                          //      mPerfil.setVisibility(View.VISIBLE);
                            //    mPerfilCumple.setVisibility(View.GONE);
                           // }
                            if (clientes.getLatitud() == null)
                                mDireccionImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                            else
                                if (clientes.getLatitud() == 0)
                                    mDireccionImage.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                else
                                    mDireccionImage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String geo = "google.navigation:q=" + clientes.getLatitud().toString() + "," + clientes.getLongitud();
                                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                                    Uri.parse(geo));
                                            startActivity(intent);
                                        }
                                    });
                            // Se asigna la informacion al telefono 1 con la opcion para que pueda llamar haciendo click sobre el dato
                            if (clientes.getTelefono1() != null){
                                SpannableString  spannableTelefono1 = new SpannableString(clientes.getTelefono1());
                                ClickableSpan clickableSpan = new ClickableSpan() {
                                    @Override
                                    public void onClick(View textView) {
                                        if (getCallPhonePermission()) {
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + clientes.getTelefono1()));
                                            startActivity(intent);
                                        }
                                    }
                                };
                                spannableTelefono1.setSpan(clickableSpan, 0, clientes.getTelefono1().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mTelefonoView.setText(spannableTelefono1, TextView.BufferType.SPANNABLE );
                                mTelefonoView.setMovementMethod(LinkMovementMethod.getInstance());
                            }
                            if (clientes.getTelefono2() != null){
                                SpannableString  spannableTelefono2 = new SpannableString(clientes.getTelefono2());
                                ClickableSpan clickableSpan = new ClickableSpan() {
                                    @Override
                                    public void onClick(View textView) {
                                        if (getCallPhonePermission()) {
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + clientes.getTelefono2()));
                                            startActivity(intent);
                                        }
                                    }
                                };
                                spannableTelefono2.setSpan(clickableSpan, 0, clientes.getTelefono2().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                mTelefono2View.setText(spannableTelefono2, TextView.BufferType.SPANNABLE );
                                mTelefono2View.setMovementMethod(LinkMovementMethod.getInstance());
                            }


                            mEmailView.setText(clientes.getEmail());
                            // si el cliente es de tipo farmacia se deshabilita la opcion ficha medico
                            if (clientes.getOrigen() == null || clientes.getOrigen().equals("FARMA")){
                                if (popup != null ){
                                    popup.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
                                    popup.getMenu().findItem(R.id.action_recetas).setVisible(false);
                                    popup.getMenu().findItem(R.id.action_categoria).setVisible(false);

                                }
                                else
                                    esFarmacia = true;
                            }

                            if (mListener != null) {
                                mListener.onFragmentInteraction(clientes);
                            }
                            mProgress.setVisibility(View.GONE);
                            mLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });


//        ClienteService service = ApiClient.getClient().create(ClienteService.class);
//        Call<Clientes> call = service.GetCliente(codigo);
//        call.enqueue(new Callback<Clientes>() {
//            @Override
//            public void onResponse(Call<Clientes> call, Response<Clientes> response) {
//                if (response.body() != null){
//                    clientesResponse = response.body();
//                    mNombreClienteView.setText(clientesResponse.getNombreCliente());
//                    mCodigoView.setText(clientesResponse.getIdCliente() + " - Tipo: " + clientesResponse.getTipo() + " - Sector:" + clientesResponse.getSeccion());
//                    mRepresentanteView.setText(clientesResponse.getRepresentante());
//                    mDireccionView.setText(clientesResponse.getDireccion());
//                    mTelefonoView.setText(clientesResponse.getTelefono1() + " - " + clientesResponse.getTelefono2());
//                    mEmailView.setText(clientesResponse.getEmail());
//                    if (mListener != null) {
//                        mListener.onFragmentInteraction(clientesResponse);
//                    }
//
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<Clientes> call, Throwable t) {
//                call.cancel();
//            }
//        });

    }

    private boolean getCallPhonePermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getActivity().getApplicationContext(),
                android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            return  true;
        } else {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
            return false;
        }
    }

    public void showPopup(View v, int menures) {
        PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.action_historial_pedidos) {
                    Intent iFacturas = new Intent(context, ClientesFacturasActivity.class);
                    iFacturas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    iFacturas.putExtra(Common.ARG_NOMBRE_CLIENTE, nombreCliente);
                    iFacturas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    startActivity(iFacturas);
                    return true;
                } else if (itemId == R.id.action_detalle_productos) {
                    Intent iFacturaDetalle = new Intent(context, FacturaDetalleActivity.class);
                    iFacturaDetalle.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    iFacturaDetalle.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    startActivity(iFacturaDetalle);
                    return true;
                } else if (itemId == R.id.action_cupos_credito) {
                    Intent i = new Intent(context, ClientesCupoCreditoActivity.class);
                    i.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    i.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    startActivity(i);
                    return true;
                } else if (itemId == R.id.action_totales_mes) {
                    Intent iTotalesXMes = new Intent(context, VentasMensualesClientesActivity.class);
                    iTotalesXMes.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    iTotalesXMes.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    startActivity(iTotalesXMes);
                    return true;
                } else if (itemId == R.id.action_ficha_medico) {
                    Intent iFichaMedico = new Intent(context, MedicoFichaActivity.class);
                    iFichaMedico.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    iFichaMedico.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    startActivity(iFichaMedico);
                    return true;
                } else if (itemId == R.id.action_historial_comentarios) {
                    Intent iComentarios = new Intent(context, HistorialComentariosActivity.class);
                    iComentarios.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    iComentarios.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    context.startActivity(iComentarios);
                    return true;
                } else if (itemId == R.id.action_historial_visitas) {
                    Intent iVisitas = new Intent(context, HistorialVisitasActivity.class);
                    iVisitas.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    iVisitas.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    context.startActivity(iVisitas);
                    return true;
                } else if (itemId == R.id.action_recetas) {
                    // Acción específica para "Recetas" (si aplica)
                    return true;
                } else if (itemId == R.id.action_categoria) {
                    Intent iCategoria = new Intent(context, MedicosCategoriaActivity.class);
                    iCategoria.putExtra(Common.ARG_IDCLIENTE, idCliente);
                    iCategoria.putExtra(Common.ARG_IDUSUARIO, idUsuario);
                    context.startActivity(iCategoria);
                    return true;
                } else {
                    return false;
                }
            }
        };

        popup = new PopupMenu(context.getApplicationContext(), v);
        popup.setOnMenuItemClickListener(listener);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_pedidos_detalle_cliente, popup.getMenu());

        // Configuración de visibilidad según la condición
        if (esFarmacia) {
            popup.getMenu().findItem(R.id.action_ficha_medico).setVisible(false);
            popup.getMenu().findItem(R.id.action_recetas).setVisible(false);
            popup.getMenu().findItem(R.id.action_categoria).setVisible(false);
        }

        if (!esFarmacia) {
            popup.getMenu().findItem(R.id.action_historial_pedidos).setVisible(false);
            popup.getMenu().findItem(R.id.action_detalle_productos).setVisible(false);
            popup.getMenu().findItem(R.id.action_cupos_credito).setVisible(false);
            popup.getMenu().findItem(R.id.action_totales_mes).setVisible(false);
        }

        popup.show();
    }





}
