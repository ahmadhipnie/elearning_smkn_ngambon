package com.example.elearning_smkn_ngambon.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.adapter.MateriAdapter;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.example.elearning_smkn_ngambon.model.MateriModel;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MateriActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private RecyclerView rvMateri;

    private TextView tvTitleMateri;

    private ImageView btnKembali;

    private MateriAdapter materiAdapter;

    private List<MateriModel> materiModelList;

    private RequestQueue requestQueue;

    private RecyclerView.ItemDecoration itemDecoration;

    private LinearLayoutManager linearLayoutManager;

    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materi);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        int idKelas = sharedPreferences.getInt("id_kelas", 0);

        btnKembali = findViewById(R.id.btnKembali);

        Intent intent = getIntent();
        int idMapel = intent.getIntExtra("id_mapel", 0);
        String namaMapel = intent.getStringExtra("nama_mapel");
        String namaKelas = intent.getStringExtra("nama_kelas");

        tvTitleMateri = findViewById(R.id.tv_title_materi);

        tvTitleMateri.setText(namaMapel);

        rvMateri = findViewById(R.id.rv_materi);
        linearLayoutManager = new LinearLayoutManager(this);
        rvMateri.setLayoutManager(linearLayoutManager);

        itemDecoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());

        rvMateri.addItemDecoration(itemDecoration);

        materiModelList = new ArrayList<>();
        materiAdapter = new MateriAdapter(this, materiModelList);

        rvMateri.setAdapter(materiAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showRecyclerViewMateri();

    }

    private void showRecyclerViewMateri() {
        int idKelas = sharedPreferences.getInt("id_kelas", 0);
        int idMapel = getIntent().getIntExtra("id_mapel", 0);

        String url = apiconfig.TAMPIL_MATERI + "?id_mapel=" + idMapel + "&id_kelas=" + idKelas;

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
                        MateriModel materiModel = new MateriModel(idMateri, judulMateri, keteranganMateri);
                        materiModelList.add(materiModel);
                    }
                    materiAdapter.notifyDataSetChanged();
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