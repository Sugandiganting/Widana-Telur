package com.example.projekta.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.DaftarPembayaranKurir;
import com.example.projekta.R;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MResponse;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CPenagihanCreditKurir {
    Context context;
    private Api_Rest apiRest;
    private TextView tv_batas_pembayaran, tv_total_pembayaran,tv_dp,tv_sisa;
    private MaterialEditText et_jumlahUang;
    private AlertDialog dialog, alertForm;
    private Button btn_ok,btn_batal;
    private String idTransaksi,sisaPembayaran;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public CPenagihanCreditKurir(Context context){
        this.context=context;
    }

    public void penagihanCreditKurir (String id_transaksi, String batas_pembayaran, String total_pembayaran, String dp, String sisa){
        idTransaksi = id_transaksi;
        sisaPembayaran = sisa;
        apiRest = Common.getApi();

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.alert_penagihan_kredit_kurir, null);

        tv_batas_pembayaran = layout.findViewById(R.id.tv_batasPembayaranPCrK);
        tv_batas_pembayaran.setText(""+batas_pembayaran);

        tv_total_pembayaran = layout.findViewById(R.id.tv_totalPembayaranPCrK);
        tv_total_pembayaran.setText(""+formatRupiah.format(Double.valueOf(total_pembayaran)));

        tv_dp = layout.findViewById(R.id.tv_dpPCrK);
        tv_dp.setText(""+formatRupiah.format(Double.valueOf(dp)));

        tv_sisa = layout.findViewById(R.id.tv_sisaPCrK);
        tv_sisa.setText(""+formatRupiah.format(Double.valueOf(sisa)));

        btn_ok = layout.findViewById(R.id.btn_okPCrK);
        btn_batal = layout.findViewById(R.id.btn_batalPCrK);

        et_jumlahUang = layout.findViewById(R.id.et_sisaPembayaranPCrK);

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               alertForm.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasi();
            }
        });

        Toasty.info(context,"sisa "+sisa,Toast.LENGTH_SHORT).show();

        alertBuilder.setView(layout);
        alertBuilder.setCancelable(true);
        alertForm = alertBuilder.create();
        alertForm.show();

    }

    private void validasi(){
        int sisaP, input;
        sisaP = Integer.valueOf(sisaPembayaran);
        input = Integer.valueOf(et_jumlahUang.getText().toString());
        if (sisaP == input){
            insertPembayaranCredit();
        }else {
            et_jumlahUang.setError("Pembayaran Tidak Sesuai");
        }
    }

    private void insertPembayaranCredit() {
        dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Sedang Insert....");
        dialog.setCancelable(false);
        dialog.show();

        String stotalPembayaran = et_jumlahUang.getText().toString();

        apiRest.insertpaymentcredit(idTransaksi,stotalPembayaran).enqueue(new Callback<MResponse>() {
            @Override
            public void onResponse(Call<MResponse> call, Response<MResponse> response) {
                Log.d("RETRO", "Response : " + response.body().toString());
                int kode = response.body().getKode();

                if (kode == 1) {
                    Toasty.success(context, ""+response.body().getPesan(), Toast.LENGTH_SHORT).show();
                    alertForm.dismiss();
                    ((Activity)context).finish();
                    dialog.dismiss();
                    Intent intent = new Intent(context, DaftarPembayaranKurir.class);
                    context.startActivity(intent);
                } else if (kode == 2){
                    Toasty.error(context, ""+response.body().getPesan(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else {
                    Toasty.error(context, ""+response.body().getPesan(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<MResponse> call, Throwable t) {
                dialog.hide();
                Log.d("RETRO", "Failure : " + "Gagal Mengirim Request");
                dialog.dismiss();
            }
        });
    }
}
