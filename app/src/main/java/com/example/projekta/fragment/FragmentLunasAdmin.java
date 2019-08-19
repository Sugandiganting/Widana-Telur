package com.example.projekta.fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projekta.R;
import com.example.projekta.adapter.ASuksesAdmin;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.model.MListUser;
import com.example.projekta.model.MPandingAdmin;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@SuppressLint("ValidFragment")
public class FragmentLunasAdmin extends Fragment implements DatePickerDialog.OnDateSetListener {
    View v;
    private Api_Rest apiRest;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MaterialSpinner lstCustomer;
    private ASuksesAdmin aPandingAdmin;
    private Button btn_search, btn_refresh;
    private TextView tv_tanggal;
    List<String> namaCustomer = new ArrayList<>();
    List<String> lstIdUser = new ArrayList<>();
    private ImageButton btn_tanggal;
    private Context context;

    public FragmentLunasAdmin(Context context){
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.pembayaran_sukses_admin,container,false);
        apiRest = Common.getApi();
        recyclerView = v.findViewById(R.id.suksesAdmin_rv);
        lstCustomer = v.findViewById(R.id.cb_namaUserSA);
        btn_tanggal = v.findViewById(R.id.btn_tanggalPengirimanSA);
        btn_search = v.findViewById(R.id.btn_cariSA);
        btn_refresh = v.findViewById(R.id.btn_refreshSA);
        tv_tanggal = v.findViewById(R.id.tv_tanggalPengirimanSA);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //set Tanggal
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        btn_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        loadListSuksesAdmin();

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListSuksesAdmin();
                lstCustomer.setText("");
                tv_tanggal.setText("");
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });

        //set nama user yang terdaftar
        apiRest.listuser().enqueue(new Callback<List<MListUser>>() {
            @Override
            public void onResponse(Call<List<MListUser>> call, Response<List<MListUser>> response) {
                List<MListUser> listUsers = response.body();
                for (MListUser listUser : listUsers) {
                    if (listUser.getUser_level().equals("0")) {
                        namaCustomer.add(listUser.getUsername());
                        lstIdUser.add(listUser.getId_user());
                    }
                }
                lstCustomer.setItems(namaCustomer);
            }

            @Override
            public void onFailure(Call<List<MListUser>> call, Throwable t) {
                Toasty.error(v.getContext(), "Gagal menampilkan daftar user", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void search() {
        if (validasi()==true){
            String scustomer_id;
            String stanggal;
            if (lstCustomer.getText().equals("") || lstCustomer.getText().equals("Search by name")) {
                scustomer_id = "";
            } else {
                scustomer_id = lstIdUser.get(lstCustomer.getSelectedIndex());
            }
            if (tv_tanggal.getText().equals("")) {
                stanggal = "";
            } else {
                stanggal = tv_tanggal.getText().toString();
            }
            apiRest.searchsuksesadmin(scustomer_id,stanggal).enqueue(new Callback<List<MPandingAdmin>>() {
                @Override
                public void onResponse(Call<List<MPandingAdmin>> call, Response<List<MPandingAdmin>> response) {
                    if (response.body()!=null){
                        List<MPandingAdmin> mPandingAdmins = response.body();
                        aPandingAdmin = new ASuksesAdmin(v.getContext(), mPandingAdmins);
                        aPandingAdmin.notifyDataSetChanged();
                        recyclerView.setAdapter(aPandingAdmin);
                    }else {
                        Toasty.info(context,"Data tidak ditemukan",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<MPandingAdmin>> call, Throwable t) {
                    final SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE);
                    pDialog.setTitle("Data Tidak ditemukan");
                    pDialog.setCancelable(false);
                    pDialog.setConfirmText("YES");

                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            pDialog.dismiss();
                            loadListSuksesAdmin();
                        }
                    });
                    pDialog.show();
                }
            });
        }
    }

    private void loadListSuksesAdmin() {
        apiRest.listsuksesadmin().enqueue(new Callback<List<MPandingAdmin>>() {
            @Override
            public void onResponse(Call<List<MPandingAdmin>> call, Response<List<MPandingAdmin>> response) {
                if (response.body()!=null){
                    List<MPandingAdmin> mPandingAdmins = response.body();
                    aPandingAdmin = new ASuksesAdmin(v.getContext(), mPandingAdmins);
                    aPandingAdmin.notifyDataSetChanged();
                    recyclerView.setAdapter(aPandingAdmin);
                }else {
                    Toasty.info(context,"Tidak Ada Transaksi",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MPandingAdmin>> call, Throwable t) {
                Toasty.error(v.getContext(), "Tidak Ada Transaksi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "-" + (month + 1) + "-" + year;
        tv_tanggal.setText(date);
    }
    private boolean validasi(){
        boolean valid = true;
        if (tv_tanggal.getText().equals("") && lstCustomer .getText().equals("Search by name")){
            valid = false;
            Toasty.error(context,"Harap isi salah satu menu pencarian",Toast.LENGTH_SHORT).show();
        }if (tv_tanggal.getText().equals("") && lstCustomer .getText().equals("")){
            valid = false;
            Toasty.error(context,"Harap isi salah satu menu pencarian",Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
}
