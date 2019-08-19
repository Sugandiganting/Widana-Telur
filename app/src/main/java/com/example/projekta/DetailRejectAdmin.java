package com.example.projekta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projekta.adapter.ADetailListPesananAdmin;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MDetailListPesanan;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRejectAdmin extends AppCompatActivity {

    private Api_Rest apiRest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ADetailListPesananAdmin detailListPesananAdmin;
    private String id_transaksi;
    private Button btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_reject_admin);

        btn_close = findViewById(R.id.btn_closeR);

        apiRest = Common.getApi();
        recyclerView = findViewById(R.id.rejectdetailorderanA_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        id_transaksi = getIntent().getStringExtra("id");

        detailPesanan();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void detailPesanan(){
        apiRest.detailpesanan(id_transaksi).enqueue(new Callback<List<MDetailListPesanan>>() {
            @Override
            public void onResponse(Call<List<MDetailListPesanan>> call, Response<List<MDetailListPesanan>> response) {
                if (response.body()==null){
                    Toasty.error(DetailRejectAdmin.this,"Detail Pesanan Kosong");
                }else {
                    List<MDetailListPesanan> mDetailListPesanans = response.body();
                    detailListPesananAdmin = new ADetailListPesananAdmin(DetailRejectAdmin.this,mDetailListPesanans);
                    recyclerView.setAdapter(detailListPesananAdmin);
                }
            }

            @Override
            public void onFailure(Call<List<MDetailListPesanan>> call, Throwable t) {
                Toasty.error(DetailRejectAdmin.this, "Gagal Terhubung!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
