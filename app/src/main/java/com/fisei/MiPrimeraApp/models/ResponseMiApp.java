package com.fisei.MiPrimeraApp.models;

public class ResponseMiApp {
    public boolean Success;
    public String Message;

    public ResponseMiApp(Boolean success, String message){
        this.Success = success;
        this.Message = message;
    }
}