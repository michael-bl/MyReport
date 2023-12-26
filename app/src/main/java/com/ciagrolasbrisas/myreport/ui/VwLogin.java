package com.ciagrolasbrisas.myreport.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.controller.ApiUtils;
import com.ciagrolasbrisas.myreport.controller.CacheManager;
import com.ciagrolasbrisas.myreport.controller.CheckPermissions;
import com.ciagrolasbrisas.myreport.controller.ConnectivityService;
import com.ciagrolasbrisas.myreport.controller.GetStringDate;
import com.ciagrolasbrisas.myreport.controller.GetStringTime;
import com.ciagrolasbrisas.myreport.controller.HashPass;
import com.ciagrolasbrisas.myreport.controller.LogGenerator;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.model.MdUsuario;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VwLogin extends AppCompatActivity {
        private TextInputEditText txtId, txtPass;
        private DatabaseController dbController;
        private Switch stLocalMode;
        public static String dniUser; // Variable global
        private boolean localMode;
        private String date, time;
        private LogGenerator logGenerator;
        private CacheManager myCache;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.vw_login);

                myCache = new CacheManager();

                crearDbLocal();

                stLocalMode = findViewById(R.id.switchModoLocal);

                localMode = stLocalMode.isChecked();
                if(!myCache.getIfExist(  "localMode")){
                        myCache.saveToCache("localMode", stLocalMode); // Guarda en cache el modo de almacenamiento general de datos
                }

                logGenerator = new LogGenerator();

                CheckPermissions permisos = new CheckPermissions(this);  // Lanza activity para solicitar permisos correspondientes

                txtId = findViewById(R.id.txtLoginUser);
                txtPass = findViewById(R.id.txtLoginPass);
                Button btnLogin = findViewById(R.id.btnLogin);
                btnLogin.setOnClickListener(v -> loginLocalOremoto());
        }

        private void checkAndRequestPermissions() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        String[] permissions = new String[]{
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                        };
                        List<String> permissionsToRequest = new ArrayList<>();
                        for (String permission : permissions) {
                                if (ContextCompat.checkSelfPermission(this, permission)
                                        != PackageManager.PERMISSION_GRANTED) {
                                        permissionsToRequest.add(permission);
                                }
                        }
                        if (!permissionsToRequest.isEmpty()) {
                                ActivityCompat.requestPermissions(this,
                                        permissionsToRequest.toArray(new String[0]), 7);
                        }
                        if (!Environment.isExternalStorageManager()) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                startActivity(intent);
                        }
                } else {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }
                }
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
                        logGenerator.generateLogFile(date + ": " + time + ": " + sqle.getMessage()); // agregamos el error al archivo Logs.txt
                }
        }

        private void loginLocalOremoto() {
                MdUsuario usuario = new MdUsuario();
                usuario.setId(Objects.requireNonNull(txtId.getText()).toString().trim());
                usuario.setPass(Objects.requireNonNull(txtPass.getText()).toString().trim());

                GetStringDate stringDate = new GetStringDate();
                GetStringTime stringTime = new GetStringTime();
                date = stringDate.getFecha();
                time = stringTime.getHora();


                if (!usuario.getId().equals("") && !usuario.getPass().equals("")) { // Validando llenado de inputs

                        if (localMode) { // Si es verdadero logueamos de manera local, sino remoto

                                if (dbController.existUsersData(this)) { // Verificamos si la tabla usuario local tiene registros

                                        if (dbController.loginUser(this, usuario)) {

                                                dniUser = usuario.getId();
                                                Intent intent = new Intent(this, VwMain.class);
                                                startActivity(intent);

                                        } else {
                                                Toast.makeText(this, "Error: usuario o contraseña incorrecto!", Toast.LENGTH_LONG).show();
                                                logGenerator.generateLogFile(date + ": " + time + ": " + "Usuario: " + usuario.getId() + "Password: " + usuario.getPass()); // agregamos el error al archivo Logs.txt
                                        }
                                } else {
                                        Toast.makeText(this, "Error: la tabla usuario esta vacía!", Toast.LENGTH_LONG).show();
                                }

                        } else {

                                // Verificamos que el dispositivo tenga coneccion a internet y hacer login en servidor remoto
                                ConnectivityService con = new ConnectivityService();

                                if (con.stateConnection(this)) {

                                        usuario.setPass(HashPass.convertSHA256(txtPass.getText().toString().trim())); // encriptamos la contraseña

                                        Call<List<MdUsuario>> requestActividad = ApiUtils.getApiServices().login(usuario.getId(), usuario.getPass());

                                        requestActividad.enqueue(new Callback<List<MdUsuario>>() {
                                                @Override
                                                public void onResponse(@NonNull Call<List<MdUsuario>> call, @NonNull Response<List<MdUsuario>> res) {

                                                        if (!res.isSuccessful()) {
                                                                Toast.makeText(VwLogin.this, "No se puede realizar la consulta!", Toast.LENGTH_SHORT).show();
                                                                logGenerator.generateLogFile(date + ": " + time + ": " + res.message()); // Agregamos el error al archivo Logs.txt

                                                        } else {

                                                                List<MdUsuario> response = res.body();  // Manejar datos obtenidos en el reponse de la peticion

                                                                assert response != null;
                                                                if (response.size() != 0) {

                                                                        for (int i = 0; i < response.size(); i++) {

                                                                                if (usuario.getId().equals(response.get(i).getId()) && usuario.getPass().equals(response.get(i).getPass())) {

                                                                                        if(!dbController.existUser(VwLogin.this, usuario.getId())) {  // De no existir un usuario guarda los datos en tabla local
                                                                                                dbController.nuevoUsuario(VwLogin.this, usuario);
                                                                                        }

                                                                                        usuario.setNombre(response.get(i).getNombre());
                                                                                        dniUser = usuario.getId(); // Lanzamos actividad principal
                                                                                        Intent intent = new Intent(VwLogin.this, VwMain.class);
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
                                                        logGenerator.generateLogFile(date + ": " + time + ": " + t.getMessage()); // Agregamos el error al archivo Logs.txt
                                                }
                                        });
                                } else {
                                        Toast.makeText(VwLogin.this, "El dispositivo no puede accesar a la red en este momento!", Toast.LENGTH_SHORT).show();
                                }

                        }
                } else {
                        Toast.makeText(this, "Debe rellenar todos los campos!", Toast.LENGTH_LONG).show();
                }
        }

}