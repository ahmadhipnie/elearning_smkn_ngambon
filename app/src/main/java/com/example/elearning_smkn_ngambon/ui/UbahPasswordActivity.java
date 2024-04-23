package com.example.elearning_smkn_ngambon.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.api.apiconfig;

import java.util.HashMap;
import java.util.Map;

public class UbahPasswordActivity extends AppCompatActivity {

    EditText etPasswordLama, etPasswordBaru, etKonfirmasiPasswordBaru;
    Button btnUbahPassword;


    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_password);

        TextView tvUbahPassword = findViewById(R.id.tv_faq);
        tvUbahPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_back, 0, 0, 0);


        etPasswordLama = findViewById(R.id.et_password_lama);
        etPasswordBaru = findViewById(R.id.et_password_baru);
        etKonfirmasiPasswordBaru = findViewById(R.id.et_konfirmasi_password_baru);
        btnUbahPassword = findViewById(R.id.btn_ubah_password);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        
        btnUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahPassword();
            }
        });


        tvUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ketika tv_ubah_password ditekan, jalankan Intent
                Intent intent = new Intent(UbahPasswordActivity.this, MainActivity.class);
                intent.putExtra("tab", "profile");
                startActivity(intent);
                finish();
            }
        });
    }

    private void ubahPassword() {
        int userId = sharedPreferences.getInt("nisn", 0);
        String password = sharedPreferences.getString("password", "");
        String passwordLama = etPasswordLama.getText().toString().trim();
        String passwordBaru = etPasswordBaru.getText().toString().trim();
        String konfirmasiPasswordBaru = etKonfirmasiPasswordBaru.getText().toString().trim();

        if (TextUtils.isEmpty(passwordLama) || TextUtils.isEmpty(passwordBaru) || TextUtils.isEmpty(konfirmasiPasswordBaru)) {
            Toast.makeText(this, "Semua kolom password harus diisi", Toast.LENGTH_SHORT).show();

        } else if (!passwordBaru.equals(konfirmasiPasswordBaru)) {
            Toast.makeText(this, "konfirmasi password tidak cocok", Toast.LENGTH_LONG).show();

        } else if (!passwordLama.equals(password)) {
            Toast.makeText(this, "Password lama tidak cocok", Toast.LENGTH_SHORT).show();

        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Apakah Anda yakin ingin mengubah password?");
            alertDialogBuilder.setPositiveButton("Ya", (dialogInterface, i) -> {
                // Proses untuk mengubah password melalui API Laravel

                // Tampilkan ProgressDialog
                ProgressDialog progressDialog = new ProgressDialog(UbahPasswordActivity.this);
                progressDialog.setMessage("Mengubah password...");
                progressDialog.show();

                String url = apiconfig.UBAH_PASSWORD_ENDPOINT;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                // Tutup ProgressDialog
                                progressDialog.dismiss();

                                // Tanggapan dari API setelah berhasil mengubah password
                                Toast.makeText(UbahPasswordActivity.this, "Password berhasil diubah", Toast.LENGTH_SHORT).show();

                                // Hapus SharedPreferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();

                                // Navigasi kembali ke LoginActivity
                                Intent intent = new Intent(UbahPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finishAffinity(); // Menutup semua activity sebelumnya
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Tutup ProgressDialog
                                progressDialog.dismiss();

                                // Tanggapan dari API jika terjadi kesalahan
                                Toast.makeText(UbahPasswordActivity.this, "Gagal mengubah password", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        // Parameter yang akan dikirim ke API (ID user, password lama, password baru)
                        Map<String, String> params = new HashMap<>();
                        params.put("nisn", String.valueOf(userId));
                        params.put("old_password", passwordLama);
                        params.put("new_password", passwordBaru);
                        return params;
                    }
                };

                // Menambahkan request ke queue
                RequestQueue requestQueue = Volley.newRequestQueue(UbahPasswordActivity.this);
                requestQueue.add(stringRequest);
            });

            alertDialogBuilder.setNegativeButton("Tidak", (dialogInterface, i) -> {
                // Batal mengubah password
                dialogInterface.dismiss();
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("tab", "profile");
        startActivity(intent);
        finish();

        super.onBackPressed(); // Kembali ke activity sebelumnya
    }
}