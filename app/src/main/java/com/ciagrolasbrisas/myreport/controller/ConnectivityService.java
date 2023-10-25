package com.ciagrolasbrisas.myreport.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityService {


    /**
     * Método para verificar si el dispositivo cuenta con conexion a internet.
     *
     * @return falso o verdadero.
     */
    public boolean stateConnection(Context context) {

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
}
