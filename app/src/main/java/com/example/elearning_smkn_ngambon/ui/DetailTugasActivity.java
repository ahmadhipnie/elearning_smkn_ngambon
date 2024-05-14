package com.example.elearning_smkn_ngambon.ui;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
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
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DetailTugasActivity extends AppCompatActivity {

    private ImageView btnKembali, imageView;

    private Button btnPilihFile, btnUploadTugas;

    private TextView tvIdTugas, tvNamaMateri, tvTenggatWaktu, tvKeterangan;

    String encodedFile;

    int idTugas, idSiswa;

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

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        idSiswa = sharedPreferences.getInt("nisn", 0);

        Intent dariRecycleView = getIntent();
        idTugas = dariRecycleView.getIntExtra("id_tugas", 0);
        String namaMateri = dariRecycleView.getStringExtra("nama_materi");
        String tenggatWaktu = dariRecycleView.getStringExtra("tenggat_waktu");
        String keterangan = dariRecycleView.getStringExtra("keterangan");

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


    }

    private void uploadFile(String encodedFile) {
        // Inisialisasi RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Buat StringRequest untuk melakukan POST request ke API Laravel
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiconfig.UPLOAD_TUGAS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Tanggapan dari server jika upload berhasil
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = jsonObject.getString("message");
                            Toast.makeText(DetailTugasActivity.this, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTugasActivity.this, "Terjadi kesalahan dalam tanggapan server", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Tanggapan dari server jika terjadi kesalahan
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            String errorMessage = new String(error.networkResponse.data);
                            try {
                                JSONObject jsonObject = new JSONObject(errorMessage);
                                String message = jsonObject.getString("message");
                                Toast.makeText(DetailTugasActivity.this, message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(DetailTugasActivity.this, "Terjadi kesalahan saat mengupload file", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(DetailTugasActivity.this, "Terjadi kesalahan saat mengupload file", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // Mengirim data ke server, sesuaikan dengan nama parameter yang digunakan di API Laravel
                Map<String, String> params = new HashMap<>();
                params.put("file_tugas", encodedFile);
                params.put("id_tugas", String.valueOf(idTugas)); // Ganti dengan nilai id_tugas yang sesuai
                params.put("id_siswa", String.valueOf(idSiswa)); // Ganti dengan nilai id_siswa yang sesuai
                return params;
            }
        };

        // Tambahkan request ke RequestQueue
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
            } else if (mimeType != null && (mimeType.equals("application/msword"))) {
                checkFileSize(filePath);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_word));
            } else {
                Toast.makeText(this, "File harus berupa PDF atau Word", Toast.LENGTH_SHORT).show();
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