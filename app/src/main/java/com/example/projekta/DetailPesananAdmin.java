package com.example.projekta;

import android.content.Intent;
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
import com.example.projekta.controller.CConfirmAdmin;
import com.example.projekta.controller.CTolakAdmin;
import com.example.projekta.model.MDetailListPesanan;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPesananAdmin extends AppCompatActivity {
    private Api_Rest apiRest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ADetailListPesananAdmin detailListPesananAdmin;
    private String id_transaksi,biaya_jasa,total_transaksi,hargaTb,hargaTs,qtyTb,qtyTs,totHargaTB,totHargaTS,type_pengambilan;
    private TextView biayaJasa, totalTransaksi;
    private Button btn_terimaA,btn_tolakA,btn_closeA;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(DetailPesananAdmin.this,DaftarPesananAdmin.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pesanan_admin);

        biayaJasa = findViewById(R.id.tv_biayaJasaPeA);
        totalTransaksi = findViewById(R.id.tv_totalPeA);

        id_transaksi = getIntent().getStringExtra("id");
        biaya_jasa = getIntent().getStringExtra("biaya_jasa");
        total_transaksi = getIntent().getStringExtra("total_transaksi");
        type_pengambilan= getIntent().getStringExtra("type_pengambilan");

        biayaJasa.setText(""+formatRupiah.format(Double.valueOf(biaya_jasa)));
        totalTransaksi.setText(""+formatRupiah.format(Double.valueOf(total_transaksi)));

        btn_terimaA = findViewById(R.id.btn_terimaA);
        btn_tolakA = findViewById(R.id.btn_tolakA);
        btn_closeA = findViewById(R.id.btn_closeA);

        apiRest = Common.getApi();
        recyclerView = findViewById(R.id.listdetailorderanA_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DetailPesanan();

        btn_terimaA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CConfirmAdmin ckonfirm = new CConfirmAdmin(DetailPesananAdmin.this);
                ckonfirm.konfirm(id_transaksi,biaya_jasa,total_transaksi,hargaTb,hargaTs,qtyTb,qtyTs,totHargaTB,totHargaTS,type_pengambilan);
            }
        });
        btn_closeA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailPesananAdmin.this,DaftarPesananAdmin.class);
                startActivity(intent);
                finish();
            }
        });

        btn_tolakA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CTolakAdmin cTolakAdmin = new CTolakAdmin(DetailPesananAdmin.this);
                cTolakAdmin.batal(id_transaksi);
            }
        });
    }

    public void DetailPesanan(){
        apiRest.detailpesanan(id_transaksi).enqueue(new Callback<List<MDetailListPesanan>>() {
            @Override
            public void onResponse(Call<List<MDetailListPesanan>> call, Response<List<MDetailListPesanan>> response) {
                if (response.body()==null){
                    Toasty.error(DetailPesananAdmin.this,"Detail Pesanan Kosong");
                }else {
                    List<MDetailListPesanan> mDetailListPesanans = response.body();
                    detailListPesananAdmin = new ADetailListPesananAdmin(DetailPesananAdmin.this,mDetailListPesanans);
                    recyclerView.setAdapter(detailListPesananAdmin);
                    for (MDetailListPesanan detail : mDetailListPesanans){
                        if (detail.getUkuran_telur().equals("Sedang")){
                            hargaTs = detail.getHarga_jual();
                            qtyTs = (""+(Integer.valueOf(detail.getQty_order()))/30);
                            totHargaTS = detail.getTotal();
                        }else if (detail.getUkuran_telur().equals("Besar")){
                            hargaTb = detail.getHarga_jual();
                            qtyTb = (""+(Integer.valueOf(detail.getQty_order()))/30);
                            totHargaTB = detail.getTotal();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MDetailListPesanan>> call, Throwable t) {
                Toasty.error(DetailPesananAdmin.this, "Gagal Terhubung!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
