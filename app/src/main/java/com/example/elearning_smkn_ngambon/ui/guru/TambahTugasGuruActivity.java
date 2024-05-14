package com.example.elearning_smkn_ngambon.ui.guru;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.api.apiconfig;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TambahTugasGuruActivity extends AppCompatActivity {

    EditText etKeteranganTambahTugasGuru, etTenggatWaktuTambahTugasGuru;

    ImageView btnKembali;
    ProgressDialog progressDialog;


    int idMateri;

    Button btnTambahTugasGuru;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_tugas_guru);

        etKeteranganTambahTugasGuru = findViewById(R.id.et_keterangan_tambah_tugas_guru);
        etTenggatWaktuTambahTugasGuru = findViewById(R.id.et_tenggat_waktu_tambah_tugas_guru);

        btnTambahTugasGuru = findViewById(R.id.btn_tambah_tugas_guru);

        btnKembali = findViewById(R.id.btnKembali);

        Intent getIntent = getIntent();
        idMateri = getIntent.getIntExtra("id_materi", 0);

        Log.d("id_materi", String.valueOf(idMateri));


        btnKembali.setOnClickListener(v -> {
            finish();
        });

        btnTambahTugasGuru.setOnClickListener(v -> {
            tambahTugasGuru();
        });


    }

    private void tambahTugasGuru() {

        String keteranganTambahTugasGuru = etKeteranganTambahTugasGuru.getText().toString().trim();
        String tenggatWaktuTambahTugasGuru = etTenggatWaktuTambahTugasGuru.getText().toString().trim();

        if (keteranganTambahTugasGuru.isEmpty()) {
            etKeteranganTambahTugasGuru.setError("Keterangan Tugas Harus Diisi");
            etKeteranganTambahTugasGuru.requestFocus();
            return;
        } else if (tenggatWaktuTambahTugasGuru.isEmpty()) {
            etTenggatWaktuTambahTugasGuru.setError("Tenggat Waktu Tugas Harus Diisi");
            etTenggatWaktuTambahTugasGuru.requestFocus();
            return;
        }

        String tenggatWaktuTambahTugasGuruFormatted = convertDateFormat(tenggatWaktuTambahTugasGuru);
        if (tenggatWaktuTambahTugasGuruFormatted == null) {
            etTenggatWaktuTambahTugasGuru.setError("Format Tanggal Tidak Valid");
            etTenggatWaktuTambahTugasGuru.requestFocus();
            return;
        }

        String url = apiconfig.TAMBAH_TUGAS_GURU_ENDPOINT;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.contains("Tugas untuk materi ini sudah ada")) {
                            Toast.makeText(TambahTugasGuruActivity.this, response, Toast.LENGTH_SHORT).show();
                        } else if (response.contains("Tugas berhasil ditambahkan")){
                            Toast.makeText(TambahTugasGuruActivity.this, "Tugas Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TambahTugasGuruActivity.this, MainActivityGuru.class);
                            intent.putExtra("tab_guru", "beranda_guru");
                            startActivity(intent);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(TambahTugasGuruActivity.this, "Tugas untuk materi ini sudah ada", Toast.LENGTH_SHORT).show();
                        Log.e("EditTugasActivity", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_materi", String.valueOf(idMateri));
                params.put("tenggat_waktu", tenggatWaktuTambahTugasGuruFormatted);
                params.put("keterangan", keteranganTambahTugasGuru);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(TambahTugasGuruActivity.this);
        requestQueue.add(stringRequest);
    }

    private String convertDateFormat(String dateStr) {
        SimpleDateFormat fromUser = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date = fromUser.parse(dateStr);
            return myFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}