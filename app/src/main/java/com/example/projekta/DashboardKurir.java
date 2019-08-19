package com.example.projekta;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.api.Api_Rest;
import com.example.projekta.controller.CEditProfil;
import com.example.projekta.database.User;

import java.util.Calendar;

import at.markushi.ui.CircleButton;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;

public class DashboardKurir extends AppCompatActivity {
    private TextView waktu,ucapan;
    private CircleButton daftar_kiriman, daftar_pembayaran;
    private ImageButton img_setting;

    User user = new User();
    Calendar calendar = Calendar.getInstance();
    int time = calendar.get(Calendar.HOUR_OF_DAY);

    @Override
    public void onBackPressed() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(DashboardKurir.this, SweetAlertDialog.WARNING_TYPE);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_kurir);

        ucapan = findViewById(R.id.ucapan);
        waktu = findViewById(R.id.waktu);
        daftar_kiriman = findViewById(R.id.daftar_kirim);
        daftar_pembayaran = findViewById(R.id.daftar_pembayaran);
        img_setting = findViewById(R.id.setting_kurir);

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(DashboardKurir.this,img_setting);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.setting_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edt_profil : {
                                CEditProfil cEditProfil = new CEditProfil(DashboardKurir.this);
                                cEditProfil.EditProfil();
                                Toasty.info(DashboardKurir.this,"Edit Profil", Toast.LENGTH_SHORT).show();
                                return true;
                            }case R.id.log_out: {
                                Intent intent = new Intent(DashboardKurir.this,Login.class);
                                Toasty.info(DashboardKurir.this,"Berhasil Log Out", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(intent);
                                return true;
                            } default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        Toolbar toolbar =findViewById(R.id.toolbar_dashboard);

        setSupportActionBar(toolbar);
        View view = findViewById(R.id.v_dashboard);
        user = User.findById(User.class,(long)1);
        ucapan.setText("Welcome "+user.getUsername());

        if (time >= 0 && time < 11) {
            waktu.setText("Selamat Pagi  :)");
            toolbar.setBackgroundColor(getResources().getColor(R.color.pagi));
            view.setBackgroundColor(getResources().getColor(R.color.pagi));
            img_setting.setBackgroundColor(getResources().getColor(R.color.pagi));
        } else if (time >= 11 && time < 16) {
            waktu.setText("Selamat Siang  :)");
            toolbar.setBackgroundColor(getResources().getColor(R.color.siang));
            view.setBackgroundColor(getResources().getColor(R.color.siang));
            img_setting.setBackgroundColor(getResources().getColor(R.color.siang));
        } else if (time >= 16 && time < 20) {
            waktu.setText("Selamat Sore  :)");
            toolbar.setBackgroundColor(getResources().getColor(R.color.sore));
            view.setBackgroundColor(getResources().getColor(R.color.sore));
            img_setting.setBackgroundColor(getResources().getColor(R.color.sore));
        } else if (time >= 20 && time < 24) {
            waktu.setText("Selamat Malam  :)");
            toolbar.setBackgroundColor(getResources().getColor(R.color.malam));
            view.setBackgroundColor(getResources().getColor(R.color.malam));
            img_setting.setBackgroundColor(getResources().getColor(R.color.malam));
        }

        daftar_kiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardKurir.this,DaftarKirimanKurir.class);
                startActivity(intent);
            }
        });

        daftar_pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardKurir.this,DaftarPembayaranKurir.class);
                startActivity(intent);
            }
        });

    }
}
