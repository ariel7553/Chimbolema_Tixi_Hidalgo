package com.fisei.athanasiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fisei.athanasiaapp.models.ResponseAthanasia;
import com.fisei.athanasiaapp.objects.UserClient;
import com.fisei.athanasiaapp.services.UserClientService;

import org.json.JSONObject;

import java.net.URL;
import java.util.regex.Pattern;

public class SingUpActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextCedula;
    private EditText editTextPassword;
    private TextView errorTextView;
    private Button buttonSignUp;
    private ResponseAthanasia responseTask = new ResponseAthanasia(false, "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        InitializeViewComponents();



    }


    private class SignUpTask extends AsyncTask<URL, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(URL... urls) {
            UserClient newUser = new UserClient(0, editTextName.getText().toString(),
                    editTextEmail.getText().toString() + "@ath.com",
                    editTextCedula.getText().toString(), "");
            responseTask = UserClientService.SignUpNewUser(newUser, editTextPassword.getText().toString());
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject){
            if(responseTask.Success){
                StartLoginActivity();
            } else {
                errorTextView.setText(responseTask.Message);
            }
            responseTask.Success = false;
        }
    }
    private void InitializeViewComponents(){
        editTextEmail = (EditText) findViewById(R.id.editTextSignUpEmail);
        editTextName = (EditText) findViewById(R.id.editTextSignUpName);
        editTextCedula = (EditText) findViewById(R.id.editTextSignUpCedula);
        editTextPassword = (EditText) findViewById(R.id.editTextSignUpPassword);
        errorTextView = (TextView) findViewById(R.id.textViewSignUpFail1);
        buttonSignUp = (Button) findViewById(R.id.btnSignUp);
        buttonSignUp.setOnClickListener(signUpButtonClicked);
    }
    //
    private void SignUp() {
        if (editTextEmail.getText().toString().isEmpty() || editTextName.getText().toString().isEmpty() ||
                editTextCedula.getText().toString().isEmpty() || editTextPassword.getText().toString().isEmpty()) {
            errorTextView.setText(R.string.fields_empty_error);

        } else {
            String contraseña = editTextPassword.getText().toString();
            if (contraseña.length() < 6 || contraseña.length() > 10){
                Toast.makeText(this, "La contraseña debe  tener entre 6 y 10 caracteres", Toast.LENGTH_LONG).show();
                return;
            }
            boolean ma = contraseña.matches(".*[[:punct:]]");
            if (!contraseña.matches(".*[[:punct:]]")) {
                Toast.makeText(this, "debe tener caracteres especiales", Toast.LENGTH_LONG).show();
                return;
            }

            if (!contraseña.matches(".*\\d.*")) {
                Toast.makeText(this, "debe tener numeros", Toast.LENGTH_LONG).show();
                return;
            }
            if (!contraseña.matches(".*[a-z].*")) {
                Toast.makeText(this, "Debe tener una letra minuscula", Toast.LENGTH_LONG).show();
                return;
            }
            if (!contraseña.matches(".*[A-Z].*")) {
                Toast.makeText(this, "Debe tener una letra mayúsculas", Toast.LENGTH_LONG).show();
                return;
            }

            errorTextView.setText("");
            SignUpTask signUpTask = new SignUpTask();
            signUpTask.execute();




/*
            if (contraseña.length() < 10) {
                Toast.makeText(this, "La contraseña debe tener máximo 10 caráteres", Toast.LENGTH_LONG).show();
            } else {
                if (contraseña.length() > 6) {
                    Toast.makeText(this, "La contraseña debe tener mÍnimo 6 carácteres", Toast.LENGTH_LONG).show();
                } else {
                    if (!contraseña.matches("(?=.*[@#$%^&+=])")) {
                        Toast.makeText(this, "debe tener caracteres especiales", Toast.LENGTH_LONG).show();
                    } else {
                        if (!contraseña.matches("(?=.*[0-9])")) {
                            Toast.makeText(this, "debe tener numeros", Toast.LENGTH_LONG).show();
                        } else {
                            if (!contraseña.matches("(?=.*[a-z])")) {
                                Toast.makeText(this, "debe tener una letra minuscula", Toast.LENGTH_LONG).show();
                            } else {
                                if (!contraseña.matches("(?=.*[A-Z])")) {
                                    Toast.makeText(this, "debe tener una letra mayusculas", Toast.LENGTH_LONG).show();
                                }else{
                                    errorTextView.setText("");
                                    SignUpTask signUpTask = new SignUpTask();
                                    signUpTask.execute();
                                }
                            }
                        }
                    }
                }
            }*/
        }

    }



    private void StartLoginActivity(){
        Intent backLogin = new Intent(this, LoginActivityVELLC.class);
        startActivity(backLogin);
        Toast.makeText(this, "Your register was successful", Toast.LENGTH_SHORT).show();
    }
    private final View.OnClickListener signUpButtonClicked = view -> SignUp();

}