package com.example.elearning_smkn_ngambon.adapter;
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
import com.example.elearning_smkn_ngambon.model.MataPelajaranModel;
import com.example.elearning_smkn_ngambon.ui.MateriActivity;

import java.util.List;

public class MataPelajaranAdapter extends RecyclerView.Adapter<MataPelajaranAdapter.MataPelajaranViewHolder> {
    private final Context context;
    private final List<MataPelajaranModel> mataPelajaranList;

    public MataPelajaranAdapter(Context context, List<MataPelajaranModel> mataPelajaranList) {
        this.context = context;
        this.mataPelajaranList = mataPelajaranList;
    }

    @NonNull
    @Override
    public MataPelajaranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mata_pelajaran, parent, false);
        return new MataPelajaranViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MataPelajaranViewHolder holder, int position) {
        MataPelajaranModel mataPelajaran = mataPelajaranList.get(position);
        holder.tvJudulMapel.setText(mataPelajaran.getIdMapel() + " - " +mataPelajaran.getNamaMapel());
        holder.tvKelas.setText(mataPelajaran.getNamaKelas());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keMateri = new Intent(context, MateriActivity.class);
                keMateri.putExtra("id_mapel", mataPelajaran.getIdMapel());
                keMateri.putExtra("nama_mapel", mataPelajaran.getNamaMapel());
                keMateri.putExtra("nama_kelas", mataPelajaran.getNamaKelas());
                context.startActivity(keMateri);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mataPelajaranList.size();
    }

    public static class MataPelajaranViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgMapel;
        public TextView tvJudulMapel;
        public TextView tvKelas;
        public TextView btnLihatDetail;
        public CardView cvMataPelajaran;

        public MataPelajaranViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMapel = itemView.findViewById(R.id.img_mapel);
            tvJudulMapel = itemView.findViewById(R.id.tv_judul_mapel);
            tvKelas = itemView.findViewById(R.id.tv_kelas);
            btnLihatDetail = itemView.findViewById(R.id.btn_lihat_detail);
            cvMataPelajaran = itemView.findViewById(R.id.cv_mata_pelajaran);
        }
    }
}
