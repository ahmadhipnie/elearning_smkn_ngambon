package com.example.elearning_smkn_ngambon.adapter.guruFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.model.guruModel.MataPelajaranGuruModel;

import com.example.elearning_smkn_ngambon.ui.guru.MateriGuruActivity;

import java.util.List;

public class MataPelajaranGuruAdapter extends RecyclerView.Adapter<MataPelajaranGuruAdapter.MataPelajaranGuruViewHolder>{

    private final Context context;
    private final List<MataPelajaranGuruModel> mataPelajaranGuruList;

    public MataPelajaranGuruAdapter(Context context, List<MataPelajaranGuruModel> mataPelajaranGuruList) {
        this.context = context;
        this.mataPelajaranGuruList = mataPelajaranGuruList;
    }


    public static class MataPelajaranGuruViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgMapel;
        public TextView tvJudulMapel;
        public TextView tvKelas;
        public TextView btnLihatDetail;
        public CardView cvMataPelajaran;

        public MataPelajaranGuruViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMapel = itemView.findViewById(R.id.img_mapel);
            tvJudulMapel = itemView.findViewById(R.id.tv_judul_mapel);
            tvKelas = itemView.findViewById(R.id.tv_kelas);
            btnLihatDetail = itemView.findViewById(R.id.btn_lihat_detail);
            cvMataPelajaran = itemView.findViewById(R.id.cv_tugas);
        }
    }



    @NonNull
    @Override
    public MataPelajaranGuruAdapter.MataPelajaranGuruViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mata_pelajaran, parent, false);
        return new MataPelajaranGuruAdapter.MataPelajaranGuruViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MataPelajaranGuruAdapter.MataPelajaranGuruViewHolder holder, int position) {
        MataPelajaranGuruModel mataPelajaranGuru = mataPelajaranGuruList.get(position);
        holder.tvJudulMapel.setText(mataPelajaranGuru.getIdMapel() + " - " +mataPelajaranGuru.getNamaMapel());
        holder.tvKelas.setText(mataPelajaranGuru.getNamaKelas());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keMateriGuru = new Intent(context, MateriGuruActivity.class);
                keMateriGuru.putExtra("id_mapel", mataPelajaranGuru.getIdMapel());
                keMateriGuru.putExtra("nama_mapel", mataPelajaranGuru.getNamaMapel());
                keMateriGuru.putExtra("nama_kelas", mataPelajaranGuru.getNamaKelas());
                keMateriGuru.putExtra("id_kelas", mataPelajaranGuru.getIdKelas());
                context.startActivity(keMateriGuru);

            }
        });
    }

    @Override
    public int getItemCount() {return mataPelajaranGuruList.size();}
}
