package com.ciagrolasbrisas.myreport.controller;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashPass {
public HashPass(){
}
    public static String convertSHA256(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(password.getBytes());
        StringBuffer sb = new StringBuffer();

        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
