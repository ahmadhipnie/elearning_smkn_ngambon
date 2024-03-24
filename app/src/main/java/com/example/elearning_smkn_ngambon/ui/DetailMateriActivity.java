package com.example.elearning_smkn_ngambon.ui;


import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;


import android.Manifest;
import android.app.DownloadManager;

import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DetailMateriActivity extends AppCompatActivity {

    TextView tvJudulMateri, tvKeteranganMateri;
    Button btnDownloadMateri;
    ImageView btnKembali;

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
        int idMateri = getIntent.getIntExtra("id_materi", 0);
        String judulMateri = getIntent.getStringExtra("judul_materi");
        String keteranganMateri = getIntent.getStringExtra("keterangan_materi");

        // Set judul dan keterangan materi
        tvJudulMateri.setText(judulMateri);
        tvKeteranganMateri.setText(keteranganMateri);

        btnDownloadMateri.setOnClickListener(v -> {
            downloadMateri(idMateri, judulMateri);
        });


    }

    private void downloadMateri(int idMateri, String judulMateri) {
        String url = apiconfig.DOWNLOAD_MATERI + "?id_materi=" + idMateri;

        // Buat timestamp saat ini
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());

        // Buat nama file dengan timestamp
        String fileName = timeStamp + "_Materi_" + judulMateri;

        // Buat permintaan unduhan
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        request.setTitle(fileName);
        request.setDescription("Mengunduh materi");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,  fileName);

        // Mulai unduhan
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Snackbar.make(findViewById(android.R.id.content), "Materi sedang diunduh...", Snackbar.LENGTH_SHORT).show();

    }

}
