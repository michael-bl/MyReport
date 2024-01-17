package com.ciagrolasbrisas.myreport.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ciagrolasbrisas.myreport.R;

public class VwUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vw_usuario);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(), VwMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}