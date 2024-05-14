package com.example.elearning_smkn_ngambon.ui.guru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.adapter.guruFragment.MateriGuruAdapter;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.example.elearning_smkn_ngambon.model.MateriModel;
import com.example.elearning_smkn_ngambon.model.guruModel.MateriGuruModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MateriGuruActivity extends AppCompatActivity {


    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    private TextView tvTitleMateri;
    RecyclerView rvMateriGuru;
    private ImageView btnKembali;

    private MateriGuruAdapter materiGuruAdapter;

    private List<MateriGuruModel> materiGuruModelList;

    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi_guru);

        sharedPreferences = this.getSharedPreferences("UserData", Context.MODE_PRIVATE);

        rvMateriGuru = findViewById(R.id.rv_materi_guru);
        rvMateriGuru.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        int idMapel = intent.getIntExtra("id_mapel", 0);
        String namaMapel = intent.getStringExtra("nama_mapel");
        String namaKelas = intent.getStringExtra("nama_kelas");
        int idKelas = intent.getIntExtra("id_kelas", 0);

        tvTitleMateri = findViewById(R.id.tv_judul_mapel_guru);

        tvTitleMateri.setText(namaMapel);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        materiGuruModelList = new ArrayList<>();
        materiGuruAdapter = new MateriGuruAdapter(this, materiGuruModelList);

        rvMateriGuru.setAdapter(materiGuruAdapter);

        btnKembali = findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(v -> finish());

        showRecyclerViewMateriGuru();


    }

    private void showRecyclerViewMateriGuru() {
       int idMapel = getIntent().getIntExtra("id_mapel", 0);

        String url = apiconfig.MATERI_GURU_ENDPOINT + "?id_mapel=" + idMapel;

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject orderObj = jsonArray.getJSONObject(i);
                        int idMateri = orderObj.getInt("id_materi");
                        String judulMateri = orderObj.getString("judul_materi");
                        String keteranganMateri = orderObj.getString("keterangan");
                        MateriGuruModel materiGuruModel = new MateriGuruModel(idMateri, judulMateri, keteranganMateri);
                        materiGuruModelList.add(materiGuruModel);
                    }
                    materiGuruAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Tidak dapat menampilkan materi", Snackbar.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(request);
    }


}