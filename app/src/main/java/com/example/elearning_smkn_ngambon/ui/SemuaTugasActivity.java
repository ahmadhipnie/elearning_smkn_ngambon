package com.example.elearning_smkn_ngambon.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.adapter.MataPelajaranAdapter;
import com.example.elearning_smkn_ngambon.adapter.TugasAdapter;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.example.elearning_smkn_ngambon.model.MataPelajaranModel;
import com.example.elearning_smkn_ngambon.model.TugasModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SemuaTugasActivity extends AppCompatActivity {

    private ImageView btnKembali;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private List<TugasModel> tugasList;

    private TugasAdapter tugasAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_tugas);

        recyclerView = findViewById(R.id.rv_semua_tugas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = this.getSharedPreferences("UserData", Context.MODE_PRIVATE);

        tugasList = new ArrayList<>();
        tugasAdapter = new TugasAdapter(this, tugasList);

        recyclerView.setAdapter(tugasAdapter);


        btnKembali = findViewById(R.id.btnKembali);
        // Menambahkan onClickListener untuk tombol kembali
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SemuaTugasActivity.this, MainActivity.class);
                intent.putExtra("tab", "dashboard");
                startActivity(intent);
                finish(); // Mengakhiri activity dan kembali ke activity sebelumnya
            }
        });

        // Inisialisasi ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang Memuat...");
        progressDialog.setCancelable(false);

        progressDialog.show();

        showRecycleViewTugas();
    }

    private void showRecycleViewTugas() {
        int id_kelas = sharedPreferences.getInt("id_kelas", 0);
        String url = apiconfig.TAMPIL_TUGAS + "?id_kelas=" + id_kelas;

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
                                TugasModel tugasModel = new TugasModel(idTugas, judulMateri, tenggatWaktu, keterangan);
                                tugasList.add(tugasModel);
                            }
                            tugasAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "gagal menampilkan semua tugas", Snackbar.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(request);
    }
}