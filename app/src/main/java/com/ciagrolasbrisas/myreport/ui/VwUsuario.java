package com.ciagrolasbrisas.myreport.ui;

import static com.ciagrolasbrisas.myreport.controller.HashPass.convertSHA256;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.ConnectivityService;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.controller.usuario.UsServidorController;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.model.MdUsuario;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.OkHttpClient;

public class VwUsuario extends AppCompatActivity {

    private MdUsuario usuario;
    private ArrayList<MdUsuario> listUsuario;
    private LogGenerator logGenerator;
    private String date, time, clase;
    private FloatingActionButton btnGuardarUs;

    private TextInputEditText txtCedula, txtNombre, txtContrasena, txtRol, txtPuesto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vw_usuario);

        txtCedula = findViewById(R.id.txtCedulaUsuario);
        txtNombre = findViewById(R.id.txtNombreUsuario);
        txtContrasena = findViewById(R.id.txtPassUsuario);
        txtRol = findViewById(R.id.txtRolUsuario);
        txtPuesto = findViewById(R.id.txtPuestoUsuario);
        btnGuardarUs = findViewById(R.id.btnGuardarUs);

        GetStringDate stringDate = new GetStringDate();
        GetStringTime stringTime = new GetStringTime();
        date = stringDate.getFecha();
        time = stringTime.getHora();
        clase = this.getClass().getSimpleName();

        logGenerator = new LogGenerator();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            usuario = (MdUsuario) bundle.getSerializable("usuario");  // Extrayendo el extra de tipo cadena
            if (Objects.requireNonNull(usuario).getAccion() == 1 || Objects.requireNonNull(usuario).getAccion() == 2) // Actualizar o eliminar reporte
                mostrarDatosEnInterfaz(usuario);
        }

        btnGuardarUs.setOnClickListener(view -> {
            guardarEnServidor();
        });
    }

    private void mostrarDatosEnInterfaz(MdUsuario usuario) {

    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), VwMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void guardarEnServidor(){
        String funcion = new Throwable().getStackTrace()[0].getMethodName();
        OkHttpClient client = new OkHttpClient();

        usuario = new MdUsuario();
        usuario.setCedula(txtCedula.getText().toString());
        usuario.setPass(convertSHA256(txtContrasena.getText().toString()));
        usuario.setNombre(txtNombre.getText().toString());
        usuario.setRol(txtRol.getText().toString());
        usuario.setAccion(0);

        ConnectivityService con = new ConnectivityService();

        if (con.stateConnection(this)) {

            listUsuario = new ArrayList<>();
            listUsuario.add(usuario);
            Map<String, Object> finalJson = new HashMap<>();
            finalJson.put("reporte", listUsuario);

            String json = new Gson().toJson(finalJson);

            UsServidorController usServidorController = new UsServidorController();
            usServidorController.crudUsuario(this, json);

        } else {
            logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + funcion + ": " + "El dispositivo no puede accesar a la red en este momento!"); // Agregamos el error al archivo Descargas/Logs.txt
            Toast.makeText(this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
        }

    }
}