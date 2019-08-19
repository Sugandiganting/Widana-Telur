package com.example.projekta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.projekta.adapter.AListPesanan;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.database.User;
import com.example.projekta.model.MListPesanan;

import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListOrderanUser extends AppCompatActivity {
    private Api_Rest apiRest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private AListPesanan aListPesanan;
    private String customer_id;
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orderan_user);

        apiRest= Common.getApi();
        recyclerView = findViewById(R.id.listorderan_rv);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        user = user.findById(User.class,(long)1);
        customer_id=user.getId_user();

        apiRest.listPesananUser(customer_id).enqueue(new Callback<List<MListPesanan>>() {
            @Override
            public void onResponse(Call<List<MListPesanan>> call, Response<List<MListPesanan>> response) {
                List<MListPesanan> mListPesanans = response.body();
                aListPesanan = new AListPesanan(ListOrderanUser.this, mListPesanans);
                recyclerView.setAdapter(aListPesanan);
            }

            @Override
            public void onFailure(Call<List<MListPesanan>> call, Throwable t) {
                Toasty.error(ListOrderanUser.this, "Gagal Terhubung!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
