package com.example.projekta.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.DaftarKirimanKurir;
import com.example.projekta.R;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MResponse;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CPembayaranKurir implements DatePickerDialog.OnDateSetListener {

    Context context;
    private Api_Rest apiRest;
    private MaterialSpinner type_pemabayaran;
    private TextView tv_totalPembayaran,tv_tanggal;
    private MaterialEditText et_inputUang;
    private ImageButton btn_tanggal;
    private AlertDialog dialog, alertForm;
    private Button btn_ok,btn_batal;
    private String idTransaksi, totTransaksi;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public CPembayaranKurir (Context context){
        this.context=context;
    }

    public void PembayaranKurir(String id_transaksi, String total_transaksi){
        idTransaksi = id_transaksi;
        totTransaksi = total_transaksi;
        apiRest = Common.getApi();

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.alert_pembayaran_kurir, null);

        type_pemabayaran = layout.findViewById(R.id.cb_typePembayaran);
        type_pemabayaran.setItems("Cash","Credit");

        tv_totalPembayaran = layout.findViewById(R.id.tv_totalPembayaranKurir);
        tv_totalPembayaran.setText(""+formatRupiah.format(Double.valueOf(totTransaksi)));

        tv_tanggal = layout.findViewById(R.id.tv_tanggalPKurir);
        et_inputUang = layout.findViewById(R.id.tot_pembayaran_konsumen);
        btn_tanggal = layout.findViewById(R.id.btn_tanggalPKurir);
        btn_ok = layout.findViewById(R.id.btn_okKurir);
        btn_batal = layout.findViewById(R.id.btn_batalKurir);

        Toasty.info(context,"id ="+id_transaksi, Toast.LENGTH_SHORT).show();
        Toasty.info(context,"tot ="+total_transaksi, Toast.LENGTH_SHORT).show();

        //set Tanggal
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        //show tanggal
        btn_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

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

        alertBuilder.setView(layout);
        alertBuilder.setCancelable(true);
        alertForm = alertBuilder.create();
        alertForm.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "-" + (month + 1) + "-" + year;
        tv_tanggal.setText(date);
    }

    private void validasi(){
        if (type_pemabayaran.getText().equals("Credit")){
            if (tv_tanggal.length()>0 ){
                if (et_inputUang.getText().toString().equals(totTransaksi)){
                    type_pemabayaran.setError("Ganti Type Pembayaran");
                    Toasty.error(context,"Ganti type pembayaran menjadi cash",Toast.LENGTH_SHORT).show();
                }else {
                    insertPembayaran();
                }
            }else {
                tv_tanggal.setError("Tanggal tidak boleh kosong");
            }
        }else if (type_pemabayaran.getText().equals("Cash")){
            if (et_inputUang.getText().toString().equals(totTransaksi)){
                insertPembayaran();
            }else {
                et_inputUang.setError("Pembayaran Tidak Sesuai");
            }
        }else {
            Toasty.error(context,"Mohon lengkapi data",Toast.LENGTH_SHORT).show();
        }
    }

    private void insertPembayaran(){
        dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Sedang Insert....");
        dialog.setCancelable(false);
        dialog.show();

        String sid_transaksi = idTransaksi;
        String stype_pembayaran ;

        if (type_pemabayaran.getText().equals("Cash")){
            stype_pembayaran = "2";
        }else {
            stype_pembayaran ="1";
        }
        String spayment_term = tv_tanggal.getText().toString();
        String sinput_pembayaran = et_inputUang.getText().toString();
        apiRest.insertPayment(sid_transaksi,sinput_pembayaran,stype_pembayaran,spayment_term).enqueue(new Callback<MResponse>() {
            @Override
            public void onResponse(Call<MResponse> call, Response<MResponse> response) {
                Log.d("RETRO", "Response : " + response.body().toString());
                int kode = response.body().getKode();

                if (kode == 1) {
                    Toasty.success(context, ""+response.body().getPesan(), Toast.LENGTH_SHORT).show();
                    alertForm.dismiss();
                    ((Activity)context).finish();
                    dialog.dismiss();
                    Intent intent = new Intent(context, DaftarKirimanKurir.class);
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
