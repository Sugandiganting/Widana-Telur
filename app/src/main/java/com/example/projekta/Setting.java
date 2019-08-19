package com.example.projekta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.projekta.controller.CSetting;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        CSetting cSetting = new CSetting(this);
        cSetting.setting();
    }
}
