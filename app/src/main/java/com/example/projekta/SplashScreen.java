package com.example.projekta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.projekta.database.Pengaturan;

public class SplashScreen extends AppCompatActivity {

    private ImageView iv_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        iv_logo = findViewById(R.id.iv_logo);
        Animation myAnim = AnimationUtils.loadAnimation(this,R.anim.tranisi);
        iv_logo.setAnimation(myAnim);

        if (Pengaturan.count(Pengaturan.class, null, null)>0){
            final Intent intent = new Intent(SplashScreen.this, Login.class);
            Thread timer=new Thread(){
                public void run(){
                    try {
                        sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }finally {
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timer.start();
        }else{
            final Intent intent = new Intent(SplashScreen.this, Setting.class);
            Thread timer = new Thread(){
                public void run(){
                    try {
                        sleep(2000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }finally {
                        startActivity(intent);
                        finish();
                    }
                }
            };
            timer.start();
        }
    }
}
