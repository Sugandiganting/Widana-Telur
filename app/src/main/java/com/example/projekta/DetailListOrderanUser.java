package com.example.projekta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.projekta.adapter.ADetailListPesanan;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MDetailListPesanan;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailListOrderanUser extends AppCompatActivity {
    private Api_Rest apiRest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ADetailListPesanan aDetailListPesanan;
    private String id_transaksi;
    Button btn_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_list_orderan_user);

        id_transaksi = getIntent().getStringExtra("id");
        btn_close = findViewById(R.id.btn_close);

        apiRest = Common.getApi();
        recyclerView = findViewById(R.id.listdetailorderan_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        apiRest.detailpesanan(id_transaksi).enqueue(new Callback<List<MDetailListPesanan>>() {
            @Override
            public void onResponse(Call<List<MDetailListPesanan>> call, Response<List<MDetailListPesanan>> response) {
                List<MDetailListPesanan> mDetailListPesanans = response.body();
                aDetailListPesanan = new ADetailListPesanan(DetailListOrderanUser.this,mDetailListPesanans);
                recyclerView.setAdapter(aDetailListPesanan);
            }

            @Override
            public void onFailure(Call<List<MDetailListPesanan>> call, Throwable t) {
                Toasty.error(DetailListOrderanUser.this, "Gagal Terhubung!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
