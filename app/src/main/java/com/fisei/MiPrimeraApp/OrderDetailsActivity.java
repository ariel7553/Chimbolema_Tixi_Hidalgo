package com.fisei.MiPrimeraApp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.fisei.MiPrimeraApp.adapters.OrderDetailsArrayAdapter;
import com.fisei.MiPrimeraApp.objects.OrderDetail;
import com.fisei.MiPrimeraApp.objects.Product;
import com.fisei.MiPrimeraApp.services.ProductService;
import com.fisei.MiPrimeraApp.services.SaleService;
import com.fisei.MiPrimeraApp.utilities.Utils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import MiPrimeraApp.R;

public class OrderDetailsActivity extends AppCompatActivity {
    private int orderID = 0;

    private TextView textViewOrderID;
    private TextView textViewOrderUserClient;
    private TextView textViewOrderDate;
    private TextView textViewOrderTotal;
    private TextView textViewOrderTotalIVA;
    private ListView listViewOrderDetails;

    private OrderDetailsArrayAdapter orderArrayAdapter;

    Bundle bundle;
    private List<OrderDetail> orderDetails = new ArrayList<>();
    private List<Product> saleDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        bundle = getIntent().getExtras();
        orderID = bundle.getInt("orderID");
        InitializeViewComponents();
        orderArrayAdapter = new OrderDetailsArrayAdapter(this, orderDetails);

        GetOrderDetailsTask getOrderDetailsTask = new GetOrderDetailsTask();
        getOrderDetailsTask.execute();
    }

    class GetOrderDetailsTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            saleDetails = SaleService.GetSalesDetailsByID(orderID);
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            GetProductTask getProductTask = new GetProductTask();
            getProductTask.execute();
        }
    }
    class GetProductTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... params) {
            for (Product item: saleDetails) {
                orderDetails.add(ConvertProductToOrderDetail(ProductService.GetSpecifiedProductByID(item.id), item.quantity));
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject){
            orderArrayAdapter.notifyDataSetChanged();
            listViewOrderDetails.setAdapter(orderArrayAdapter);
            //orderDetails.clear();
        }
    }
    private OrderDetail ConvertProductToOrderDetail(Product product, int qty){
        return new OrderDetail(product.name, qty, product.unitPrice, product.imageURL);
    }
    private void InitializeViewComponents(){
        textViewOrderID = (TextView) findViewById(R.id.textViewOrderInfoID);
        textViewOrderUserClient = (TextView) findViewById(R.id.textViewOrderInfoUserClient);
        textViewOrderDate = (TextView) findViewById(R.id.textViewOrderInfoDate);
        textViewOrderTotal = (TextView) findViewById(R.id.textViewOrderInfoTotal);
        listViewOrderDetails = (ListView) findViewById(R.id.listViewOrderDetails);
        textViewOrderTotalIVA = (TextView) findViewById(R.id.textViewOrderInfoTotalIva);
        FillOrderHeader();
    }
    private void FillOrderHeader(){
        textViewOrderID.setText(String.format("%s",bundle.getInt("orderID")));
        textViewOrderUserClient.setText(String.format("%s",bundle.getInt("orderUserClient")));
        textViewOrderDate.setText(String.format("%s",Utils.ConvertDate(bundle.getString("orderDate"))));
        textViewOrderTotal.setText(String.format("%s",bundle.getDouble("orderTotal") + " $"));
        textViewOrderTotalIVA.setText(String.format("%.2f",bundle.getDouble("orderTotal") / 1.1 )+ " $");
    }
}