package com.example.elearning_smkn_ngambon.fragment.guruFragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.adapter.guruFragment.MataPelajaranGuruAdapter;
import com.example.elearning_smkn_ngambon.adapter.guruFragment.PengumpulanTugasGuruAdapter;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.example.elearning_smkn_ngambon.model.guruModel.MataPelajaranGuruModel;
import com.example.elearning_smkn_ngambon.model.guruModel.PengumpulanTugasGuruModel;
import com.example.elearning_smkn_ngambon.ui.guru.SemuaMataPelajaranGuruActivity;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BerandaFragmentGuru extends Fragment {

    TextView tvNamaGuru, tvLihatSemuaMapelGuru;
    RecyclerView rvMataPelajaranGuru, rvPengumpulanTugasGuru;

    MataPelajaranGuruAdapter mataPelajaranGuruAdapter;
    PengumpulanTugasGuruAdapter pengumpulanTugasGuruAdapter;


    List<MataPelajaranGuruModel> mataPelajaranGuruList;
    List<PengumpulanTugasGuruModel> pengumpulanTugasGuruList;


    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda_guru, container, false);

        tvNamaGuru = view.findViewById(R.id.tv_nama_guru);
        tvLihatSemuaMapelGuru = view.findViewById(R.id.btn_lihat_semua_mapel_guru);


        rvMataPelajaranGuru = view.findViewById(R.id.rv_mapel_guru);
        rvMataPelajaranGuru.setLayoutManager(new LinearLayoutManager(requireContext()));

        rvPengumpulanTugasGuru = view.findViewById(R.id.rv_pengumpulan_tugas_guru);
        rvPengumpulanTugasGuru.setLayoutManager(new LinearLayoutManager(requireContext()));



        sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        tvNamaGuru.setText(sharedPreferences.getString("nama_guru", ""));

        mataPelajaranGuruList = new ArrayList<>();
        mataPelajaranGuruAdapter = new MataPelajaranGuruAdapter(requireContext(), mataPelajaranGuruList);
        rvMataPelajaranGuru.setAdapter(mataPelajaranGuruAdapter);


        pengumpulanTugasGuruList = new ArrayList<>();
        pengumpulanTugasGuruAdapter = new PengumpulanTugasGuruAdapter(requireContext(), pengumpulanTugasGuruList);
        rvPengumpulanTugasGuru.setAdapter(pengumpulanTugasGuruAdapter);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Harap tunggu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        tvLihatSemuaMapelGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentKeSemuaMapelGuru = new Intent(requireContext(), SemuaMataPelajaranGuruActivity.class);
                intentKeSemuaMapelGuru.putExtra("id_guru", sharedPreferences.getInt("nip", 0));
                startActivity(intentKeSemuaMapelGuru);

            }
        });

        showRecycleViewMapelGuru();

        showRecycleViewPengumpulanTugasGuru();

        return view;
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
                            for (int i = 0; i < Math.min(jsonArray.length(), 3); i++) {
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
                            Snackbar.make(requireView(), "Terjadi kesalahan", Snackbar.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Snackbar.make(requireView(), "Gagal mengambil data", Snackbar.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(requireContext());
        }
        requestQueue.add(request);
    }

    private void showRecycleViewPengumpulanTugasGuru() {
        int nip = sharedPreferences.getInt("nip", 0);
        String url = apiconfig.TAMPIL_PENGUMPULAN_TUGAS_GURU_ENDPOINT + "?id_guru=" + nip;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < Math.min(jsonArray.length(), 3); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                int idPengumpulan = jsonObject.getInt("id_pengumpulan");
                                String lampiranTugas = jsonObject.getString("lampiran_tugas");
                                int idTugas = jsonObject.getInt("id_tugas");
                                int idSiswa = jsonObject.getInt("id_siswa");
                                int nilai = jsonObject.getInt("nilai");
                                String createdAt = jsonObject.getString("created_at");
                                String updatedAt = jsonObject.getString("updated_at");
                                String namaSiswa = jsonObject.getString("nama_siswa");
                                String judulMateri = jsonObject.getString("judul_materi");
                                PengumpulanTugasGuruModel pengumpulanTugasGuruModel = new PengumpulanTugasGuruModel(idPengumpulan, lampiranTugas, idTugas, idSiswa, nilai, createdAt, updatedAt, namaSiswa, judulMateri);
                                pengumpulanTugasGuruList.add(pengumpulanTugasGuruModel);
                            }
                            pengumpulanTugasGuruAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Snackbar.make(requireView(), "Terjadi kesalahan", Snackbar.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Snackbar.make(requireView(), "Gagal mengambil data", Snackbar.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(requireContext());
        }
        requestQueue.add(request);
    }
}
