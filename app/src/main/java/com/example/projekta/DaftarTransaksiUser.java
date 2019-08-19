package com.example.projekta;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.projekta.adapter.AViewPager;
import com.example.projekta.fragment.FragmentCreditUser;
import com.example.projekta.fragment.FragmentLunasUser;

public class DaftarTransaksiUser extends AppCompatActivity {

    private TabLayout tabLayout;
    private AViewPager aViewPager;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_transaksi_user);

        tabLayout = findViewById(R.id.tablayout_user);
        viewPager = findViewById(R.id.viewpager_user);
        aViewPager = new AViewPager(getSupportFragmentManager());

        //add fragment here
        aViewPager.AddFragment(new FragmentCreditUser(this),"Credit");
        aViewPager.AddFragment(new FragmentLunasUser(this),"Lunas");
        viewPager.setAdapter(aViewPager);
        tabLayout.setupWithViewPager(viewPager);
    }
}
