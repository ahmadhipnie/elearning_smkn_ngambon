package com.example.elearning_smkn_ngambon.ui.guru;

import static android.content.ContentValues.TAG;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

public class DetailPengumpulanTugasGuruActivity extends AppCompatActivity {

    TextView tvLampiranTugasGuru, tvWaktuPengumpulan, tvJudulPengumpulan;

    EditText etNilaiPengumpulan;

    Button btnSimpanNilai, btnDownloadTugas;
    int idPengumpulan;
    String lampiranTugas, namaSiswa, judulMateri;
    ImageView btnKembali;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pengumpulan_tugas_guru);

        tvLampiranTugasGuru = findViewById(R.id.tv_lampiran_tugas_guru);
        tvWaktuPengumpulan = findViewById(R.id.tv_waktu_pengumpulan);
        tvJudulPengumpulan = findViewById(R.id.tv_judul_pengumpulan);
        btnKembali = findViewById(R.id.btnKembali);
        etNilaiPengumpulan = findViewById(R.id.et_nilai_pengumpulan_tugas);
        btnSimpanNilai = findViewById(R.id.btn_simpan_nilai);
        btnDownloadTugas = findViewById(R.id.btn_download_tugas);

        Intent intent = getIntent();
        idPengumpulan = intent.getIntExtra("id_pengumpulan", 0);
        lampiranTugas = intent.getStringExtra("lampiran_tugas");
        int idTugas = intent.getIntExtra("id_tugas", 0);
        int idSiswa = intent.getIntExtra("id_siswa", 0);
        int nilai = intent.getIntExtra("nilai", 0);
        String createdAt = intent.getStringExtra("created_at");
        String formattedCreatedAt = intent.getStringExtra("formatted_created_at");
        String updatedAt = intent.getStringExtra("updated_at");
        namaSiswa = intent.getStringExtra("nama_siswa");
        judulMateri = intent.getStringExtra("judul_materi");

        Log.d(TAG, "onCreate: " + idPengumpulan + " " + lampiranTugas + " " + idTugas + " " + idSiswa + " " + nilai + " " + createdAt + " " + updatedAt + " " + namaSiswa + " " + judulMateri + " " + formattedCreatedAt);

        tvLampiranTugasGuru.setText(lampiranTugas);
        tvWaktuPengumpulan.setText(formattedCreatedAt);
        tvJudulPengumpulan.setText(judulMateri);

        etNilaiPengumpulan.setText(String.valueOf(nilai));

        btnKembali.setOnClickListener(v -> {
            finish();
        });

        btnSimpanNilai.setOnClickListener(v -> {
            simpanNilai();
        });


        btnDownloadTugas.setOnClickListener(v -> {
            downloadTugas();
        });



    }

    private void downloadTugas() {
        String url = apiconfig.DOWNLOAD_PENGUMPULAN_TUGAS_GURU_ENDPOINT + "?id_pengumpulan=" + idPengumpulan;

        // Buat permintaan unduhan
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // Set judul file
        String fileName = namaSiswa+"_"+judulMateri+".pdf";
        request.setTitle(fileName);
        request.setDescription("Mengunduh tugas");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        // Mulai unduhan
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

        Snackbar.make(findViewById(android.R.id.content), "Tugas sedang diunduh...", Snackbar.LENGTH_SHORT).show();
    }

    private void simpanNilai() {
        String nilaiBaru = etNilaiPengumpulan.getText().toString().trim();

        String url = apiconfig.EDIT_NILAI_TUGAS_GURU_ENDPOINT;


        if (nilaiBaru.isEmpty()) {
            Toast.makeText(this, "nilai tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (nilaiBaru.length() > 3) {
            Toast.makeText(this, "nilai tidak boleh lebih dari 3 karakter", Toast.LENGTH_SHORT).show();
            return;
        } else if (Integer.parseInt(nilaiBaru) < 0 || Integer.parseInt(nilaiBaru) > 100) {
            Toast.makeText(this, "nilai harus diantara 0 sampai 100", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.contains("Nilai berhasil diperbarui.")) {
                            Toast.makeText(DetailPengumpulanTugasGuruActivity.this, "Nilai berhasil diperbarui", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DetailPengumpulanTugasGuruActivity.this, MainActivityGuru.class);
                            intent.putExtra("tab_guru", "beranda_guru");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(DetailPengumpulanTugasGuruActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailPengumpulanTugasGuruActivity.this, "Pemberian Nilai Gagal", Toast.LENGTH_SHORT).show();
                        Log.e("EditTugasActivity", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_pengumpulan", String.valueOf(idPengumpulan));
                params.put("nilai", nilaiBaru);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DetailPengumpulanTugasGuruActivity.this);
        requestQueue.add(stringRequest);
    }
}