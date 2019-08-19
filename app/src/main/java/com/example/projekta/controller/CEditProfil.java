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

import com.example.projekta.DashboardAdmin;
import com.example.projekta.DashboardKurir;
import com.example.projekta.DashboardUser;
import com.example.projekta.R;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.database.User;
import com.example.projekta.model.MResponse;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CEditProfil implements DatePickerDialog.OnDateSetListener {
    Context context;
    private Api_Rest apiRest;
    private MaterialEditText et_nama,et_alamat,et_noHp,et_tanggalLahir,et_username,et_password;
    private ImageButton btn_tanggal;
    private Button btn_update,btn_batal;
    private AlertDialog dialog, alertForm;
    User user = new User();
    private String id_user;

    public CEditProfil(Context context){
        this.context = context;
    }

    public void EditProfil(){
        apiRest = Common.getApi();
        user = User.findById(User.class, (long) 1);
        id_user = user.getId_user();
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.alert_edit_profil, null);
        et_nama = layout.findViewById(R.id.edt_namaEp);
        et_nama.setText(user.getNama());
        et_alamat = layout.findViewById(R.id.edt_alamatEp);
        et_alamat.setText(user.getAlamat());
        et_noHp = layout.findViewById(R.id.edt_nohpEp);
        et_noHp.setText(user.getNo_hp());
        et_tanggalLahir = layout.findViewById(R.id.edt_tanggalLahirEp);
        et_tanggalLahir.setText(user.getTanggal_lahir());
        et_username = layout.findViewById(R.id.edt_usernameEp);
        et_username.setText(user.getUsername());
        et_password = layout.findViewById(R.id.edt_passwordEp);
        et_password.setText(user.getPassword());
        btn_tanggal = layout.findViewById(R.id.btn_tanggalEp);
        btn_update = layout.findViewById(R.id.btn_updateEp);
        btn_batal = layout.findViewById(R.id.btn_batalEp);

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
                validasi();
                if (!validasi()){
                    Toasty.error(context,"Update profil gagal",Toast.LENGTH_SHORT).show();
                }else {
                    updateProfil();
                }
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
    private void updateProfil(){
        dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Sedang Update....");
        dialog.setCancelable(false);
        dialog.show();

        final String sid_user = id_user;
        final String snama = et_nama.getText().toString();
        final String salamat = et_alamat.getText().toString();
        final String snoHp = et_noHp.getText().toString();
        final String stanggal_lahir = et_tanggalLahir.getText().toString();
        final String susername = et_username.getText().toString();
        final String spassword = et_password.getText().toString();
        final String suser_level = user.getUser_level();

        apiRest.updateprofil(sid_user,snama,salamat,snoHp,stanggal_lahir,susername,spassword).enqueue(new Callback<MResponse>() {
            @Override
            public void onResponse(Call<MResponse> call, Response<MResponse> response) {
                Log.d("RETRO", "Response : " + response.body().toString());
                int kode = response.body().getKode();
                if (kode == 1) {
                    Toasty.success(context, "Profil berhasil di update", Toast.LENGTH_SHORT).show();
                    if (User.count(User.class,null,null)> 0){
                        User user = User.findById(User.class,(long)1);
                        user.setId_user(id_user);
                        user.setNama(snama);
                        user.setAlamat(salamat);
                        user.setNo_hp(snoHp);
                        user.setTanggal_lahir(stanggal_lahir);
                        user.setUsername(susername);
                        user.setPassword(spassword);
                        user.setUser_level(suser_level);
                        user.save();
                    }else {
                        User user = new User(sid_user,snama,salamat,snoHp,stanggal_lahir,susername,spassword,suser_level);
                        user.save();
                    }
                    alertForm.dismiss();
                    dialog.dismiss();
                    if (suser_level.equals("1")) {
                        Intent kurir = new Intent(context, DashboardKurir.class);
                        context.startActivity(kurir);
                        ((Activity)context).finish();
                    } else if (suser_level.equals("2")) {
                        Intent admin = new Intent(context, DashboardAdmin.class);
                        context.startActivity(admin);
                        ((Activity)context).finish();
                    } else {
                        Intent user = new Intent(context, DashboardUser.class);
                        context.startActivity(user);
                        ((Activity)context).finish();
                    }
                } else {
                    Toasty.error(context, "Gagal Update", Toast.LENGTH_SHORT).show();
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
