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
import android.widget.Toast;

import com.example.projekta.DataUser;
import com.example.projekta.R;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MResponse;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CEditDataUser implements DatePickerDialog.OnDateSetListener {
    Context context;
    private Api_Rest apiRest;
    private MaterialSpinner cb_userLevel;
    private MaterialEditText et_nama,et_alamat,et_noHp,et_tanggalLahir,et_username,et_password;
    private ImageButton btn_tanggal;
    private Button btn_update,btn_batal;
    private AlertDialog dialog, alertForm;
    private String customerId,userLevel;

    public CEditDataUser(Context context){
        this.context = context;
    }

    public void editData(String customer_id, String nama, String alamat, String noHp, String tanggal_lahir, String username, String password,String user_level){
        apiRest = Common.getApi();
        customerId = customer_id;
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.alert_tambah_data_user, null);
        userLevel = user_level;
        et_nama = layout.findViewById(R.id.edt_namaDA);
        et_nama.setText(nama);
        et_alamat = layout.findViewById(R.id.edt_alamatDA);
        et_alamat.setText(alamat);
        et_noHp = layout.findViewById(R.id.edt_nohpDA);
        et_noHp.setText(noHp);
        et_tanggalLahir = layout.findViewById(R.id.edt_tanggalLahirDA);
        et_tanggalLahir.setText(tanggal_lahir);
        et_username = layout.findViewById(R.id.edt_usernameDA);
        et_username.setText(username);
        et_password = layout.findViewById(R.id.edt_passwordDA);
        et_password.setText(password);
        btn_tanggal = layout.findViewById(R.id.btn_tanggalDA);
        btn_update = layout.findViewById(R.id.btn_InsertDA);
        btn_batal = layout.findViewById(R.id.btn_batalDA);
        cb_userLevel = layout.findViewById(R.id.cb_userLevelDA);
        cb_userLevel.setItems("Konsumen","Kurir");
        if (user_level.equals("0")){
            cb_userLevel.setText("Konsumen");
        }else if (user_level.equals("1")){
            cb_userLevel.setText("Kurir");
        }

        btn_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTanggal();
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertForm.dismiss();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDataUser();
            }
        });


        alertBuilder.setView(layout);
        alertBuilder.setCancelable(true);
        alertForm = alertBuilder.create();
        alertForm.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "-" + (month+1) + "-" + year;
        et_tanggalLahir.setText(date);
    }
    private void setTanggal() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private boolean validasi(){
        boolean valid= true;
        if (cb_userLevel.getText().toString().equals("User Level")){
            cb_userLevel.setError("Harap pilih user level");
        }
        if (et_nama.length()==0){
            et_nama.setError("Harap input nama");
            valid =false;
        }
        if (et_alamat.length()==0){
            et_alamat.setError("Harap input alamat");
            valid =false;
        }
        if (et_noHp.length()==0){
            et_noHp.setError("Harap input no hp");
            valid =false;
        }
        if (et_tanggalLahir.length()==0){
            et_tanggalLahir.setError("Harap input tanggal lahir");
            valid =false;
        }
        if (et_username.length()==0){
            et_username.setError("Harap input username");
            valid =false;
        }
        if (et_password.length()==0){
            et_password.setError("Harap input password");
            valid =false;
        }
        return valid;
    }
    public void updateDataUser(){
        dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Sedang Update....");
        dialog.setCancelable(false);
        dialog.show();

        if (validasi()==true){
            String snama= et_nama.getText().toString();
            String salamat = et_alamat.getText().toString();
            String sno_hp = et_noHp.getText().toString();
            String stanggal_lahir = et_tanggalLahir.getText().toString();
            String susername = et_username.getText().toString();
            String spassword = et_password.getText().toString();
            if (cb_userLevel.getText().toString().equals("Konsumen")){
                userLevel="0";
            }else  if (cb_userLevel.getText().toString().equals("Kurir")){
                userLevel="1";
            }
            apiRest.updatedatauser(customerId,snama,salamat,sno_hp,stanggal_lahir,susername,spassword,userLevel).enqueue(new Callback<MResponse>() {
                @Override
                public void onResponse(Call<MResponse> call, Response<MResponse> response) {
                    Log.d("RETRO", "Response : " + response.body().toString());
                    int kode = response.body().getKode();
                    if (kode == 1) {
                        Toasty.success(context, "" + response.body().getPesan(), Toast.LENGTH_SHORT).show();
                        alertForm.dismiss();
                        dialog.dismiss();
                        ((Activity) context).finish();
                        Intent intent = new Intent(context, DataUser.class);
                        intent.putExtra("user_level", userLevel);
                        context.startActivity(intent);
                    }else {
                        Toasty.error(context, ""+response.body().getPesan(), Toast.LENGTH_SHORT).show();
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
}
