package com.example.projekta.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.projekta.R;
import com.example.projekta.controller.CDeleteDataUser;
import com.example.projekta.controller.CEditDataUser;
import com.example.projekta.model.MDataUser;

import java.util.List;

public class ADataUser extends RecyclerView.Adapter<ADataUser.ViewHolder> {
    private Context context;
    private List<MDataUser> pandingAdminList;

    public ADataUser(Context context, List<MDataUser> pandingAdminList){
        this.context = context;
        this.pandingAdminList = pandingAdminList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_daftar_user, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.tv_nama.setText(""+pandingAdminList.get(i).getNama());
        holder.tv_alamat.setText(""+pandingAdminList.get(i).getAlamat());
        holder.tv_noHp.setText(""+pandingAdminList.get(i).getNo_hp());
        holder.tv_tanggal_lahir.setText(""+pandingAdminList.get(i).getTanggal_lahir());
        holder.tv_username.setText(""+pandingAdminList.get(i).getUsername());
        holder.tv_password.setText(""+pandingAdminList.get(i).getPassword());
        if (pandingAdminList.get(i).getUser_level().equals("0")){
            holder.tv_userLevel.setText("Konsumen");
        }else  if (pandingAdminList.get(i).getUser_level().equals("1")){
            holder.tv_userLevel.setText("Kurir");
        }

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customer_id = pandingAdminList.get(i).getId_user();
                String nama = pandingAdminList.get(i).getNama();
                String alamat = pandingAdminList.get(i).getAlamat();
                String no_hp = pandingAdminList.get(i).getNo_hp();
                String tanggal_lahir = pandingAdminList.get(i).getTanggal_lahir();
                String username = pandingAdminList.get(i).getUsername();
                String password = pandingAdminList.get(i).getPassword();
                String user_level = pandingAdminList.get(i).getUser_level();

                CEditDataUser cEditDataUser = new CEditDataUser(context);
                cEditDataUser.editData(customer_id,nama,alamat,no_hp,tanggal_lahir,username,password,user_level);
            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String customer_id = pandingAdminList.get(i).getId_user();
                String user_level = pandingAdminList.get(i).getUser_level();

                CDeleteDataUser cDeleteDataUser = new CDeleteDataUser(context);
                cDeleteDataUser.deleteUser(customer_id,user_level);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pandingAdminList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_nama,tv_alamat,tv_noHp,tv_tanggal_lahir,tv_username,tv_password,tv_userLevel;
        private Button btn_edit,btn_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_nama = itemView.findViewById(R.id.tv_namaData);
            tv_alamat = itemView.findViewById(R.id.tv_alamatData);
            tv_noHp = itemView.findViewById(R.id.tv_noHpData);
            tv_tanggal_lahir = itemView.findViewById(R.id.tv_tglLahirData);
            tv_username = itemView.findViewById(R.id.tv_usernameData);
            tv_password = itemView.findViewById(R.id.tv_passwordData);
            tv_userLevel = itemView.findViewById(R.id.tv_userLevelData);
            btn_edit = itemView.findViewById(R.id.btn_editData);
            btn_delete = itemView.findViewById(R.id.btn_deleteData);
        }
    }
}
