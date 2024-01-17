package com.ciagrolasbrisas.myreport.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.ApiUtils;
import com.ciagrolasbrisas.myreport.controller.CheckPermissions;
import com.ciagrolasbrisas.myreport.controller.ConnectivityService;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.ciagrolasbrisas.myreport.controller.HashPass;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.model.MdUsuario;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VwLogin extends AppCompatActivity {
        private TextInputEditText txtId, txtPass;
        private DatabaseController dbController;
        private Switch stLocalMode;
        private boolean localMode;
        private String date, time, clase;
        private LogGenerator logGenerator;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_login);

                clase = this.getClass().getSimpleName();

                stLocalMode = findViewById(R.id.switchModoLocal);

                logGenerator = new LogGenerator();

                CheckPermissions permisos = new CheckPermissions(this);  // Lanza activity para solicitar permisos correspondientes

                crearDbLocal();

                txtId = findViewById(R.id.txtLoginUser);
                txtPass = findViewById(R.id.txtLoginPass);

                Button btnLogin = findViewById(R.id.btnLogin);
                btnLogin.setOnClickListener(v -> loginLocalOremoto());
        }

        private void crearDbLocal() {
                dbController = new DatabaseController();
                try {
                        String resultadoConsulta = dbController.crearDbLocal(this);
                        if (resultadoConsulta.equals("2")) {
                                dbController.insertDefaultCuelloBotella(this);
                                dbController.insertDefaultCliente(this);
                                dbController.insertDefaultCalibre(this);
                                dbController.insertDefaultLocalMode(this);
                                Toast.makeText(this, "Base datos creada correctamente!", Toast.LENGTH_LONG).show();
                        }
                } catch (SQLiteException sqle) {
                        Toast.makeText(this, "Error en consulta: " + sqle, Toast.LENGTH_LONG).show();
                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": "+ new Throwable().getStackTrace()[0].getMethodName() + ": " + sqle); // Agregamos el error al archivo Descargas/Logs.txt
                }
        }

        private void loginLocalOremoto() {
                MdUsuario usuario = new MdUsuario();
                usuario.setCedula(Objects.requireNonNull(txtId.getText()).toString().trim());
                usuario.setPass(HashPass.convertSHA256(txtPass.getText().toString().trim())); // encriptamos la contraseña

                GetStringDate stringDate = new GetStringDate();
                GetStringTime stringTime = new GetStringTime();
                date = stringDate.getFecha();
                time = stringTime.getHora();


                if (!usuario.getCedula().equals("") && !usuario.getPass().equals("")) { // Validando llenado de inputs
                        localMode = stLocalMode.isChecked();
                        if (localMode) { // Si es verdadero logueamos de manera local, sino remoto

                                if (dbController.existUsersData(this)) { // Verificamos si la tabla usuario local tiene registros

                                        if (dbController.loginUser(this, usuario)) {

                                                dbController.nuevoLocalMode(VwLogin.this, 1); // 1 = true = guardar registros en db local
                                                Intent intent = new Intent(this, VwMain.class);
                                                startActivity(intent);

                                        } else {
                                                Toast.makeText(this, "Error: usuario o contraseña incorrecto!", Toast.LENGTH_LONG).show();
                                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": "+ new Throwable().getStackTrace()[0].getMethodName() +" Usuario: " + usuario.getId() + " Password: " + usuario.getPass()); // agregamos el error al archivo Logs.txt
                                        }
                                } else {
                                        Toast.makeText(this, "Error: la tabla usuario esta vacía!", Toast.LENGTH_LONG).show();
                                }

                        } else {

                                // Verificamos que el dispositivo tenga conexion a internet y para login en servidor remoto
                                ConnectivityService con = new ConnectivityService();

                                if (con.stateConnection(this)) {

                                        Call<List<MdUsuario>> requestActividad = ApiUtils.getApiServices().login(usuario.getCedula(), usuario.getPass());

                                        requestActividad.enqueue(new Callback<List<MdUsuario>>() {
                                                @Override
                                                public void onResponse(@NonNull Call<List<MdUsuario>> call, @NonNull Response<List<MdUsuario>> res) {

                                                        if (!res.isSuccessful()) {
                                                                Toast.makeText(VwLogin.this, "No se puede realizar la consulta!", Toast.LENGTH_SHORT).show();
                                                                logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": "+ new Throwable().getStackTrace()[0].getMethodName()  + ": "+ res.message()); // Agregamos el error al archivo Logs.txt

                                                        } else {

                                                                List<MdUsuario> response = res.body();  // Manejar datos obtenidos en el reponse de la peticion

                                                                assert response != null;
                                                                if (response.size() != 0) {

                                                                        for (int i = 0; i < response.size(); i++) {

                                                                                if (usuario.getCedula().equals(response.get(i).getCedula()) && usuario.getPass().equals(response.get(i).getPass())) {
                                                                                        usuario.setNombre(response.get(i).getNombre());
                                                                                        usuario.setDepartamento(response.get(i).getDepartamento());
                                                                                        usuario.setRol(response.get(i).getRol());

                                                                                        if(!dbController.existUser(VwLogin.this, usuario.getCedula())) {  // De no existir un usuario guarda los datos en tabla local
                                                                                                dbController.nuevoUsuario(VwLogin.this, usuario);
                                                                                                dbController.nuevoLocalMode(VwLogin.this, 0); // 0 = falso = guardar registros en db remota
                                                                                        }

                                                                                        Intent intent = new Intent(VwLogin.this, VwMain.class); // Lanzamos actividad principal
                                                                                        startActivity(intent);
                                                                                }
                                                                        }
                                                                } else {
                                                                        Toast.makeText(VwLogin.this, "Usuario o contraseña incorrecto!", Toast.LENGTH_SHORT).show();
                                                                }
                                                        }
                                                }

                                                @Override
                                                public void onFailure(@NonNull Call<List<MdUsuario>> call, @NonNull Throwable t) {
                                                        Toast.makeText(VwLogin.this, "Error: la petición falló!", Toast.LENGTH_SHORT).show();
                                                        logGenerator.generateLogFile(date + ": " + time + ": " + clase + ": " + new Throwable().getStackTrace()[0].getMethodName()+ ": "+ t.getMessage()); // Agregamos el error al archivo Logs.txt
                                                }
                                        });
                                } else {
                                        Toast.makeText(VwLogin.this, "El dispositivo no puede acceder a la red en este momento!", Toast.LENGTH_SHORT).show();
                                }

                        }
                } else {
                        Toast.makeText(this, "Debe rellenar todos los campos!", Toast.LENGTH_LONG).show();
                }
        }

}