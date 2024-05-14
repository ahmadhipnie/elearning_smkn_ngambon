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
import com.example.elearning_smkn_ngambon.model.guruModel.PengumpulanTugasGuruModel;
import com.example.elearning_smkn_ngambon.ui.guru.DetailPengumpulanTugasGuruActivity;

import java.util.List;

public class PengumpulanTugasGuruAdapter extends RecyclerView.Adapter<PengumpulanTugasGuruAdapter.PengumpulanTugasGuruViewHolder> {

    private final Context context;
    private final List<PengumpulanTugasGuruModel> pengumpulanTugasGuruList;

    public PengumpulanTugasGuruAdapter(Context context, List<PengumpulanTugasGuruModel> pengumpulanTugasGuruList) {
        this.context = context;
        this.pengumpulanTugasGuruList = pengumpulanTugasGuruList;
    }

    public static class PengumpulanTugasGuruViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPengumpulanTugas;
        public TextView tvJudulMateri, tvNamaSiswaPengumpulanTugas, tvNamaKelasPengumpulanTugas, tvWaktuPengumpulanTugas, tvNilaiPengumpulanTugas;
        public CardView cvPengumpulanTugas;

        public PengumpulanTugasGuruViewHolder(@NonNull View itemView) {
            super(itemView);
            cvPengumpulanTugas = itemView.findViewById(R.id.cv_pengumpulan_tugas);
            imgPengumpulanTugas = itemView.findViewById(R.id.img_pengumpulan_tugas);
            tvNamaSiswaPengumpulanTugas = itemView.findViewById(R.id.tv_nama_siswa_pengumpulan_tugas_guru);
            tvJudulMateri = itemView.findViewById(R.id.tv_judul_materi_pengumpulan_tugas_guru);
            tvWaktuPengumpulanTugas = itemView.findViewById(R.id.tv_waktu_pengumpulan_pengumpulan_tugas_guru);
            tvNilaiPengumpulanTugas = itemView.findViewById(R.id.tv_nilai_pengumpulan_tugas_guru);
        }
    }

    @NonNull
    @Override
    public PengumpulanTugasGuruAdapter.PengumpulanTugasGuruViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pengumpulan_tugas, parent, false);
        return new PengumpulanTugasGuruAdapter.PengumpulanTugasGuruViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengumpulanTugasGuruAdapter.PengumpulanTugasGuruViewHolder holder, int position) {
        PengumpulanTugasGuruModel pengumpulanTugasGuruModel = pengumpulanTugasGuruList.get(position);
        holder.tvNamaSiswaPengumpulanTugas.setText(pengumpulanTugasGuruModel.getNamaSiswa());
        holder.tvJudulMateri.setText("Materi : "+ pengumpulanTugasGuruModel.getJudulMateri());
        holder.tvWaktuPengumpulanTugas.setText("tanggal : " + pengumpulanTugasGuruModel.getFormattedCreatedAt());
        holder.tvNilaiPengumpulanTugas.setText("Nilai : " + String.valueOf(pengumpulanTugasGuruModel.getNilai()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keDetailPengumpulanTugas = new Intent(context, DetailPengumpulanTugasGuruActivity.class);
                keDetailPengumpulanTugas.putExtra("id_pengumpulan", pengumpulanTugasGuruModel.getIdPengumpulan());
                keDetailPengumpulanTugas.putExtra("lampiran_tugas", pengumpulanTugasGuruModel.getLampiranTugas());
                keDetailPengumpulanTugas.putExtra("id_tugas", pengumpulanTugasGuruModel.getIdTugas());
                keDetailPengumpulanTugas.putExtra("id_siswa", pengumpulanTugasGuruModel.getIdSiswa());
                keDetailPengumpulanTugas.putExtra("nilai", pengumpulanTugasGuruModel.getNilai());
                keDetailPengumpulanTugas.putExtra("created_at", pengumpulanTugasGuruModel.getCreatedAt());
                keDetailPengumpulanTugas.putExtra("formatted_created_at", pengumpulanTugasGuruModel.getFormattedCreatedAt());
                keDetailPengumpulanTugas.putExtra("updated_at", pengumpulanTugasGuruModel.getUpdatedAt());
                keDetailPengumpulanTugas.putExtra("nama_siswa", pengumpulanTugasGuruModel.getNamaSiswa());
                keDetailPengumpulanTugas.putExtra("judul_materi", pengumpulanTugasGuruModel.getJudulMateri());
                context.startActivity(keDetailPengumpulanTugas);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pengumpulanTugasGuruList.size();
    }
}
