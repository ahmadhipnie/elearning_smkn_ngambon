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
import com.example.elearning_smkn_ngambon.adapter.MateriAdapter;
import com.example.elearning_smkn_ngambon.model.guruModel.MateriGuruModel;
import com.example.elearning_smkn_ngambon.ui.guru.DetailMateriGuruActivity;

import java.util.List;

public class MateriGuruAdapter extends RecyclerView.Adapter<MateriGuruAdapter.MateriGuruViewHolder>{

    private final Context context;
    private final List<MateriGuruModel> materiGuruList;

    public MateriGuruAdapter(Context context, List<MateriGuruModel> materiGuruList) {
        this.context = context;
        this.materiGuruList = materiGuruList;
    }


    public static class MateriGuruViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNamaMateri;
        public TextView tvKeterangan;
        public MateriGuruViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMateri = itemView.findViewById(R.id.tv_judul_materi);
            tvKeterangan = itemView.findViewById(R.id.tv_keterangan_materi);
        }
    }



    @NonNull
    @Override
    public MateriGuruAdapter.MateriGuruViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_materi, parent, false);
        return new MateriGuruAdapter.MateriGuruViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MateriGuruAdapter.MateriGuruViewHolder holder, int position) {
        MateriGuruModel materi = materiGuruList.get(position);
        holder.tvNamaMateri.setText(materi.getIdMateri() + " - " + materi.getJudulMateri());

        String keteranganMateri = materi.getKeteranganMateri();
        if (keteranganMateri.length() > 20) {
            keteranganMateri = keteranganMateri.substring(0, 20) + ". . .";
        }
        holder.tvKeterangan.setText(keteranganMateri);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keDetailMateri = new Intent(context, DetailMateriGuruActivity.class);
                keDetailMateri.putExtra("id_materi", materi.getIdMateri());
                keDetailMateri.putExtra("judul_materi", materi.getJudulMateri());
                keDetailMateri.putExtra("keterangan_materi", materi.getKeteranganMateri());
                context.startActivity(keDetailMateri);
            }
        });
    }

    @Override
    public int getItemCount() {
        return materiGuruList.size();
    }

}
