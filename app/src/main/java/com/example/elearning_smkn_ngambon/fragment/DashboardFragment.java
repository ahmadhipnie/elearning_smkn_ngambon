package com.example.elearning_smkn_ngambon.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.example.elearning_smkn_ngambon.ui.SemuaMataPelajaranActivity;
import com.example.elearning_smkn_ngambon.ui.SemuaTugasActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment {

    TextView tvNamaSiswa, btnLihatSemuaMapel, btnLihatSemuaTugas;
    private RecyclerView rvMataPelajaran, rvTugas;
    private SharedPreferences sharedPreferences;
    private MataPelajaranAdapter mataPelajaranAdapter;
    private TugasAdapter tugasAdapter;
    private List<MataPelajaranModel> mataPelajaranList;
    private List<TugasModel> tugasList;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvNamaSiswa = view.findViewById(R.id.tv_nama_siswa);
        btnLihatSemuaMapel = view.findViewById(R.id.btn_lihat_semua_mapel);
        btnLihatSemuaTugas = view.findViewById(R.id.btn_lihat_semua_tugas);
        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama_siswa", "");
        tvNamaSiswa.setText(nama);

        rvMataPelajaran = view.findViewById(R.id.rv_mapel);
        rvMataPelajaran.setLayoutManager(new LinearLayoutManager(requireContext()));


        rvTugas = view.findViewById(R.id.rv_tugas);
        rvTugas.setLayoutManager(new LinearLayoutManager(requireContext()));

        mataPelajaranList = new ArrayList<>();
        mataPelajaranAdapter = new MataPelajaranAdapter(requireContext(), mataPelajaranList);

        tugasList = new ArrayList<>();
        tugasAdapter = new TugasAdapter(requireContext(), tugasList);

        rvTugas.setAdapter(tugasAdapter);
        rvMataPelajaran.setAdapter(mataPelajaranAdapter);

        btnLihatSemuaMapel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keSemuaMataPelajaran = new Intent(requireActivity(), SemuaMataPelajaranActivity.class);
                startActivity(keSemuaMataPelajaran);
            }
        });

        btnLihatSemuaTugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent keSemuaTugas = new Intent(requireActivity(), SemuaTugasActivity.class);
                startActivity(keSemuaTugas);
            }
        });

        // Inisialisasi ProgressDialog
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCancelable(false);

        progressDialog.show();

        showRecycleViewMapel();
        showRecycleViewTugas();

        return view;
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
                            for (int i = 0; i < Math.min(jsonArray.length(), 3); i++) {
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
                Snackbar.make(requireView(), "Terjadi kesalahan, coba restart aplikasi", Snackbar.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(requireContext());
        }
        requestQueue.add(request);
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
                            for (int i = 0; i < Math.min(jsonArray.length(), 3); i++) {
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
                Snackbar.make(requireView(), "Terjadi kesalahan, coba restart aplikasi", Snackbar.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(requireContext());
        }
        requestQueue.add(request);
    }


}
