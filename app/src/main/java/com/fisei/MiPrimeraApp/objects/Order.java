package com.fisei.MiPrimeraApp.objects;

public class Order {
    public int ID;
    public String Date;
    public int UserClientID;
    public double Total;

    public Order(int id, String date, int userID, double total){
        this.ID = id;
        this.Date = date;
        this.UserClientID = userID;
        this.Total = total;
    }
}
