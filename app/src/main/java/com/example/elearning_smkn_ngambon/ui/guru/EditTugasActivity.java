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

public class EditTugasActivity extends AppCompatActivity {

    ImageView btnKembali;
    Button btnEditTugasGuru;
    EditText etKeteranganTugasGuru, etTenggatWaktuTugasGuru;
    ProgressDialog progressDialog;
    int idTugas;
    String keterangan, tenggatWaktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tugas);

        etKeteranganTugasGuru = findViewById(R.id.et_keterangan_tugas_guru);
        etTenggatWaktuTugasGuru = findViewById(R.id.et_tenggat_waktu_tugas_guru);
        btnKembali = findViewById(R.id.btnKembali);
        btnEditTugasGuru = findViewById(R.id.btn_edit_tugas_guru);

        Intent getIntent = getIntent();
        idTugas = getIntent.getIntExtra("id_tugas", 0);
        keterangan = getIntent.getStringExtra("keterangan");
        tenggatWaktu = getIntent.getStringExtra("tenggat_waktu");

        etKeteranganTugasGuru.setText(keterangan);
        etTenggatWaktuTugasGuru.setText(tenggatWaktu);

        btnKembali.setOnClickListener(v -> {
            finish();
        });

        btnEditTugasGuru.setOnClickListener(v -> {
            editTugasGuru();
        });
    }

    private void editTugasGuru() {
        String keteranganBaru = etKeteranganTugasGuru.getText().toString().trim();
        String tenggatWaktuBaru = etTenggatWaktuTugasGuru.getText().toString().trim();

        if (keteranganBaru.isEmpty()) {
            Toast.makeText(this, "Keterangan tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (tenggatWaktuBaru.isEmpty()) {
            Toast.makeText(this, "Tenggat waktu tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        // Konversi format tanggal dari dd-MM-yyyy ke yyyy-MM-dd
        String tenggatWaktuEditTugasGuruFormatted = convertDateFormat(tenggatWaktuBaru);
        if (tenggatWaktuEditTugasGuruFormatted == null) {
            Toast.makeText(this, "Format tanggal tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = apiconfig.EDIT_TUGAS_GURU_ENDPOINT;

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.contains("Tugas berhasil diupdate")) {
                            Toast.makeText(EditTugasActivity.this, "Tugas berhasil diupdate", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(EditTugasActivity.this, MainActivityGuru.class);
                            intent.putExtra("tab_guru", "beranda_guru");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(EditTugasActivity.this, response, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(EditTugasActivity.this, "Edit Tugas Gagal", Toast.LENGTH_SHORT).show();
                        Log.e("EditTugasActivity", "Error: " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_tugas", String.valueOf(idTugas));
                params.put("tenggat_waktu", tenggatWaktuEditTugasGuruFormatted);
                params.put("keterangan", keteranganBaru);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(EditTugasActivity.this);
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
