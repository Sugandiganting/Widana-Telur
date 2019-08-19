package com.example.projekta.controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projekta.DashboardAdmin;
import com.example.projekta.DashboardKurir;
import com.example.projekta.DashboardUser;
import com.example.projekta.R;
import com.example.projekta.Setting;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.database.User;
import com.example.projekta.model.MLogin;
import com.rengwuxian.materialedittext.MaterialEditText;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CLogin {
    Context context;
    private MaterialEditText edt_username,edt_password;
    private Button btn_login,btn_resetting;
    private AlertDialog dialog;
    private Api_Rest apiRest;
    String username,password;

    public CLogin(Context context){
        this.context = context;
    }
    public void login(){
        edt_username = (((Activity)context).findViewById(R.id.edt_username));
        edt_password = (((Activity)context).findViewById(R.id.edt_password));
        btn_login = (((Activity)context).findViewById(R.id.btn_login));
        btn_resetting = (((Activity)context).findViewById(R.id.btn_resetting));

        btn_resetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetting();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username= edt_username.getText().toString();
                password= edt_password.getText().toString();

                getLogin(username,password);
            }
        });

    }

    private void resetting(){
        Intent resetting =new Intent(context, Setting.class);
        context.startActivity(resetting);
        ((Activity)context).finish();
    }

    private void getLogin(String username, String password){
        apiRest = Common.getApi();

        dialog = new SpotsDialog.Builder().setContext(context).build();
        dialog.setMessage("Sedang Login....");
        dialog.setCancelable(false);
        dialog.show();

        apiRest.login(username,password).enqueue(new Callback<MLogin>() {
            @Override
            public void onResponse(Call<MLogin> call, Response<MLogin> response) {
                dialog.dismiss();
                if (response.body()==null){
                    Toasty.error(context,"Alamat IP Salah",Toast.LENGTH_SHORT).show();
                }else {
                    MLogin mLogin = response.body();
                    if (mLogin.getKondisi().equals("Berhasil")) {

                        if (User.count(User.class,null,null)> 0){
                            User user = User.findById(User.class,(long)1);
                            user.setId_user(mLogin.getId());
                            user.setNama(mLogin.getNama());
                            user.setAlamat(mLogin.getAlamat());
                            user.setNo_hp(mLogin.getNo_hp());
                            user.setTanggal_lahir(mLogin.getTanggal_lahir());
                            user.setUsername(mLogin.getUsername());
                            user.setPassword(mLogin.getPassword());
                            user.setUser_level(mLogin.getUser_level());
                            user.save();
                        }else {
                            User user = new User(mLogin.getId(),mLogin.getNama(),mLogin.getAlamat(),mLogin.getNo_hp(),mLogin.getTanggal_lahir(),mLogin.getUsername(),mLogin.getPassword(),mLogin.getUser_level());
                            user.save();
                        }
                        if (mLogin.getUser_level().equals("1")) {
                            Intent kurir = new Intent(context, DashboardKurir.class);
                            context.startActivity(kurir);
                            ((Activity)context).finish();
                        } else if (mLogin.getUser_level().equals("2")) {
                            Intent admin = new Intent(context, DashboardAdmin.class);
                            context.startActivity(admin);
                            ((Activity)context).finish();
                        } else {
                            Intent user = new Intent(context, DashboardUser.class);
                            context.startActivity(user);
                            ((Activity)context).finish();
                        }

                        Toasty.success(context, "Login Berhasil", Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.info(context, "Username atau Password Salah", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MLogin> call, Throwable t) {
                dialog.dismiss();
                Toasty.error(context,"Gagal Terhubung",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
