package com.example.projekta;

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
import com.example.projekta.api.common.Common;
import com.example.projekta.controller.CEditProfil;
import com.example.projekta.controller.CPesan;
import com.example.projekta.database.User;
import com.example.projekta.model.MTelur;

import java.util.Calendar;
import java.util.List;

import at.markushi.ui.CircleButton;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardUser extends AppCompatActivity {
    private TextView waktu,ucapan,hargaTelur_B,hargaTelur_S;
    private CircleButton new_order,list_orderan, daftar_transaksi;
    private Api_Rest apiRest;
    private String id_telurBesar,id_telurSedang,TS_perTray,TB_perTray;
    private int telurBesar,telurSedang,hTelurB,hTelurS;
    private ImageButton img_setting;
    User user = new User();
    Calendar calendar = Calendar.getInstance();
    int time = calendar.get(Calendar.HOUR_OF_DAY);

    @Override
    public void onBackPressed() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(DashboardUser.this, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitle("Do you want exit ?");
        pDialog.setCancelable(false);
        pDialog.setConfirmText("YES");
        pDialog.setCancelText("NO");

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
        pDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashoarduser);

        apiRest= Common.getApi();
        hargaTelur_B = findViewById(R.id.harga_telur_besar);
        hargaTelur_S = findViewById(R.id.harga_telur_kecil);
        ucapan = findViewById(R.id.ucapan);
        waktu = findViewById(R.id.waktu);
        new_order = findViewById(R.id.new_order);
        list_orderan = findViewById(R.id.list_orderan);
        daftar_transaksi = findViewById(R.id.daftar_transaksi);
        img_setting = findViewById(R.id.img_settingUser);
        Toolbar toolbar =findViewById(R.id.toolbar_dashboard);

        setSupportActionBar(toolbar);
        View view = findViewById(R.id.v_dashboard);
        user = User.findById(User.class,(long)1);
        ucapan.setText("Welcome "+user.getUsername());

        HargaTelur();

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

        new_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CPesan cTelur = new CPesan(DashboardUser.this);
                cTelur.telur(TS_perTray,TB_perTray,id_telurBesar,id_telurSedang);
            }
        });
        list_orderan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listOrderan = new Intent(DashboardUser.this,ListOrderanUser.class);
                startActivity(listOrderan);
            }
        });
        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(DashboardUser.this,img_setting);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.setting_menu,popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edt_profil : {
                                CEditProfil cEditProfil = new CEditProfil(DashboardUser.this);
                                cEditProfil.EditProfil();
                                Toasty.info(DashboardUser.this,"Edit Profil", Toast.LENGTH_SHORT).show();
                                return true;
                            }case R.id.log_out: {
                                Intent intent = new Intent(DashboardUser.this,Login.class);
                                Toasty.info(DashboardUser.this,"Berhasil Log Out", Toast.LENGTH_SHORT).show();
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

        daftar_transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(DashboardUser.this,DaftarTransaksiUser.class);
               startActivity(intent);
            }
        });


    }

    private void HargaTelur() {
        apiRest.telur().enqueue(new Callback<List<MTelur>>() {
            @Override
            public void onResponse(Call<List<MTelur>> call, Response<List<MTelur>> response) {
                if (response.body()==null) {
                    Toasty.error(DashboardUser.this,"Harga Telur Tidak didapatkan");
                }else {
                    List<MTelur> telurs = response.body();
                    for (MTelur telur : telurs) {
                        if (telur.getUkuran_telur().equals("Sedang")) {
                            //harga per butir
                            telurSedang = Integer.valueOf(telur.getHarga_jual()) ;
                            id_telurSedang = telur.getId_telur();
                            //harga per tray
                            hTelurS = telurSedang*30;
                            TS_perTray = String.valueOf(hTelurS);
                            hargaTelur_S.setText(TS_perTray);

                        } else if (telur.getUkuran_telur().equals("Besar")) {
                            telurBesar =Integer.valueOf(telur.getHarga_jual());
                            id_telurBesar = telur.getId_telur();
                            hTelurB = telurBesar*30;
                            TB_perTray = String.valueOf(hTelurB);
                            hargaTelur_B.setText(TB_perTray);
                        } else {
                            Toasty.info(DashboardUser.this, "Harga telur tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<MTelur>> call, Throwable t) {
                Toasty.error(DashboardUser.this,"Gagal Terhubung...",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
