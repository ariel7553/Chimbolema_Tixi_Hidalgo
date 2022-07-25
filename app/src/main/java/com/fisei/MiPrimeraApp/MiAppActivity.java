package com.fisei.MiPrimeraApp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.fisei.MiPrimeraApp.objects.MiAppGlobal;
import com.fisei.MiPrimeraApp.objects.UserClient;
import com.fisei.MiPrimeraApp.services.UserClientService;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;



import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import MiPrimeraApp.R;
import MiPrimeraApp.databinding.ActAppBinding;


public class MiAppActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActAppBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActAppBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBar.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_shop, R.id.nav_orders, R.id.nav_shop_cart).setOpenableLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        if(MiAppGlobal.ADMIN_PRIVILEGES){
            Menu menuNav=navigationView.getMenu();
            menuNav.removeItem(R.id.nav_shop_cart);
        }
        GetUserClientInfoTask getUserClientInfo = new GetUserClientInfoTask();
        getUserClientInfo.execute();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.miapp, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout){
            LogOut();
        }
        return false;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }
    private class GetUserClientInfoTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            String tempToken = MiAppGlobal.ACTUAL_USER.JWT;
            MiAppGlobal.ACTUAL_USER = UserClientService.GetUserInfoByID(MiAppGlobal.ACTUAL_USER.ID);
            MiAppGlobal.ACTUAL_USER.JWT = tempToken;
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            //Las vistas no pueden ser editadas en un hilo diferente al main
            if(MiAppGlobal.ADMIN_PRIVILEGES){
                ShowAdminInfo();
            } else {
                ShowUserInfo();
            }
        }
    }


    private void ShowUserInfo(){
        TextView userName = (TextView) findViewById(R.id.textViewUserName);
        userName.setText(MiAppGlobal.ACTUAL_USER.Name);
        TextView userEmail = (TextView) findViewById(R.id.textViewUserEmail);
        userEmail.setText(MiAppGlobal.ACTUAL_USER.Email);
    }
    private void ShowAdminInfo(){
        TextView userName = (TextView) findViewById(R.id.textViewUserName);
        userName.setText("ADMIN");
        TextView userEmail = (TextView) findViewById(R.id.textViewUserEmail);
        userEmail.setText("ADMIN");
    }
    private void LogOut(){
        MiAppGlobal.ACTUAL_USER = new UserClient();
        MiAppGlobal.ADMIN_PRIVILEGES = false;
        MiAppGlobal.SHOPPING_CART = new ArrayList<>();
        Intent login = new Intent(this, LoginActivityVELLC.class);
        startActivity(login);
        finish();
    }
}