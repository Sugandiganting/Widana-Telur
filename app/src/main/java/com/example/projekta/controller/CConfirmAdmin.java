package com.example.projekta.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.DaftarPesananAdmin;
import com.example.projekta.DetailPesananAdmin;
import com.example.projekta.R;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.database.User;
import com.example.projekta.model.MListUser;
import com.example.projekta.model.MResponse;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CConfirmAdmin {
    Context context;
    private Api_Rest apiRest;
    private MaterialEditText edt_hargaTB_A, edt_hargaTS_A, edt_qtyTB_A, edt_qtyTS_A;
    private MaterialSpinner lstKurir;
    private TextView tv_totTb, tv_totTs, tv_biayaJasaConfirmA, tv_totalTransaksiA;
    private Button btn_konfirmA, btn_batalA;
    private AlertDialog alertForm,dialog;
    private List<String> namaKurir = new ArrayList<>();
    private List<String> idKurir = new ArrayList<>();
    private String idTransaksi,biayaJasaA;
    private int pengambilan;
    private int tot_transaksi;

    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    User user = new User();

    public CConfirmAdmin(Context context) {
        this.context = context;
    }

    public void konfirm(String id_transaksi, String biaya_jasa, String total_transaksi, String hargaTb, String hargaTs, String qtyTb, String qtyTs, String totHargaTB, String totHargaTS, String type_pengambilan) {

        apiRest = Common.getApi();
        pengambilan = Integer.valueOf(type_pengambilan);
        tot_transaksi =Integer.valueOf(total_transaksi) ;
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.alert_confirm_pesanan_admin, null);
        edt_hargaTB_A = layout.findViewById(R.id.edt_hargaTB_A);
        edt_qtyTB_A = layout.findViewById(R.id.edt_qtyTB_A);
        tv_totTb = layout.findViewById(R.id.tv_totTb);

        edt_hargaTS_A = layout.findViewById(R.id.edt_hargaTS_A);
        edt_qtyTS_A = layout.findViewById(R.id.edt_qtyTS_A);
        tv_totTs = layout.findViewById(R.id.tv_totTs);
        lstKurir = layout.findViewById(R.id.cb_namaKurir);

        idTransaksi = id_transaksi;
        if (pengambilan == 2){
            lstKurir.setEnabled(false);
        }
        if (qtyTb != null){
            edt_hargaTB_A.setText(hargaTb);
            edt_hargaTB_A.addTextChangedListener(tot_tb);
            edt_qtyTB_A.setText(qtyTb);
            edt_qtyTB_A.addTextChangedListener(tot_tb);
            tv_totTb.setText("" + formatRupiah.format(Double.valueOf(totHargaTB)));
            tv_totTb.addTextChangedListener(totalBayar);
        }if (qtyTs != null){
            edt_hargaTS_A.setText(hargaTs);
            edt_hargaTS_A.addTextChangedListener(tot_ts);
            edt_qtyTS_A.setText(qtyTs);
            edt_qtyTS_A.addTextChangedListener(tot_ts);
            tv_totTs.setText("" + formatRupiah.format(Double.valueOf(totHargaTS)));
            tv_totTs.addTextChangedListener(totalBayar);
        }

        tv_biayaJasaConfirmA = layout.findViewById(R.id.tv_biayaJasaConfirmA);
        tv_biayaJasaConfirmA.setText(""+formatRupiah.format(Double.valueOf(biaya_jasa)));
        biayaJasaA = biaya_jasa;

        tv_totalTransaksiA = layout.findViewById(R.id.tv_totalTransaksiA);
        tv_totalTransaksiA.setText(""+formatRupiah.format(Double.valueOf(total_transaksi)));

        btn_konfirmA = layout.findViewById(R.id.btn_konfirmA);
        btn_batalA = layout.findViewById(R.id.btn_batalA);

        alertBuilder.setView(layout);
        alertBuilder.setCancelable(true);
        alertForm = alertBuilder.create();
        alertForm.show();

        btn_konfirmA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validasi();
            }
        });

        btn_batalA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForm.dismiss();
            }
        });

        //Set nama kurir
        apiRest.listuser().enqueue(new Callback<List<MListUser>>() {
            @Override
            public void onResponse(Call<List<MListUser>> call, Response<List<MListUser>> response) {
                List<MListUser> listUsers = response.body();
                for (MListUser listUser : listUsers) {
                    if (listUser.getUser_level().equals("1")) {
                        namaKurir.add(listUser.getNama());
                        idKurir.add(listUser.getId_user());
                    }
                }
                lstKurir.setItems(namaKurir);
            }

            @Override
            public void onFailure(Call<List<MListUser>> call, Throwable t) {
                Toasty.error(context, "Gagal menampilkan daftar kurir", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Telur Sedang set harga
    private TextWatcher tot_ts = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tv_totTs.setText("" + formatRupiah.format(Double.valueOf(tot_telurSedang())));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    //Total telur besar
    private TextWatcher tot_tb = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tv_totTb.setText("" + formatRupiah.format(Double.valueOf(tot_telurBesar())));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    // Total Bayar
    private TextWatcher totalBayar = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tv_totalTransaksiA.setText(""+formatRupiah.format(Double.valueOf(tot_pembayaran())));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    //validasi
    private boolean validasi(){
        boolean valid = true;
       if (lstKurir.getText().equals("Pilih Kurir")&& pengambilan ==1){
            lstKurir.setError("Pilih Kurir yang mengantar");
            Toasty.error(context,"Pilih kurir yang mengantar",Toast.LENGTH_SHORT).show();
            valid = false;
        }else {
            updatePesanan();
        }
        return valid;
    }

    private int tot_telurBesar() {
        int total = 0;
        if (edt_hargaTB_A.length()>0) {
            int harga = Integer.valueOf(edt_hargaTB_A.getText().toString());
            if (edt_qtyTB_A.length()>0){
                int qty = Integer.valueOf(edt_qtyTB_A.getText().toString());
                total = (qty * 30) * harga;
            }
        } else {
            total=0;
        }
        return total;
    }
    private int tot_telurSedang(){
        int total = 0;
        if (edt_hargaTS_A.length()>0) {
            int harga = Integer.valueOf(edt_hargaTS_A.getText().toString());
            if (edt_qtyTS_A.length()>0){
                int qty = Integer.valueOf(edt_qtyTS_A.getText().toString());
                total = (qty * 30) * harga;
            }
        } else {
            total=0;
        }
        return total;
    }

    //total pembayaran
    private int tot_pembayaran(){
        int total = 0;
        int biayaJasa =0;
        if (tv_biayaJasaConfirmA.length()>0){
            biayaJasa = Integer.valueOf(biayaJasaA);
        }
        if (tv_totTs.length()>0){
            if (tv_totTb.length()>0){
                total = biayaJasa + tot_telurSedang() + tot_telurBesar();
            }else {
                total = biayaJasa + tot_telurSedang();
            }
        }else if (tv_totTb.length()>0){
            if (tv_totTs.length()>0){
                total = biayaJasa + tot_telurSedang() + tot_telurBesar();
            }else {
                total = biayaJasa + tot_telurBesar();
            }
        }else {
            total = biayaJasa;
        }
        return total;
    }
    //Update ke database
    private void updatePesanan(){
        dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Sedang Insert....");
        dialog.setCancelable(false);
        dialog.show();
        String sid_kurir;
        user = User.findById(User.class, (long) 1);
        if (pengambilan==1){
            sid_kurir = idKurir.get(lstKurir.getSelectedIndex());
        }else {
            sid_kurir = "";
        }
        String sid_admin = user.getId_user();
        String sid_transaksi = idTransaksi;
        String sharga_ts=edt_hargaTS_A.getText().toString();
        String stotal_ts=String.valueOf(tot_telurSedang());
        String sharga_tb=edt_hargaTB_A.getText().toString();
        String stotal_tb=String.valueOf(tot_telurBesar());
        String sqty_ts=edt_qtyTS_A.getText().toString();
        String sqty_tb=edt_qtyTB_A.getText().toString();
        String sbiaya_jasa = biayaJasaA;

        apiRest.updatedataconfirm(sid_transaksi,sid_kurir,sqty_tb,sqty_ts,sharga_tb,sharga_ts,stotal_tb,stotal_ts,sbiaya_jasa,sid_admin)
                .enqueue(new Callback<MResponse>() {
                    @Override
                    public void onResponse(Call<MResponse> call, Response<MResponse> response) {
                        Log.d("RETRO", "Response : " + response.body().toString());
                        int kode = response.body().getKode();

                        if (kode == 1) {
                            Toasty.success(context, "Pesanan Berhasil Di Terima", Toast.LENGTH_SHORT).show();
                            alertForm.dismiss();
                            dialog.dismiss();
                            Intent intent = new Intent(context, DaftarPesananAdmin.class);
                            context.startActivity(intent);
                            ((Activity)context).finish();
                        } else {
                            Toasty.error(context, "Gagal Diterima", Toast.LENGTH_SHORT).show();
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
