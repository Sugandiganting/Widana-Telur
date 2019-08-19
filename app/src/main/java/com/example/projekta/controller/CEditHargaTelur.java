package com.example.projekta.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projekta.R;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MResponse;
import com.example.projekta.model.MTelur;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CEditHargaTelur {
    Context context;
    private Api_Rest apiRest;
    private MaterialEditText edt_hargaTb, edt_hargaTs,edt_hargaBeliTb,edt_hargaBeliTs;
    private AlertDialog dialog, alertForm;
    private Button btn_update,btn_batal;
    private String hargaTb,hargaTs;

    public CEditHargaTelur(Context context){
        this.context = context;
    }

    public void editHargaTelur(){
        apiRest = Common.getApi();
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.alert_update_harga_telur, null);
        HargaTelur();

        edt_hargaTb = layout.findViewById(R.id.edt_hargaEditTB);
        edt_hargaTs = layout.findViewById(R.id.edt_hargaEditTS);
        edt_hargaBeliTb = layout.findViewById(R.id.edt_hargaEditTBb);
        edt_hargaBeliTs = layout.findViewById(R.id.edt_hargaEditTSb);
        btn_update = layout.findViewById(R.id.btn_UpdateHT);
        btn_batal = layout.findViewById(R.id.btn_batalHT);

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForm.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHargaTelur();
            }
        });

        alertBuilder.setView(layout);
        alertBuilder.setCancelable(true);
        alertForm = alertBuilder.create();
        alertForm.show();
    }

    private void HargaTelur() {
        apiRest.telur().enqueue(new Callback<List<MTelur>>() {
            @Override
            public void onResponse(Call<List<MTelur>> call, Response<List<MTelur>> response) {
                if (response.body()==null) {
                    Toasty.error(context,"Harga Telur Tidak didapatkan");
                }else {
                    List<MTelur> telurs = response.body();
                    for (MTelur telur : telurs) {
                        if (telur.getUkuran_telur().equals("Sedang")) {
                            hargaTs = telur.getHarga_jual();
                            edt_hargaTs.setText(telur.getHarga_jual());
                            edt_hargaBeliTs.setText(telur.getHarga_beli());
                        }else if (telur.getUkuran_telur().equals("Besar")){
                            hargaTb = telur.getHarga_jual();
                            edt_hargaTb.setText(telur.getHarga_jual());
                            edt_hargaBeliTb.setText(telur.getHarga_beli());
                        }else {
                            Toasty.info(context, "Harga telur tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<List<MTelur>> call, Throwable t) {
                Toasty.error(context,"Gagal Terhubung...",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validasi(){
        boolean valid = true;
        if (edt_hargaTs.length() == 0){
            edt_hargaTs.setError("Harga jual telur tidak boleh kosong");
            valid = false;
        }if (edt_hargaTb.length() ==0){
            edt_hargaTb.setError("Harga jual telur tidak boleh kosong");
            valid = false;
        }if (edt_hargaBeliTs.length()==0){
            edt_hargaBeliTs.setText("Harga beli telur tidak boleh kosong");
            valid = false;
        }if (edt_hargaBeliTb.length()==0) {
            edt_hargaBeliTb.setText("Harga beli telur tidak boleh kosong");
            valid = false;
        }
        return valid;
    }

    private void updateHargaTelur(){
        dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Sedang Update....");
        dialog.setCancelable(false);
        dialog.show();

        if (validasi()==true){
            String shargatb = edt_hargaTb.getText().toString();
            String shargabelitb = edt_hargaBeliTb.getText().toString();
            String shargats = edt_hargaTs.getText().toString();
            String shargabelits = edt_hargaBeliTs.getText().toString();
            apiRest.updatehargatelur(shargatb,shargats,shargabelits,shargabelitb).enqueue(new Callback<MResponse>() {
                @Override
                public void onResponse(Call<MResponse> call, Response<MResponse> response) {
                    Log.d("RETRO", "Response : " + response.body().toString());
                    int kode = response.body().getKode();
                    if (kode == 1) {
                        Toasty.success(context, "" + response.body().getPesan(), Toast.LENGTH_SHORT).show();
                        alertForm.dismiss();
                        dialog.dismiss();
                    }else {
                        Toasty.error(context, ""+response.body().getPesan(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<MResponse> call, Throwable t) {
                    Toasty.error(context, "Gagal Terhubung....", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }
}
