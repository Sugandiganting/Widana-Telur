package com.example.projekta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.adapter.ADetailListPesananAdmin;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MDetailListPesanan;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTransaksiKurir extends AppCompatActivity {
    private Api_Rest apiRest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ADetailListPesananAdmin detailListPesananAdmin;
    private String id_transaksi,biaya_jasa,total_transaksi;
    private Button btn_close;
    private TextView tv_biayaJasa, tv_totalTransaksi;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_transaksi_kurir);

        id_transaksi = getIntent().getStringExtra("id");
        biaya_jasa = getIntent().getStringExtra("biaya_jasa");
        total_transaksi = getIntent().getStringExtra("total_transaksi");

        btn_close = findViewById(R.id.btn_closeKurir);
        tv_biayaJasa = findViewById(R.id.tv_biayaJasaKurir);
        tv_biayaJasa.setText(""+formatRupiah.format(Double.valueOf(biaya_jasa)));
        tv_totalTransaksi = findViewById(R.id.tv_totalKurir);
        tv_totalTransaksi.setText(""+formatRupiah.format(Double.valueOf(total_transaksi)));

        apiRest = Common.getApi();
        recyclerView = findViewById(R.id.listdetailkurir_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DetailPesanan();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void DetailPesanan() {
        apiRest.detailpesanan(id_transaksi).enqueue(new Callback<List<MDetailListPesanan>>() {
            @Override
            public void onResponse(Call<List<MDetailListPesanan>> call, Response<List<MDetailListPesanan>> response) {
                if (response.body()==null){
                    Toasty.error(DetailTransaksiKurir.this,"Detail Pesanan Kosong");
                }else {
                    List<MDetailListPesanan> mDetailListPesanans = response.body();
                    detailListPesananAdmin = new ADetailListPesananAdmin(DetailTransaksiKurir.this,mDetailListPesanans);
                    recyclerView.setAdapter(detailListPesananAdmin);
                }
            }

            @Override
            public void onFailure(Call<List<MDetailListPesanan>> call, Throwable t) {
                Toasty.error(DetailTransaksiKurir.this, "Gagal Terhubung!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
