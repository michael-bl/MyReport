package com.ciagrolasbrisas.myreport.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class CheckPermissions {

        public CheckPermissions(Context con){
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
                                if (ContextCompat.checkSelfPermission(con, permission)
                                        != PackageManager.PERMISSION_GRANTED) {
                                        permissionsToRequest.add(permission);
                                }
                        }
                        if (!permissionsToRequest.isEmpty()) {
                                ActivityCompat.requestPermissions((Activity) con,
                                        permissionsToRequest.toArray(new String[0]), 7);
                        }
                        if (!Environment.isExternalStorageManager()) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                con. startActivity(intent);
                        }
                } else {
                        if (ContextCompat.checkSelfPermission(con, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) con, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        }
                        if (ContextCompat.checkSelfPermission(con, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions((Activity) con, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                        }
                }
        }
}
