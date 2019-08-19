package com.example.projekta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.controller.CEditHargaTelur;
import com.example.projekta.controller.CEditProfil;
import com.example.projekta.database.User;

import java.util.Calendar;

import at.markushi.ui.CircleButton;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;


public class DashboardAdmin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private TextView waktu,ucapan;
    private CircleButton daftar_orderan,daftar_kiriman,daftar_pembayaran,laporan_penjualan;
    User user = new User();
    Calendar calendar = Calendar.getInstance();
    int time = calendar.get(Calendar.HOUR_OF_DAY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

        waktu = findViewById(R.id.waktu_admin);
        ucapan = findViewById(R.id.ucapan_admin);
        daftar_orderan = findViewById(R.id.list_orderanAdmin);
        daftar_kiriman = findViewById(R.id.list_kiriman);
        daftar_pembayaran = findViewById(R.id.daftar_pembayaran);
        laporan_penjualan = findViewById(R.id.cetak_laporan);
        user = User.findById(User.class,(long)1);
        ucapan.setText("Welcome Admin "+user.getUsername());

        Toolbar toolbar = findViewById(R.id.toolbar_dashboardAdmin);
        setSupportActionBar(toolbar);

        View view = findViewById(R.id.v_dashboard_admin);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (time >= 0 && time < 11) {
            waktu.setText("Selamat Pagi  :)");
            toolbar.setBackgroundColor(getResources().getColor(R.color.pagi));
            view.setBackgroundColor(getResources().getColor(R.color.pagi));

        } else if (time >= 11 && time < 16) {
            waktu.setText("Selamat Siang  :)");
            toolbar.setBackgroundColor(getResources().getColor(R.color.siang));
            view.setBackgroundColor(getResources().getColor(R.color.siang));
        } else if (time >= 16 && time < 20) {
            waktu.setText("Selamat Sore  :)");
            toolbar.setBackgroundColor(getResources().getColor(R.color.sore));
            view.setBackgroundColor(getResources().getColor(R.color.sore));
        } else if (time >= 20 && time < 24) {
            waktu.setText("Selamat Malam  :)");
            toolbar.setBackgroundColor(getResources().getColor(R.color.malam));
            view.setBackgroundColor(getResources().getColor(R.color.malam));
        }

        daftar_orderan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent daftarOrderan = new Intent(DashboardAdmin.this,DaftarPesananAdmin.class);
                startActivity(daftarOrderan);
            }
        });

        daftar_kiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardAdmin.this,DaftarKirimanAdmin.class);
                startActivity(intent);
            }
        });

        daftar_pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardAdmin.this,DaftarPembayaranAdmin.class);
                startActivity(intent);
            }
        });

        laporan_penjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardAdmin.this,CetakLaporan.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            final SweetAlertDialog pDialog = new SweetAlertDialog(DashboardAdmin.this, SweetAlertDialog.WARNING_TYPE);
            pDialog.setTitle("Do you want exit ?");
            pDialog.setCancelable(false);
            pDialog.setConfirmText("YES");
            pDialog.setCancelText("NO");
            pDialog.show();
            pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    finish();
                }
            });

            pDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    pDialog.dismiss();
                }
            });

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_editProfil:
                CEditProfil cEditProfil = new CEditProfil(DashboardAdmin.this);
                cEditProfil.EditProfil();
                Toasty.info(DashboardAdmin.this,"Edit Profil", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_dataUser:
                String user_level = "0";
                Intent dataUser = new Intent(DashboardAdmin.this,DataUser.class);
                dataUser.putExtra("user_level",user_level);
                startActivity(dataUser);
                Toasty.info(DashboardAdmin.this,"Data User", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_dataKurir:
                String kurir = "1";
                Intent dataKurir = new Intent(DashboardAdmin.this,DataUser.class);
                dataKurir.putExtra("user_level",kurir);
                startActivity(dataKurir);
                Toasty.info(DashboardAdmin.this,"Data Kurir", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_hargaTelur:
                CEditHargaTelur cEditHargaTelur = new CEditHargaTelur(DashboardAdmin.this);
                cEditHargaTelur.editHargaTelur();
                Toasty.info(DashboardAdmin.this,"Harga Telur", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_logOut:
                Intent intent = new Intent(DashboardAdmin.this,Login.class);
                Toasty.info(DashboardAdmin.this,"Log Out", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(intent);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
