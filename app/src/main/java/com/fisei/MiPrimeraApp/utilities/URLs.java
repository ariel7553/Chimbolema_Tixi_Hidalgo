package com.fisei.MiPrimeraApp.utilities;

public class URLs {

    private  static String base_url = "https://serviciosFactura.somee.com/APISERVICE/api";

    //Client
    public static final String LOGIN = base_url+"/userClient/login";
    public static final String LOGIN_ADMIN = base_url+"/userAdmin/login";
    public static final String CLIENT_BY_ID = base_url+"/clients/";  //...clients/ID_TO_SEARCH
    public static final String SIGN_UP = base_url+"/clients";
    public static final String SHOPPING_CART = base_url+"/shoppingcart";
    //Product
    public static final String PRODUCTS = base_url+"/product";

    //Images
    public static final String PRODUCTS_IMAGES = "https://i.ibb.co/";
    //Sales
    public static final String SALES = base_url+"/sale";
    public static final String SALE_DETAILS = base_url+"/saleDetails/";

 /*
    ///*
    public static final String LOGIN = "http://localhost:5295/api/userClient/login";
    public static final String LOGIN_ADMIN = "http://localhost:5295/api/userAdmin/login";
    public static final String CLIENT_BY_ID = "http://localhost:5295/api/clients/";  //...clients/ID_TO_SEARCH
    public static final String SIGN_UP = "http://localhost:5295/api/clients";
    public static final String SHOPPING_CART = "http://localhost:5295/api/shoppingcart";
    //Product
    public static final String PRODUCTS = "http://localhost:5295/api/product";
    //Images
    public static final String PRODUCTS_IMAGES = "https://i.ibb.co/";
    //Sales
    public static final String SALES = "http://localhost:5295/api/sale";
    public static final String SALE_DETAILS = "http://localhost:5295/api/saleDetails/";
    */


}
