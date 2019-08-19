package com.example.projekta;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.projekta.adapter.AViewPager;
import com.example.projekta.fragment.FragmentAccept;
import com.example.projekta.fragment.FragmentPanding;
import com.example.projekta.fragment.FragmentReject;

public class DaftarPesananAdmin extends AppCompatActivity {

    private TabLayout tabLayout;
    private AViewPager aViewPager;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pesanan_admin);

        tabLayout = findViewById(R.id.tablayout_id);
        viewPager = findViewById(R.id.viewpager_id);
        aViewPager = new AViewPager(getSupportFragmentManager());

        //add fragment here
        aViewPager.AddFragment(new FragmentPanding(this),"Panding");
        aViewPager.AddFragment(new FragmentAccept(this),"Accept");
        aViewPager.AddFragment(new FragmentReject(this),"Reject");
        viewPager.setAdapter(aViewPager);
        tabLayout.setupWithViewPager(viewPager);
    }
}
