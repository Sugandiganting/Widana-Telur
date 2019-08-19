package com.example.projekta;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.projekta.adapter.AViewPager;
import com.example.projekta.fragment.FragmentLunasAdmin;
import com.example.projekta.fragment.FragmentPembayaranCreditAdmin;
import com.example.projekta.fragment.FragmentPickUpAdmin;

public class DaftarPembayaranAdmin extends AppCompatActivity {

    private TabLayout tabLayout;
    private AViewPager aViewPager;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pembayaran_admin);


        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        aViewPager = new AViewPager(getSupportFragmentManager());

        //add fragment here
        aViewPager.AddFragment(new FragmentPickUpAdmin(this),"Pick Up");
        aViewPager.AddFragment(new FragmentPembayaranCreditAdmin(this),"Credit");
        aViewPager.AddFragment(new FragmentLunasAdmin(this),"Sukses");
        viewPager.setAdapter(aViewPager);
        tabLayout.setupWithViewPager(viewPager);
    }
}
