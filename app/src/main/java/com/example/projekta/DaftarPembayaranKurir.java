package com.example.projekta;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.projekta.adapter.AViewPager;
import com.example.projekta.fragment.FragmentCashKurir;
import com.example.projekta.fragment.FragmentCreditKurir;

public class DaftarPembayaranKurir extends AppCompatActivity {

    private TabLayout tabLayout;
    private AViewPager aViewPager;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pembayaran_kurir);

        tabLayout = findViewById(R.id.tablayout_kurir);
        viewPager = findViewById(R.id.viewpager_kurir);
        aViewPager = new AViewPager(getSupportFragmentManager());

        //add fragment here
        aViewPager.AddFragment(new FragmentCreditKurir(this ),"Credit");
        aViewPager.AddFragment(new FragmentCashKurir(this),"Cash");
        viewPager.setAdapter(aViewPager);
        tabLayout.setupWithViewPager(viewPager);

    }
}
