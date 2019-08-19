package com.example.projekta.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.projekta.DataUser;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MResponse;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CDeleteDataUser {
    Context context;
    private Api_Rest apiRest;
    private String customerId,userLevel;
    SweetAlertDialog pDialog;

    public CDeleteDataUser (Context context){
        this.context=context;
    }

    public void deleteUser(String customer_id, String user_level){
        customerId = customer_id;
        userLevel = user_level;
        apiRest = Common.getApi();
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
        pDialog.setTitle("Yakin Hapus User ?");
        pDialog.setCancelable(false);
        pDialog.setCancelText("Batal");
        pDialog.setConfirmText("YES");

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                deleteSelectedUser();
            }
        });

        pDialog.show();
    }

    private void deleteSelectedUser() {
        apiRest.deleteuser(customerId).enqueue(new Callback<MResponse>() {
            @Override
            public void onResponse(Call<MResponse> call, Response<MResponse> response) {
                Log.d("RETRO", "Response : " + response.body().toString());
                int kode = response.body().getKode();
                if (kode == 1) {
                    Toasty.success(context, "" + response.body().getPesan(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
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
                Toasty.warning(context,"Gagal Connect",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
