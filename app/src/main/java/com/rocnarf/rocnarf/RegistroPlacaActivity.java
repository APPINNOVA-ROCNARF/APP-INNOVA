package com.rocnarf.rocnarf;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.rocnarf.rocnarf.Utils.Common;
import com.rocnarf.rocnarf.adapters.VehiculoAdapter;
import com.rocnarf.rocnarf.models.Vehiculo;
import com.rocnarf.rocnarf.viewmodel.VehiculoViewModel;

import java.util.List;

public class RegistroPlacaActivity extends AppCompatActivity {
    private static final int MAX_PLACAS = 2; // Podría moverse a res/values/config.xml
    private EditText etPlaca;
    private TextInputLayout tilPlaca;
    private Button btnRegistrar;
    private TextView tvPlacas;
    private RecyclerView rvPlacas;
    private VehiculoAdapter vehiculoAdapter;
    private VehiculoViewModel vehiculoViewModel;
    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_placa);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Obtener el ID del usuario desde la actividad anterior
        idUsuario = getIntent().getStringExtra(Common.ARG_IDUSUARIO);

        initializeUI();
        setupViewModel();
        setupListeners();
        vehiculoViewModel.cargarVehiculos(idUsuario); // Cargar vehículos al iniciar
    }

    private void initializeUI() {
        etPlaca = findViewById(R.id.etPlaca);
        tilPlaca = findViewById(R.id.til_placa);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        rvPlacas = findViewById(R.id.rvPlacas);
        tvPlacas = findViewById(R.id.tv_maximo_placas);
        tvPlacas.setVisibility(View.GONE);

        // Configurar RecyclerView
        rvPlacas.setLayoutManager(new LinearLayoutManager(this));
        vehiculoAdapter = new VehiculoAdapter();
        rvPlacas.setAdapter(vehiculoAdapter);
    }

    private void setupViewModel() {
        vehiculoViewModel = new ViewModelProvider(this).get(VehiculoViewModel.class);

        // Observar lista de vehículos
        vehiculoViewModel.getVehiculos().observe(this, this::updateUI);

        // Observar mensajes de error
        vehiculoViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                showSnackbar(error);
            }
        });

        // Observar éxito del registro
        vehiculoViewModel.getRegistroExitoso().observe(this, success -> {
            if (success != null && success) {
                showSnackbar("Placa registrada con exito");
                etPlaca.setText("");
                tilPlaca.setEndIconMode(TextInputLayout.END_ICON_NONE);
                vehiculoViewModel.cargarVehiculos(idUsuario); // Recargar lista
            }
        });
    }

    private void setupListeners() {
        // Validación en tiempo real
        etPlaca.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePlate(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Registro de vehículo
        btnRegistrar.setOnClickListener(v -> {
            String placa = etPlaca.getText().toString().trim();
            if (placa.isEmpty()) {
                showSnackbar("Ingrese una placa por favor");
            } else if (!isValidPlate(placa)) {
                showSnackbar("Formato incorrecto de placa");
            } else {
                vehiculoViewModel.registrarVehiculo(idUsuario, placa);
            }
        });
    }

    /**
     * Valida el formato de la placa: 3 letras mayúsculas seguidas de 3 o 4 dígitos (e.g., ABC123, ABC1234)
     */
    private boolean isValidPlate(String placa) {
        return placa.matches("^[A-Z]{3}\\d{3,4}$");
    }

    private void validatePlate(String placa) {
        if (placa.isEmpty()) {
            etPlaca.setError(null);
            tilPlaca.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else if (!isValidPlate(placa)) {
            etPlaca.setError("Formato inválido. Ejemplo: ABC123 o ABC1234");
            tilPlaca.setEndIconMode(TextInputLayout.END_ICON_NONE);
        } else {
            etPlaca.setError(null);
            tilPlaca.setEndIconMode(TextInputLayout.END_ICON_CUSTOM);
            tilPlaca.setEndIconDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_circle_green_24dp));
        }
    }

    private void updateUI(List<Vehiculo> vehiculos) {
        vehiculoAdapter.setVehiculos(vehiculos);
        boolean maxReached = vehiculos.size() >= MAX_PLACAS;
        etPlaca.setEnabled(!maxReached);
        btnRegistrar.setEnabled(!maxReached);
        tvPlacas.setVisibility(maxReached ? View.VISIBLE : View.GONE);
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_OK);
        finish();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("placa_text", etPlaca.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            etPlaca.setText(savedInstanceState.getString("placa_text"));
        }
    }
}