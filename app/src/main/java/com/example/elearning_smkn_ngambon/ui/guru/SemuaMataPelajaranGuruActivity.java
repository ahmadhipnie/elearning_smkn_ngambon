package com.example.elearning_smkn_ngambon.ui.guru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.adapter.guruFragment.MataPelajaranGuruAdapter;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.example.elearning_smkn_ngambon.model.guruModel.MataPelajaranGuruModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SemuaMataPelajaranGuruActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;

    ImageView btnKembali;
    RecyclerView rvMataPelajaranGuru;
    MataPelajaranGuruAdapter mataPelajaranGuruAdapter;
    List<MataPelajaranGuruModel> mataPelajaranGuruList;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semua_mata_pelajaran_guru);

        btnKembali = findViewById(R.id.btnKembali);

        btnKembali.setOnClickListener(v -> {
        finish();
        });

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        rvMataPelajaranGuru = findViewById(R.id.rv_semua_mapel_guru);

        mataPelajaranGuruList = new ArrayList<>();

        mataPelajaranGuruAdapter = new MataPelajaranGuruAdapter(this, mataPelajaranGuruList);

        rvMataPelajaranGuru.setAdapter(mataPelajaranGuruAdapter);

        rvMataPelajaranGuru.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();




        Intent getIntent = getIntent();
        int idGuru = getIntent.getIntExtra("id_guru", 0);

        Log.d("id_guru", String.valueOf(idGuru));

        showRecycleViewMapelGuru();

    }

    private void showRecycleViewMapelGuru() {
        int nip = sharedPreferences.getInt("nip", 0);
        String url = apiconfig.MATA_PELAJARAN_GURU_ENDPOINT + "?id_guru=" + nip;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int idMapel = jsonObject.getInt("id_mapel");
                                String namaMapel = jsonObject.getString("nama_mapel");
                                String namaKelas = jsonObject.getString("nama_kelas");
                                int idKelas = jsonObject.getInt("id_kelas");
                                MataPelajaranGuruModel mataPelajaranGuruModel = new MataPelajaranGuruModel(idMapel, namaMapel, namaKelas, idKelas);
                                mataPelajaranGuruList.add(mataPelajaranGuruModel);
                            }
                            mataPelajaranGuruAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(findViewById(android.R.id.content), "Terjadi kesalahan", Snackbar.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Gagal mengambil data", Snackbar.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(request);
    }
}