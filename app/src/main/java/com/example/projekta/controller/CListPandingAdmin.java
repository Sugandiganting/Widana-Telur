package com.example.projekta.controller;

import android.content.Context;

import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MPandingAdmin;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CListPandingAdmin {
    Context context;
    Api_Rest apiRest;


    public CListPandingAdmin(Context context){
        this.context=context;
    }

    public List<MPandingAdmin> pandingAdminList (){
        apiRest = Common.getApi();
        final List<MPandingAdmin> mPandingAdmins = null;
        apiRest.listpanding().enqueue(new Callback<List<MPandingAdmin>>() {
            @Override
            public void onResponse(Call<List<MPandingAdmin>> call, Response<List<MPandingAdmin>> response) {
                if (response.body()!=null){

                }
            }

            @Override
            public void onFailure(Call<List<MPandingAdmin>> call, Throwable t) {

            }
        });
        return mPandingAdmins;
    }
}
