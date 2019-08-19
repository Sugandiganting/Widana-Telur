package com.example.projekta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.adapter.ADataUser;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.controller.CTambahData;
import com.example.projekta.model.MDataUser;
import com.example.projekta.model.MListUser;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataUser extends AppCompatActivity {
    private Api_Rest apiRest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    //Setting Adapter
    private ADataUser aPandingAdmin;
    private MaterialSpinner lstCustomer;
    private Button btn_search, btn_refresh,btn_tambahData;
    List<String> namaCustomer = new ArrayList<>();
    List<String> lstIdUser = new ArrayList<>();
    private String user_level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_user);

        apiRest = Common.getApi();
        user_level = getIntent().getStringExtra("user_level");
        recyclerView = findViewById(R.id.daftarUserAdmin_rv);
        lstCustomer = findViewById(R.id.cb_namaUserDA);
        btn_search = findViewById(R.id.btn_cariDA);
        btn_refresh = findViewById(R.id.btn_refreshDA);
        btn_tambahData = findViewById(R.id.btn_tambahDataUser);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //set nama user yang terdaftar
        apiRest.listuser().enqueue(new Callback<List<MListUser>>() {
            @Override
            public void onResponse(Call<List<MListUser>> call, Response<List<MListUser>> response) {
                List<MListUser> listUsers = response.body();
                for (MListUser listUser : listUsers) {
                    if (listUser.getUser_level().equals("0")) {
                        namaCustomer.add(listUser.getNama());
                        lstIdUser.add(listUser.getId_user());
                    }
                }
                lstCustomer.setItems(namaCustomer);
            }

            @Override
            public void onFailure(Call<List<MListUser>> call, Throwable t) {
                Toasty.error(DataUser.this, "Gagal menampilkan daftar user", Toast.LENGTH_SHORT).show();
            }
        });

        loadDataUser();

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDataUser();
                lstCustomer.setText("");
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });


        btn_tambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CTambahData cTambahData = new CTambahData(DataUser.this);
                cTambahData.tambahData(user_level);
            }
        });

    }

    private void loadDataUser() {
        apiRest.listdatauser(user_level).enqueue(new Callback<List<MDataUser>>() {
            @Override
            public void onResponse(Call<List<MDataUser>> call, Response<List<MDataUser>> response) {
                if (response.body()!=null){
                    List<MDataUser> mPandingAdmins = response.body();
                    aPandingAdmin = new ADataUser(DataUser.this, mPandingAdmins);
                    recyclerView.setAdapter(aPandingAdmin);
                }else {
                    Toasty.info(DataUser.this,"Tidak ada data",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MDataUser>> call, Throwable t) {
                Toasty.info(DataUser.this,"Tidak ada data",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean validasi(){
        boolean valid = true;
        if (lstCustomer.getText().toString().equals("")){
            Toasty.error(DataUser.this,"Pilih nama terlebih dahulu",Toast.LENGTH_SHORT).show();
            valid = false;
        }
        return valid;
    }

    private void search(){
        if (validasi()==true){
            String scustomer_id = lstIdUser.get(lstCustomer.getSelectedIndex());
            apiRest.searchdatauser(user_level,scustomer_id).enqueue(new Callback<List<MDataUser>>() {
                @Override
                public void onResponse(Call<List<MDataUser>> call, Response<List<MDataUser>> response) {
                    if (response.body()!=null){
                        List<MDataUser> mPandingAdmins = response.body();
                        aPandingAdmin = new ADataUser(DataUser.this, mPandingAdmins);
                        recyclerView.setAdapter(aPandingAdmin);
                    }else {
                        Toasty.info(DataUser.this,"Data tidak ditemukan",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<MDataUser>> call, Throwable t) {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(DataUser.this, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitle("Data Tidak ditemukan");
                    pDialog.setCancelable(false);
                    pDialog.setConfirmText("YES");

                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.dismiss();
                            loadDataUser();
                        }
                    });
                    pDialog.show();
                }
            });
        }
    }
}
