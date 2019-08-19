package com.example.projekta.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projekta.Login;
import com.example.projekta.R;
import com.example.projekta.database.Pengaturan;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;

public class CSetting {
    MaterialEditText edt_ip,edt_invoice,edt_namaToko,edt_alamatToko;
    Button btn_setUp;
    Context context;
    AlertDialog dialog;

    public CSetting(Context context){
        this.context = context;
    }

    public void setting(){
        edt_ip = (((Activity)context).findViewById(R.id.edt_ipadress));
        edt_invoice = (((Activity)context).findViewById(R.id.edt_invoice));
        edt_namaToko = (((Activity)context).findViewById(R.id.edt_namaToko));
        edt_alamatToko = (((Activity)context).findViewById(R.id.edt_alamatToko));
        btn_setUp = ((Activity)context).findViewById(R.id.btn_set_up);

        dialog = new SpotsDialog.Builder().setContext(context).build();

        if (Pengaturan.count(Pengaturan.class, null, null)>0){
            Pengaturan pengaturan = Pengaturan.findById(Pengaturan.class, (long)1);
            edt_ip.setText(pengaturan.getIp_address());
            edt_invoice.setText(pengaturan.getInvoice());
            edt_namaToko.setText(pengaturan.getNama_toko());
            edt_alamatToko.setText(pengaturan.getAlamat_toko());

            btn_setUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.setMessage("Harap tunggu");
                    dialog.setCancelable(false);
                    dialog.show();

                    settingUpdate(edt_ip.getText().toString(),edt_invoice.getText().toString(),edt_namaToko.getText().toString(),edt_alamatToko.getText().toString());
                }
            });
        }
        else{
            btn_setUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.setMessage("Harap tunggu");
                    dialog.setCancelable(false);
                    dialog.show();

                    settingAwal(edt_ip.getText().toString(),edt_invoice.getText().toString(),edt_namaToko.getText().toString(),edt_alamatToko.getText().toString());
                }
            });
        }
    }

    private void settingUpdate(String ip, String invoice, String nama,String alamat){
        Pengaturan pengaturan = Pengaturan.findById(Pengaturan.class,(long) 1);
        pengaturan.setIp_address(ip);
        pengaturan.setInvoice(invoice);
        pengaturan.setNama_toko(nama);
        pengaturan.setAlamat_toko(alamat);
        pengaturan.save();

        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
        ((Activity)context).finish();

        dialog.dismiss();
    }

    private void settingAwal(String ip, String invoice, String nama,String alamat){
        Pengaturan pengaturan =  new Pengaturan(ip,invoice,nama,alamat);
        pengaturan.save();

        Toasty.success(context,"Data Berhasil Disimpan",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, Login.class);
        context.startActivity(intent);
        ((Activity)context).finish();

        dialog.dismiss();
    }
}
