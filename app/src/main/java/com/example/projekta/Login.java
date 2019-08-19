package com.example.projekta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.projekta.controller.CLogin;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CLogin cLogin = new CLogin(this);
        cLogin.login();

    }
}
