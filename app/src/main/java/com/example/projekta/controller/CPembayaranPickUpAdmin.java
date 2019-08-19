package com.example.projekta.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.projekta.DaftarPembayaranAdmin;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MResponse;

import java.text.NumberFormat;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CPembayaranPickUpAdmin {
    Context context;
    private Api_Rest apiRest;
    private String idTransaksi,amount;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    SweetAlertDialog pDialog;

    public CPembayaranPickUpAdmin (Context context){
        this.context=context;
    }
    public void bayarPickUp(String id_transaksi,String total_transaksi){
        idTransaksi = id_transaksi;
        amount = total_transaksi;
        apiRest = Common.getApi();
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        pDialog.setTitle("Yakin Bayar ?");
        pDialog.setContentText("Total: "+formatRupiah.format(Double.valueOf(amount)));
        pDialog.setCancelable(false);
        pDialog.setCancelText("Batal");
        pDialog.setConfirmText("YES");

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                updatePembayaran();
            }
        });

        pDialog.show();
    }

    private void updatePembayaran() {
        apiRest.updatepembayaranpickup(idTransaksi,amount).enqueue(new Callback<MResponse>() {
            @Override
            public void onResponse(Call<MResponse> call, Response<MResponse> response) {
                Log.d("RETRO", "Response : " + response.body().toString());
                int kode = response.body().getKode();

                if (kode == 1) {
                    Toasty.success(context, ""+response.body().getPesan(), Toast.LENGTH_SHORT).show();
                    ((Activity)context).finish();
                    pDialog.dismiss();
                    Intent intent = new Intent(context, DaftarPembayaranAdmin.class);
                    context.startActivity(intent);
                } else if (kode == 2){
                    Toasty.error(context, ""+response.body().getPesan(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }else {
                    Toasty.error(context, ""+response.body().getPesan(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MResponse> call, Throwable t) {
                Log.d("RETRO", "Failure : " + "Gagal Mengirim Request");
                Toasty.error(context, "Gagal Mengirim Request", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        });
    }
}
