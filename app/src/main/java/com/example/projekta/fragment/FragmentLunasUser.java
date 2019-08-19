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
import com.example.projekta.adapter.ACreditUser;
import com.example.projekta.adapter.ALunasUser;
import com.example.projekta.api.Api_Rest;
import com.example.projekta.api.common.Common;
import com.example.projekta.database.User;
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
public class FragmentLunasUser extends Fragment implements DatePickerDialog.OnDateSetListener {
    View v;
    private Api_Rest apiRest;
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ALunasUser aPandingAdmin;
    private Button btn_search, btn_refresh;
    private TextView tv_tanggal;
    List<String> namaCustomer = new ArrayList<>();
    List<String> lstIdUser = new ArrayList<>();
    private MaterialSpinner lstCustomer;
    private ImageButton btn_tanggal;
    private String id_user,status="4";
    User user = new User();

    public FragmentLunasUser(Context context){
        this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.lunas_user_fragment, container, false);

        apiRest = Common.getApi();
        user = User.findById(User.class, (long) 1);
        id_user = user.getId_user();
        recyclerView = v.findViewById(R.id.lunasUser_rv);
        lstCustomer = v.findViewById(R.id.cb_namaUserUL);
        btn_tanggal = v.findViewById(R.id.btn_tanggalPengirimanUL);
        btn_search = v.findViewById(R.id.btn_cariUL);
        btn_refresh = v.findViewById(R.id.btn_refreshUL);
        tv_tanggal = v.findViewById(R.id.tv_tanggalPengirimanUL);
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

        loadListLunasUser();

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadListLunasUser();
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
                    if (listUser.getUser_level().equals("1")) {
                        namaCustomer.add(listUser.getNama());
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

    private void loadListLunasUser() {
        apiRest.listcredituser(id_user,status).enqueue(new Callback<List<MPandingAdmin>>() {
            @Override
            public void onResponse(Call<List<MPandingAdmin>> call, Response<List<MPandingAdmin>> response) {
                if (response.body()!=null){
                    List<MPandingAdmin> mPandingAdmins = response.body();
                    aPandingAdmin = new ALunasUser(v.getContext(), mPandingAdmins);
                    recyclerView.setAdapter(aPandingAdmin);
                }else {
                    Toasty.info(context,"Tidak Ada Pembayaran Credit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MPandingAdmin>> call, Throwable t) {
                Toasty.error(v.getContext(), "Tidak Ada Pembayaran Credit", Toast.LENGTH_SHORT).show();
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
        if (tv_tanggal.getText().equals("") && lstCustomer .getText().equals("Search by kurir")){
            valid = false;
            Toasty.error(context,"Harap isi salah satu menu pencarian",Toast.LENGTH_SHORT).show();
        }if (tv_tanggal.getText().equals("") && lstCustomer .getText().equals("")){
            valid = false;
            Toasty.error(context,"Harap isi salah satu menu pencarian",Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
    private void search() {
        if (validasi()==true){
            String suser = id_user;
            String sstatus = status;
            String sid_kurir;
            String stanggal;
            if (lstCustomer.getText().equals("") || lstCustomer.getText().equals("Search by kurir")) {
                sid_kurir = "";
            } else {
                sid_kurir = lstIdUser.get(lstCustomer.getSelectedIndex());
            }
            if (tv_tanggal.getText().equals("")) {
                stanggal = "";
            } else {
                stanggal = tv_tanggal.getText().toString();
            }
            apiRest.searchuser(suser,sstatus,sid_kurir,stanggal).enqueue(new Callback<List<MPandingAdmin>>() {
                @Override
                public void onResponse(Call<List<MPandingAdmin>> call, Response<List<MPandingAdmin>> response) {
                    if (response.body()!=null){
                        List<MPandingAdmin> mPandingAdmins = response.body();
                        aPandingAdmin = new ALunasUser( v.getContext(), mPandingAdmins);
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
                            loadListLunasUser();
                        }
                    });
                    pDialog.show();
                }
            });
        }

    }
}
