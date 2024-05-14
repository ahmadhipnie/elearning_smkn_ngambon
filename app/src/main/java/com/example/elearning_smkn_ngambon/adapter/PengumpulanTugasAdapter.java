package com.example.elearning_smkn_ngambon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.adapter.guruFragment.PengumpulanTugasGuruAdapter;
import com.example.elearning_smkn_ngambon.model.PengumpulanTugasModel;
import com.example.elearning_smkn_ngambon.model.guruModel.PengumpulanTugasGuruModel;

import java.util.List;

public class PengumpulanTugasAdapter extends RecyclerView.Adapter<PengumpulanTugasAdapter.PengumpulanTugasViewHolder> {

    private final Context context;
    private final List<PengumpulanTugasModel> pengumpulanTugasList;

    public PengumpulanTugasAdapter(Context context, List<PengumpulanTugasModel> pengumpulanTugasList) {
        this.context = context;
        this.pengumpulanTugasList = pengumpulanTugasList;
    }

    public static class PengumpulanTugasViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPengumpulanTugas;
        public TextView tvJudulMateri, tvNamaSiswaPengumpulanTugas, tvNamaKelasPengumpulanTugas, tvWaktuPengumpulanTugas, tvNilaiPengumpulanTugas;
        public CardView cvPengumpulanTugas;

        public PengumpulanTugasViewHolder(@NonNull View itemView) {
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
    public PengumpulanTugasAdapter.PengumpulanTugasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pengumpulan_tugas, parent, false);
        return new PengumpulanTugasAdapter.PengumpulanTugasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PengumpulanTugasAdapter.PengumpulanTugasViewHolder holder, int position) {
        PengumpulanTugasModel pengumpulanTugasModel = pengumpulanTugasList.get(position);
        holder.tvNamaSiswaPengumpulanTugas.setText(pengumpulanTugasModel.getLampiranTugas());
        holder.tvJudulMateri.setText("NISN: "+ pengumpulanTugasModel.getIdSiswa());
        holder.tvWaktuPengumpulanTugas.setText("tanggal : " + pengumpulanTugasModel.getFormattedCreatedAt());
        holder.tvNilaiPengumpulanTugas.setText("Nilai : " + String.valueOf(pengumpulanTugasModel.getNilai()));
        holder.imgPengumpulanTugas.setImageResource(R.drawable.ic_pdf);
    }

    @Override
    public int getItemCount() {return pengumpulanTugasList.size();}
}
