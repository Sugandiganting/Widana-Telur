package com.example.projekta.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.DaftarPesananAdmin;
import com.example.projekta.R;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.database.User;
import com.example.projekta.model.MResponse;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CTolakAdmin {
    Context context;
    private Api_Rest apiRest;
    private EditText tv_batal;
    private Button btn_ok, btn_batal;
    private AlertDialog alertForm,dialog;
    private String idTransaksi;
    User user = new User();

    public CTolakAdmin (Context context){
        this.context=context;
    }

    public void batal(String id_transaksi){
        apiRest = Common.getApi();
        idTransaksi = id_transaksi;
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.alert_tolak_pesanan_admin, null);

        tv_batal = layout.findViewById(R.id.tv_tolakAdmin);
        btn_ok = layout.findViewById(R.id.btn_tolakOk);
        btn_batal = layout.findViewById(R.id.btn_cancelAdmin);

        alertBuilder.setView(layout);
        alertBuilder.setCancelable(true);
        alertForm = alertBuilder.create();
        alertForm.show();

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
    }
    //insert database
    public void insertDatabase(){
        dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Sedang Insert....");
        dialog.setCancelable(false);
        dialog.show();

        user = User.findById(User.class, (long) 1);
        String salasan = tv_batal.getText().toString();
        String sid_transaksi = idTransaksi;
        String sadmin_id= user.getId_user();
        apiRest.updatebatalpesanan(sid_transaksi,salasan,sadmin_id).enqueue(new Callback<MResponse>() {
            @Override
            public void onResponse(Call<MResponse> call, Response<MResponse> response) {
                Log.d("RETRO", "Response : " + response.body().toString());
                int kode = response.body().getKode();

                if (kode == 1) {
                    Toasty.success(context, "Pesanan berhasil di batalkan", Toast.LENGTH_SHORT).show();
                    alertForm.dismiss();
                    dialog.dismiss();
                    Intent intent = new Intent(context, DaftarPesananAdmin.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                } else {
                    Toasty.error(context, "Gagal Dibatalkan", Toast.LENGTH_SHORT).show();
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

    //validasi
    private boolean validasi(){
        boolean valid = true;
            if (tv_batal.length()==0){
                tv_batal.setError("Berikan Alasan Batal");
                Toasty.error(context,"Masukan Alasan Pembatalan Pesanan",Toast.LENGTH_SHORT).show();
                valid = false;
            }else {
                insertDatabase();
            }
        return valid;
    }
}
