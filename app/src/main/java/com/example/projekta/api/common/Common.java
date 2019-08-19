package com.example.projekta.api.common;

import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.RetrofitClient;
import com.example.projekta.database.Pengaturan;

public class Common {
    static Pengaturan pengaturan;

    public static String BASE_URL;

    public static Api_Rest getApi(){
        try {
            pengaturan = Pengaturan.findById(Pengaturan.class, (long)1);
            BASE_URL ="http://"+pengaturan.getIp_address()+"/backend_TA/";
        }catch (Exception e){
            e.printStackTrace();
        }
        return RetrofitClient.getClient(BASE_URL).create(Api_Rest.class);
    }
}
