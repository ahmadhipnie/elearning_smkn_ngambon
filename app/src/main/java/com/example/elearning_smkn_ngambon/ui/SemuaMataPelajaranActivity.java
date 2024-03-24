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
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.example.elearning_smkn_ngambon.model.MataPelajaranModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SemuaMataPelajaranActivity extends AppCompatActivity {

    private ImageView btnKembali;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private List<MataPelajaranModel> mataPelajaranList;


    private MataPelajaranAdapter mataPelajaranAdapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_mata_pelajaran);

        recyclerView = findViewById(R.id.rv_semua_mapel);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = this.getSharedPreferences("UserData", Context.MODE_PRIVATE);

        mataPelajaranList = new ArrayList<>();
        mataPelajaranAdapter = new MataPelajaranAdapter(this, mataPelajaranList);

        recyclerView.setAdapter(mataPelajaranAdapter);


        btnKembali = findViewById(R.id.btnKembali);
        // Menambahkan onClickListener untuk tombol kembali
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SemuaMataPelajaranActivity.this, MainActivity.class);
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

        showRecycleViewMapel();
    }

    private void showRecycleViewMapel() {
        int id_kelas = sharedPreferences.getInt("id_kelas", 0);
        String url = apiconfig.MATA_PELAJARAN + "?id_kelas=" + id_kelas;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject orderObj = jsonArray.getJSONObject(i);
                                int idMapel = orderObj.getInt("id_mapel");
                                String namaMapel = orderObj.getString("nama_mapel");
                                String namaKelas = orderObj.getString("nama_kelas");
                                MataPelajaranModel mataPelajaranModel = new MataPelajaranModel(idMapel, namaMapel, namaKelas);
                                mataPelajaranList.add(mataPelajaranModel);
                            }
                            mataPelajaranAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "gagal menampilkan semua mata pelajaran", Snackbar.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SemuaMataPelajaranActivity.this, MainActivity.class);
        intent.putExtra("tab", "dashboard");
        startActivity(intent);
        finish();

        super.onBackPressed(); // Kembali ke activity sebelumnya
    }
}