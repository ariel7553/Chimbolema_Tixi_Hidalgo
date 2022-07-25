package com.fisei.MiPrimeraApp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fisei.MiPrimeraApp.objects.MiAppGlobal;
import com.fisei.MiPrimeraApp.objects.Product;
import com.fisei.MiPrimeraApp.objects.ShopCartItem;
import com.fisei.MiPrimeraApp.objects.UserClient;
import com.fisei.MiPrimeraApp.services.ProductService;
import com.fisei.MiPrimeraApp.services.ShoppingCartService;
import com.fisei.MiPrimeraApp.services.UserAdminService;
import com.fisei.MiPrimeraApp.services.UserClientService;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import MiPrimeraApp.R;

public class LoginActivityVELLC extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwdEditText;
    private TextView warningTextView;

    private UserClient user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitializeViewComponents();
    }
    //
    private void Login(){
            warningTextView.setText("");
            LoginTask loginTask = new LoginTask();
            loginTask.execute();
    }


    private Boolean LoginAdmin(){
            warningTextView.setText("");
            LoginAdminTask loginAdminTask = new LoginAdminTask();
            loginAdminTask.execute();
        return true;
    }
    private void SignUp(){
        Intent register = new Intent(this, SingUpActivity.class);
        startActivity(register);
    }
    private class LoginTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            user = UserClientService.Login(emailEditText.getText().toString(), passwdEditText.getText().toString());
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(user.JWT != null){
                MiAppGlobal.ACTUAL_USER.JWT = user.JWT;
                MiAppGlobal.ACTUAL_USER.ID = user.ID;
                GetUserCartTask getUserCartTask = new GetUserCartTask();
                getUserCartTask.execute();
            } else {
                warningTextView.setText(R.string.label_wrong_email_password);
            }
        }
    }
    private class LoginAdminTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            user = UserAdminService.Login(emailEditText.getText().toString(), passwdEditText.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(user.JWT != null){
                MiAppGlobal.ACTUAL_USER.JWT = user.JWT;
                MiAppGlobal.ADMIN_PRIVILEGES = true;
                StartMiAppActivity();
            } else {
                warningTextView.setText(R.string.label_wrong_email_password);
            }
        }
    }
    private class GetUserCartTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            MiAppGlobal.SHOPPING_CART = ShoppingCartService.GetShopCartFromUserLogged(user.ID);
            List<ShopCartItem> tempList = new ArrayList<>();
            for (ShopCartItem item: MiAppGlobal.SHOPPING_CART) {
                Product p = ProductService.GetSpecifiedProductByID(item.Id);
                if(p.quantity < item.Quantity){
                    item.Quantity = 1;
                }
                tempList.add(new ShopCartItem(p.id, p.name, p.imageURL, item.Quantity, p.unitPrice, p.quantity));
            }
            MiAppGlobal.SHOPPING_CART = tempList;
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            StartMiAppActivity();
        }
    }

    private void StartMiAppActivity(){
        Intent loginSuccesful = new Intent(this, MiAppActivity.class);
        startActivity(loginSuccesful);
        finish();
    }
    private void InitializeViewComponents(){
        emailEditText = findViewById(R.id.editTextEmailLogin);
        passwdEditText = findViewById(R.id.editTextPassword);
        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(loginButtonClicked);
        loginButton.setOnLongClickListener(loginAdminButtonClicked);
        Button signUpButton = findViewById(R.id.btnRegister);
        signUpButton.setOnClickListener(signUpButtonClicked);
        warningTextView = findViewById(R.id.textViewLoginFailed);
    }
    private final View.OnClickListener loginButtonClicked = view -> Login();
    private final View.OnClickListener signUpButtonClicked = view -> SignUp();
    private final View.OnLongClickListener loginAdminButtonClicked = view -> LoginAdmin();
}