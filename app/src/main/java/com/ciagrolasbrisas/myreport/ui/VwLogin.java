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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ciagrolasbrisas.myreport.R;
import com.ciagrolasbrisas.myreport.database.DatabaseController;
import com.ciagrolasbrisas.myreport.model.MdUsuario;
import com.google.android.material.textfield.TextInputEditText;

public class VwLogin extends AppCompatActivity {
    private TextInputEditText txtId, txtPass;
    private DatabaseController dbController;
    public static String dniUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vw_login);
        crearDbLocal();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        } else {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.R){
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                }

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                }
            }
        }

        txtId = findViewById(R.id.txtLoginUser);
        txtPass= findViewById(R.id.txtLoginPass);
        Button botonLogin = findViewById(R.id.btnLogin);
        botonLogin.setOnClickListener(v -> loginLocalOremoto());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    private void crearDbLocal(){
        dbController = new DatabaseController();
        try {
            String resultadoConsulta = dbController.crearDbLocal(this);
                if (resultadoConsulta.equals("2")) {
                    dbController.insertDefaultCuelloBotella(this);
                    dbController.insertDefaultUsuario(this);
                    dbController.insertDefaultCliente(this);
                    dbController.insertDefaultCalibre(this);
                    Toast.makeText(this, "Base datos creada correctamente!", Toast.LENGTH_LONG).show();
                }
        } catch (SQLiteException sqle) {
            Toast.makeText(this, "Error de ejecución: " + sqle, Toast.LENGTH_LONG).show();
        }
    }

    private void loginLocalOremoto(){
        MdUsuario usuario = new MdUsuario();
        usuario.setId(txtId.getText().toString().trim());
        usuario.setPass(txtPass.getText().toString().trim());

        if(dbController.loginUser(this, usuario))
        {
            dniUsuario = usuario.getId();
            Intent intent = new Intent(this, VwMain.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Error: usuario o contraseña incorrecto", Toast.LENGTH_LONG).show();
        }
    }

}