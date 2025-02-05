package com.rocnarf.rocnarf;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;

import android.app.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.api.ApiClient;
import com.rocnarf.rocnarf.api.ClienteService;
import com.rocnarf.rocnarf.api.UsuarioService;
import com.rocnarf.rocnarf.dao.ClientesDao;
import com.rocnarf.rocnarf.dao.RocnarfDatabase;
import com.rocnarf.rocnarf.dao.UsuariosDao;
import com.rocnarf.rocnarf.models.Clientes;
import com.rocnarf.rocnarf.models.ClientesResponse;
import com.rocnarf.rocnarf.models.Sincronizacion;
import com.rocnarf.rocnarf.models.Usuario;


import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends Activity {


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private  Usuario usuario;
    private  UsuariosDao usuariosDao;
    private ClientesDao clientesDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        clientesDao = RocnarfDatabase.getDatabase(getApplicationContext()).ClientesDao();
        usuariosDao = RocnarfDatabase.getDatabase(getApplicationContext()).UsuariosDao();

        usuario = usuariosDao.get();

        if (usuario != null){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra(Common.ARG_IDUSUARIO, usuario.getIdUsuario());
            i.putExtra(Common.ARG_SECCIOM, usuario.getSeccion());
            i.putExtra(Common.ARG_NOMBREUSUARIO, usuario.getNombre());
            i.putExtra(Common.ARG_ROL, usuario.getRol());
            startActivity(i);
            finish();

        }

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }



    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mProgressView.setVisibility(View.VISIBLE);
            mLoginFormView.setVisibility(View.GONE);
            //showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);

            //Guardo localmente los usuarios
            clientesDao.deleteAll();
            ClienteService service2 = ApiClient.getClient().create(ClienteService.class);
            retrofit2.Call<ClientesResponse> call2  = service2.GetClientes(1, 1000, 0, null,null, null, null, null, null, null, null);
            call2.enqueue(new Callback<ClientesResponse>() {
                @Override
                public void onResponse(Call<ClientesResponse> call, Response<ClientesResponse> response) {
                    if (response.isSuccessful()){
                        ClientesResponse clientesResponse = response.body();
                        List<Clientes> clientes = clientesResponse.items;
                        clientesDao.addClientes(clientes);
                        Toast.makeText(getApplicationContext(), "Base de datos de clientes actualizada", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ClientesResponse> call, Throwable t) {
                    //Log.d("sincronizar Clientes", t.getMessage());
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            //Se conecta al servicio de usuario para login
            UsuarioService service = ApiClient.getClient().create(UsuarioService.class);
            retrofit2.Call<Usuario> call = service.Get(email, password);
            call.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    mProgressView.setVisibility(View.GONE);
                    mLoginFormView.setVisibility(View.VISIBLE);
                    if (response.isSuccessful()){
                        usuario = response.body();
                        usuariosDao.deleteAll();
                        usuariosDao.insert(usuario);
                        Toast.makeText(getApplicationContext(), "Usuario autenticado con exito", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra(Common.ARG_IDUSUARIO, usuario.getIdUsuario());
                        i.putExtra(Common.ARG_SECCIOM, usuario.getSeccion());
                        i.putExtra(Common.ARG_NOMBREUSUARIO, usuario.getNombre());
                        i.putExtra(Common.ARG_ROL, usuario.getRol());
                        startActivity(i);
                        finish();
                    }else if  (response.code() == 404){
                        mEmailView.setError("Usuario no existente");
                    } else if (response.code() == 400) {
                        mPasswordView.setError("Clave no es valida");
                    } else {
                        mPasswordView.setError("Error al autenticar");
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    mProgressView.setVisibility(View.GONE);
                    mLoginFormView.setVisibility(View.VISIBLE);
                    mPasswordView.setError("Error al autenticar. Por favor verifique su conexion a Internet");
                }
            });


            showProgress(false);
        }
    }





    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }






}

