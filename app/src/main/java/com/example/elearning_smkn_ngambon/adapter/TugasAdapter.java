package com.example.elearning_smkn_ngambon.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.model.TugasModel;
import com.example.elearning_smkn_ngambon.ui.DetailTugasActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TugasAdapter extends RecyclerView.Adapter<TugasAdapter.TugasViewHolder> {
    private final Context context;
    private final List<TugasModel> tugasList;

    public TugasAdapter(Context context, List<TugasModel> tugasList) {
        this.context = context;
        this.tugasList = tugasList;
    }

    @NonNull
    @Override
    public TugasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tugas, parent, false);
        return new TugasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TugasViewHolder holder, int position) {
        TugasModel tugas = tugasList.get(position);
        holder.tvNamaMateri.setText(tugas.getIdTugas() + " - "+ tugas.getNamaMateri());
        holder.tvTenggatWaktu.setText("Tenggat Waktu : " + formatDate(tugas.getTenggatWaktu()));

        String deskripsi = tugas.getKeterangan();
        if (deskripsi.length() > 20) {
            deskripsi = deskripsi.substring(0, 20) + ". . .";
        }
        holder.tvKeterangan.setText(deskripsi);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keDetailTugas = new Intent(context, DetailTugasActivity.class);
                keDetailTugas.putExtra("id_tugas", tugas.getIdTugas());
                keDetailTugas.putExtra("nama_materi", tugas.getNamaMateri());
                keDetailTugas.putExtra("tenggat_waktu", formatDate(tugas.getTenggatWaktu()));
                keDetailTugas.putExtra("keterangan", tugas.getKeterangan());
                context.startActivity(keDetailTugas);

            }
        });
    }

    @Override
    public int getItemCount() {
        return tugasList.size();
    }

    public static class TugasViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTenggatWaktu;
        public TextView tvNamaMateri;
        public TextView tvKeterangan;

        public TugasViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMateri = itemView.findViewById(R.id.tv_judul_materi);
            tvTenggatWaktu = itemView.findViewById(R.id.tv_tenggat_waktu);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan);
        }
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
