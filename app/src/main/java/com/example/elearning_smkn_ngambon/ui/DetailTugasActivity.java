package com.example.elearning_smkn_ngambon.ui;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.android.volley.toolbox.StringRequest;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.api.apiconfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DetailTugasActivity extends AppCompatActivity {

    private ImageView btnKembali;

    private Button btnPilihFile, btnUploadTugas;

    private TextView tvIdTugas, tvNamaMateri, tvTenggatWaktu, tvKeterangan;

    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tugas);

        tvNamaMateri = findViewById(R.id.tv_judul_tugas);
        tvKeterangan = findViewById(R.id.tv_keterangan_tugas);
        btnPilihFile = findViewById(R.id.btn_pilih_file);
        btnUploadTugas = findViewById(R.id.btn_upload_tugas);

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        int idSiswa = sharedPreferences.getInt("nisn", 0);

        Intent dariRecycleView = getIntent();
        int idTugas = dariRecycleView.getIntExtra("id_tugas", 0);
        String namaMateri = dariRecycleView.getStringExtra("nama_materi");
        String tenggatWaktu = dariRecycleView.getStringExtra("tenggat_waktu");
        String keterangan = dariRecycleView.getStringExtra("keterangan");

        tvNamaMateri.setText("Tugas " + namaMateri);
        tvKeterangan.setText(keterangan + "\n\nTenggat Waktu: " + tenggatWaktu);

        btnKembali = findViewById(R.id.btnKembali);
        btnKembali.setOnClickListener(v -> {
            finish();
        });

        btnPilihFile.setOnClickListener(v -> {
            Intent pilihFile = new Intent(Intent.ACTION_GET_CONTENT);
            pilihFile.addCategory(Intent.CATEGORY_OPENABLE);
            pilihFile.setType("*/*");
            startActivityForResult(pilihFile, 1);
        });

        btnUploadTugas.setOnClickListener(v -> {
            uploadTugas(idTugas, idSiswa);
        });

    }

    private void uploadTugas(int idTugas, int idSiswa) {
        if (selectedFileUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                byte[] fileBytes = new byte[inputStream.available()];
                inputStream.read(fileBytes);
                inputStream.close();

                String encodedFile = new String(fileBytes);

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, apiconfig.UPLOAD_FILE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                Toast.makeText(DetailTugasActivity.this, "Tugas berhasil diupload", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(DetailTugasActivity.this, "Gagal mengupload tugas", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DetailTugasActivity.this, "Gagal mengupload tugas", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(DetailTugasActivity.this, "Gagal mengupload tugas", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_tugas", String.valueOf(idTugas));
                        params.put("id_siswa", String.valueOf(idSiswa));
                        params.put("file", encodedFile);
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal mengupload tugas", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Pilih file terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
    }

    Uri selectedFileUri; // Untuk menyimpan URI file yang dipilih

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data != null && data.getData() != null) {
                selectedFileUri = data.getData();
                long fileSize = getFileSize(selectedFileUri);
                if (fileSize > 10 * 1024 * 1024) { // 10 MB dalam byte
                    Toast.makeText(this, "File terlalu besar (lebih dari 10 MB)", Toast.LENGTH_SHORT).show();
                } else {
                    String fileType = getMimeType(selectedFileUri);
                    if (fileType != null && (fileType.equals("application/pdf")
                            || fileType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
                            || fileType.equals("application/msword"))) {
                        Toast.makeText(this, "File yang dipilih adalah: " + selectedFileUri.toString(), Toast.LENGTH_SHORT).show();

                        saveFile(selectedFileUri);
                    } else {
                        Toast.makeText(this, "Format file tidak didukung", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void saveFile(Uri selectedFileUri) {
        try {
            String fileName = getFileName(selectedFileUri); // Ambil nama file dari URI
            FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE); // Gunakan nama file untuk penyimpanan
            InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Tutup stream
            outputStream.close();
            inputStream.close();

            Toast.makeText(this, "File berhasil disimpan", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Gagal menyimpan file", Toast.LENGTH_SHORT).show();
        }
    }

    private long getFileSize(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
        cursor.moveToFirst();
        long size = cursor.getLong(sizeIndex);
        cursor.close();
        return size;
    }

    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }
    private String getFileName(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String name = cursor.getString(nameIndex);
        cursor.close();
        return name;
    }
}