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
import com.example.elearning_smkn_ngambon.model.MateriModel;
import com.example.elearning_smkn_ngambon.ui.DetailMateriActivity;

import java.util.List;

public class MateriAdapter extends RecyclerView.Adapter<MateriAdapter.MateriViewHolder>{

    private final Context context;
    private final List<MateriModel> materiList;

    public MateriAdapter(Context context, List<MateriModel> materiModelList) {
        this.context = context;
        this.materiList = materiModelList;
    }

    @NonNull
    @Override
    public MateriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materi, parent, false);
        return new MateriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriViewHolder holder, int position) {
        MateriModel materi = materiList.get(position);
        holder.tvNamaMateri.setText(materi.getIdMateri() + " - " + materi.getJudulMateri());

        String keteranganMateri = materi.getKeteranganMateri();
        if (keteranganMateri.length() > 20) {
            keteranganMateri = keteranganMateri.substring(0, 20) + ". . .";
        }
        holder.tvKeterangan.setText(keteranganMateri);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keDetailMateri = new Intent(context, DetailMateriActivity.class);
                keDetailMateri.putExtra("id_materi", materi.getIdMateri());
                keDetailMateri.putExtra("judul_materi", materi.getJudulMateri());
                keDetailMateri.putExtra("keterangan_materi", materi.getKeteranganMateri());
                context.startActivity(keDetailMateri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return materiList.size();
    }

    public static class MateriViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNamaMateri;
        public TextView tvKeterangan;

        public MateriViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMateri = itemView.findViewById(R.id.tv_judul_materi);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan_materi);
        }
    }
}

