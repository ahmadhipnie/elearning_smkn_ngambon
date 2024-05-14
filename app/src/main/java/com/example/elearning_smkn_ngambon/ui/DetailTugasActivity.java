package com.example.elearning_smkn_ngambon.ui;



import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.adapter.PengumpulanTugasAdapter;
import com.example.elearning_smkn_ngambon.adapter.guruFragment.TugasGuruAdapter;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.example.elearning_smkn_ngambon.model.PengumpulanTugasModel;
import com.example.elearning_smkn_ngambon.model.guruModel.TugasGuruModel;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailTugasActivity extends AppCompatActivity {

    private ImageView btnKembali, imageView;

    private Button btnPilihFile, btnUploadTugas;

    private TextView tvNamaMateri, tvKeterangan, tvStatusTugas;

    String encodedFile;

    int idTugas, idSiswa;

    RecyclerView rvPengumpulanTugas;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private List<PengumpulanTugasModel> pengumpulanTugasList;

    private PengumpulanTugasAdapter pengumpulanTugasAdapter;

    private static final int PICK_FILE_REQUEST = 1;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tugas);

        tvNamaMateri = findViewById(R.id.tv_judul_tugas);
        tvKeterangan = findViewById(R.id.tv_keterangan_tugas);
        btnPilihFile = findViewById(R.id.btn_pilih_file);
        btnUploadTugas = findViewById(R.id.btn_upload_tugas);
        imageView = findViewById(R.id.imView);
        tvStatusTugas = findViewById(R.id.tv_status_tugas);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mengupload tugas...");

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        idSiswa = sharedPreferences.getInt("nisn", 0);

        Intent dariRecycleView = getIntent();
        idTugas = dariRecycleView.getIntExtra("id_tugas", 0);
        String namaMateri = dariRecycleView.getStringExtra("nama_materi");
        String tenggatWaktu = dariRecycleView.getStringExtra("tenggat_waktu");
        String keterangan = dariRecycleView.getStringExtra("keterangan");

        Log.d(TAG, "onCreate: " + idSiswa +" "+ idTugas + " " + namaMateri + " " + tenggatWaktu + " " + keterangan);

        tvNamaMateri.setText("Tugas " + namaMateri);
        tvKeterangan.setText(keterangan + "\n\nTenggat Waktu: " + tenggatWaktu);

        btnKembali = findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(v -> {
            finish();
        });

        btnPilihFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(DetailTugasActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                chooseFile();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                Toast.makeText(DetailTugasActivity.this,
                                        "Izin akses penyimpanan ditolak",
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });

        btnUploadTugas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lakukan pengecekan apakah file sudah dipilih dan diencode
                if (encodedFile != null && !encodedFile.isEmpty()) {
                    uploadFile(encodedFile); // Panggil fungsi untuk mengupload file
                } else {
                    Toast.makeText(DetailTugasActivity.this, "Pilih file terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }
        });


        rvPengumpulanTugas = findViewById(R.id.rv_pengumpulan_tugas);
        rvPengumpulanTugas.setHasFixedSize(true);
        rvPengumpulanTugas.setLayoutManager(new LinearLayoutManager(this));


        pengumpulanTugasList = new ArrayList<>();
        pengumpulanTugasAdapter = new PengumpulanTugasAdapter(this, pengumpulanTugasList);
        rvPengumpulanTugas.setAdapter(pengumpulanTugasAdapter);



        // Inisialisasi ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sedang Memuat...");
        progressDialog.setCancelable(false);

        progressDialog.show();

        showRecycleViewPengumpulanTugas();
    }

    private void showRecycleViewPengumpulanTugas() {
        String url = apiconfig.TAMPIL_RIWAYAT_PENGUMPULAN_TUGAS + "?id_siswa=" + idSiswa + "&id_tugas=" + idTugas;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            pengumpulanTugasList.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                PengumpulanTugasModel pengumpulanTugas = new PengumpulanTugasModel(
                                        jsonObject.getInt("id_pengumpulan"),
                                        jsonObject.getString("lampiran_tugas"),
                                        jsonObject.getInt("id_tugas"),
                                        jsonObject.getInt("id_siswa"),
                                        jsonObject.getInt("nilai"),
                                        jsonObject.getString("created_at"),
                                        jsonObject.getString("updated_at")
                                );
                                pengumpulanTugasList.add(pengumpulanTugas);
                            }

                            pengumpulanTugasAdapter.notifyDataSetChanged();
                            tvStatusTugas.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handleNoDataFound();
                        }
                        progressDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "pengumpulan riwayat tugas tidak ada", Snackbar.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
        }
        requestQueue.add(request);
    }


    private void handleNoDataFound() {
        pengumpulanTugasList.clear();
        pengumpulanTugasAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
    }




    private void uploadFile(String encodedFile) {
        progressDialog.show();
        // Inisialisasi RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Buat StringRequest untuk melakukan POST request ke API Laravel
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiconfig.UPLOAD_TUGAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");

                            if (message.contains("File berhasil diupload dan data tugas ditambahkan")) {
                                Toast.makeText(DetailTugasActivity.this, message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                Intent intentKeMain = new Intent(DetailTugasActivity.this, MainActivity.class);
                                intentKeMain.putExtra("tab", "beranda");
                                startActivity(intentKeMain);
                            } else if (message.contains("Siswa sudah meng-upload tugas ini sebelumnya")) {
                                Toast.makeText(DetailTugasActivity.this, message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(DetailTugasActivity.this, "error", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTugasActivity.this, "Terjadi kesalahan saat mengupload file", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorMessage = new String(error.networkResponse.data);
                            try {
                                JSONObject jsonObject = new JSONObject(errorMessage);
                                String message = jsonObject.getString("message");
                                Toast.makeText(DetailTugasActivity.this, message, Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(DetailTugasActivity.this, "Terjadi kesalahan saat mengupload file", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        } else {
                            Toast.makeText(DetailTugasActivity.this, "Terjadi kesalahan saat mengupload file", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("file_tugas", encodedFile);
                params.put("id_tugas", String.valueOf(idTugas));
                params.put("id_siswa", String.valueOf(idSiswa));
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }


    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri filePath = data.getData();

            String mimeType = getContentResolver().getType(filePath);
            if (mimeType != null && (mimeType.equals("application/pdf"))) {
                checkFileSize(filePath);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf));
            } else {
                Toast.makeText(this, "File harus berupa PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkFileSize(Uri filePath) {
        try {
            Cursor cursor = getContentResolver().query(filePath, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                long size = cursor.getLong(sizeIndex);
                cursor.close();

                if (size <= 10 * 1024 * 1024) {
                    encodeFileToBase64(filePath);
                } else {
                    Toast.makeText(this, "Ukuran file tidak boleh melebihi 10MB", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void encodeFileToBase64(Uri filePath) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(filePath);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();

            // Encode file ke dalam Base64
            encodedFile = Base64.encodeToString(bytes, Base64.DEFAULT);

            // Lakukan pengiriman file ke server atau operasi lain yang diperlukan
            // Misalnya:
            // sendFileToServer(encodedFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}