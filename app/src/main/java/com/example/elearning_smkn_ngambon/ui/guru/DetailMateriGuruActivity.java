package com.example.elearning_smkn_ngambon.ui.guru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.adapter.guruFragment.TugasGuruAdapter;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.example.elearning_smkn_ngambon.model.TugasModel;
import com.example.elearning_smkn_ngambon.model.guruModel.TugasGuruModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailMateriGuruActivity extends AppCompatActivity {


    Button btnDownloadMateri, btnTambahTugasGuru;
    ImageView btnKembali;
    TextView tvJudulMateriGuru, tvKeteranganDetailMateriGuru;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<TugasGuruModel> tugasGuruList;

    private TugasGuruAdapter tugasGuruAdapter;

    private RecyclerView recyclerView;

    String judulMateri;

    int idMateri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_materi_guru);

        tvJudulMateriGuru = findViewById(R.id.tv_judul_materi_guru);
        tvKeteranganDetailMateriGuru = findViewById(R.id.tv_keterangan_detail_materi_guru);
        btnDownloadMateri = findViewById(R.id.btn_download_materi_guru);
        btnTambahTugasGuru = findViewById(R.id.btn_tambah_tugas_guru);
        btnKembali = findViewById(R.id.btnKembali);
        recyclerView = findViewById(R.id.rv_tugas_guru);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tugasGuruList = new ArrayList<>();
        tugasGuruAdapter = new TugasGuruAdapter(this, tugasGuruList);
        recyclerView.setAdapter(tugasGuruAdapter);

        // Inisialisasi ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang Memuat...");
        progressDialog.setCancelable(false);

        progressDialog.show();

        btnDownloadMateri.setOnClickListener(v -> {
           downloadMateri();
        });

        btnKembali.setOnClickListener(v -> {
            finish();
        });


        Intent intent = getIntent();
        idMateri = intent.getIntExtra("id_materi", 0);
        judulMateri = intent.getStringExtra("judul_materi");
        String keteranganMateri = intent.getStringExtra("keterangan_materi");

        btnTambahTugasGuru.setOnClickListener(v -> {
            Intent intentKeTambahTugas = new Intent(DetailMateriGuruActivity.this, TambahTugasGuruActivity.class);
            intentKeTambahTugas.putExtra("id_materi", idMateri);
            startActivity(intentKeTambahTugas);
        });

        tvJudulMateriGuru.setText(judulMateri);
        tvKeteranganDetailMateriGuru.setText(keteranganMateri);
        
        showRecycleViewTugasGuru();
    }

    private void showRecycleViewTugasGuru() {

        String url = apiconfig.TAMPIL_TUGAS_DETAIL_MATERI_GURU_ENDPOINT + "?id_materi=" + idMateri;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject orderObj = jsonArray.getJSONObject(i);
                                int idTugas = orderObj.getInt("id_tugas");
                                String judulMateri = orderObj.getString("judul_materi");
                                String tenggatWaktu = orderObj.getString("tenggat_waktu");
                                String keterangan = orderObj.getString("keterangan");
                                TugasGuruModel tugasGuruModel = new TugasGuruModel(idTugas, judulMateri, tenggatWaktu, keterangan);
                                tugasGuruList.add(tugasGuruModel);
                            }
                            tugasGuruAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "gagal menampilkan tugas", Snackbar.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(request);
    }

    private void downloadMateri() {
        String url = apiconfig.DOWNLOAD_MATERI + "?id_materi=" + idMateri;

        // Buat permintaan unduhan
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // Set judul file
        String fileName = judulMateri + ".pdf";
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