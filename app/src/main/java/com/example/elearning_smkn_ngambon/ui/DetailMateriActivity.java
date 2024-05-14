package com.example.elearning_smkn_ngambon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.google.android.material.snackbar.Snackbar;

public class DetailMateriActivity extends AppCompatActivity {

    TextView tvJudulMateri, tvKeteranganMateri;
    Button btnDownloadMateri;
    ImageView btnKembali;
    int idMateri;
    String judulMateri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_materi);

        tvJudulMateri = findViewById(R.id.tv_judul_materi);
        tvKeteranganMateri = findViewById(R.id.tv_keterangan_materi_detail);
        btnDownloadMateri = findViewById(R.id.btn_download_materi);
        btnKembali = findViewById(R.id.btnKembali);

        btnKembali.setOnClickListener(v -> {
            finish();
        });

        Intent getIntent = getIntent();
        idMateri = getIntent.getIntExtra("id_materi", 0);
        judulMateri = getIntent.getStringExtra("judul_materi");
        String keteranganMateri = getIntent.getStringExtra("keterangan_materi");

        // Set judul dan keterangan materi
        tvJudulMateri.setText(judulMateri);
        tvKeteranganMateri.setText(keteranganMateri);

        btnDownloadMateri.setOnClickListener(v -> {
            downloadMateri();
        });
    }

    private void downloadMateri() {
        String url = apiconfig.DOWNLOAD_MATERI + "?id_materi=" + idMateri;

        // Buat permintaan unduhan
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // Set judul file
        String fileName = judulMateri;
        request.setTitle(fileName);
        request.setDescription("Mengunduh materi");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        // Mulai unduhan
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Snackbar.make(findViewById(android.R.id.content), "Materi sedang diunduh...", Snackbar.LENGTH_SHORT).show();
    }
}
