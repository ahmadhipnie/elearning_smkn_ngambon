package com.example.elearning_smkn_ngambon.adapter.guruFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.adapter.TugasAdapter;
import com.example.elearning_smkn_ngambon.model.TugasModel;
import com.example.elearning_smkn_ngambon.model.guruModel.TugasGuruModel;
import com.example.elearning_smkn_ngambon.ui.DetailTugasActivity;
import com.example.elearning_smkn_ngambon.ui.guru.EditTugasActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TugasGuruAdapter extends RecyclerView.Adapter<TugasGuruAdapter.TugasGuruViewHolder>{

    private final Context context;
    private final List<TugasGuruModel> tugasGuruList;

    public TugasGuruAdapter(Context context, List<TugasGuruModel> tugasGuruList) {
        this.context = context;
        this.tugasGuruList = tugasGuruList;
    }

    public static class TugasGuruViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTenggatWaktu;
        public TextView tvNamaMateri;
        public TextView tvKeterangan;

        public TugasGuruViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMateri = itemView.findViewById(R.id.tv_judul_materi);
            tvTenggatWaktu = itemView.findViewById(R.id.tv_tenggat_waktu);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
        }
    }


    @NonNull
    @Override
    public TugasGuruAdapter.TugasGuruViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tugas, parent, false);
        return new TugasGuruAdapter.TugasGuruViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TugasGuruAdapter.TugasGuruViewHolder holder, int position) {
        TugasGuruModel tugas = tugasGuruList.get(position);
        holder.tvNamaMateri.setText(tugas.getIdTugas() + " - "+ tugas.getJudulMateri());
        holder.tvTenggatWaktu.setText("Tenggat Waktu : " + formatDate(tugas.getTenggatWaktu()));

        String deskripsi = tugas.getKeterangan();
        if (deskripsi.length() > 20) {
            deskripsi = deskripsi.substring(0, 20) + ". . .";
        }
        holder.tvKeterangan.setText(deskripsi);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keEditTugas = new Intent(context, EditTugasActivity.class);
                keEditTugas.putExtra("id_tugas", tugas.getIdTugas());
                keEditTugas.putExtra("judul_materi", tugas.getJudulMateri());
                keEditTugas.putExtra("tenggat_waktu", formatDate(tugas.getTenggatWaktu()));
                keEditTugas.putExtra("keterangan", tugas.getKeterangan());
                context.startActivity(keEditTugas);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tugasGuruList.size();
    }

    private String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format sumber
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy"); // Format tujuan
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ""; // Mengembalikan string kosong jika gagal memformat tanggal
    }
}
