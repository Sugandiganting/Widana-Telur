package com.example.projekta;

import android.bluetooth.BluetoothAdapter;
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
import com.example.projekta.model.MDetailListPesanan;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailAcceptAdmin extends AppCompatActivity {

    private Api_Rest apiRest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ADetailListPesananAdmin detailListPesananAdmin;
    private String id_transaksi,biaya_jasa,total_transaksi,hargaTs,hargaTb,qtyTs,qtyTb,totHargaTS,totHargaTB,no_transaksi,nama_user,nama_kurir,tanggal_pemesanan;
    private Button btn_close,btn_print;
    private TextView tv_biayaJasa, tv_totalTransaksi;
    private String x;
    Locale localeID = new Locale("in", "ID");
    NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_accept_admin);

        btn_close = findViewById(R.id.btn_closeAcc);
        tv_biayaJasa = findViewById(R.id.tv_biayaJasaAA);
        tv_totalTransaksi = findViewById(R.id.tv_totalAA);

        apiRest = Common.getApi();
        recyclerView = findViewById(R.id.acceptdetailorderanA_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        id_transaksi = getIntent().getStringExtra("id");
        no_transaksi = getIntent().getStringExtra("no_transaksi");
        biaya_jasa = getIntent().getStringExtra("biaya_jasa");
        nama_user = getIntent().getStringExtra("nama_user");
        nama_kurir = getIntent().getStringExtra("nama_kurir");
        tanggal_pemesanan = getIntent().getStringExtra("tanggal_pemesanan");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = simpleDateFormat.parse(tanggal_pemesanan);

            SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
            x = dt.format(date);
//            Toast.makeText(this, ""+x, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_biayaJasa.setText(""+formatRupiah.format(Double.valueOf(biaya_jasa)));
        total_transaksi = getIntent().getStringExtra("total_transaksi");
        tv_totalTransaksi.setText(""+formatRupiah.format(Double.valueOf(total_transaksi)));
        btn_print = findViewById(R.id.btn_printNota);

        detailPesanan();

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!bluetoothAdapter.isEnabled()){
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent,1);
                }else {
                    Intent intent = new Intent(DetailAcceptAdmin.this,PrintNota.class);
                    intent.putExtra("id", id_transaksi);
                    intent.putExtra("biaya_jasa",biaya_jasa);
                    intent.putExtra("total_transaksi",total_transaksi);
                    intent.putExtra("hargaTs",hargaTs);
                    intent.putExtra("hargaTb",hargaTb);
                    intent.putExtra("qtyTs",qtyTs);
                    intent.putExtra("qtyTb",qtyTb);
                    intent.putExtra("totHargaTS",totHargaTS);
                    intent.putExtra("totHargaTB",totHargaTB);
                    intent.putExtra("no_transaksi",no_transaksi);
                    intent.putExtra("nama_user",nama_user);
                    intent.putExtra("nama_kurir",nama_kurir);
                    intent.putExtra("tanggal_pemesanan",x);
                    startActivity(intent);
                }
            }
        });
    }
    public void detailPesanan(){
        apiRest.detailpesanan(id_transaksi).enqueue(new Callback<List<MDetailListPesanan>>() {
            @Override
            public void onResponse(Call<List<MDetailListPesanan>> call, Response<List<MDetailListPesanan>> response) {
                if (response.body()==null){
                    Toasty.error(DetailAcceptAdmin.this,"Detail Pesanan Kosong");
                }else {
                    List<MDetailListPesanan> mDetailListPesanans = response.body();
                    detailListPesananAdmin = new ADetailListPesananAdmin(DetailAcceptAdmin.this,mDetailListPesanans);
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
                Toasty.error(DetailAcceptAdmin.this, "Gagal Terhubung!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
