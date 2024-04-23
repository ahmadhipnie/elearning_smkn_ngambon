package com.example.elearning_smkn_ngambon.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.api.apiconfig;
import com.example.elearning_smkn_ngambon.ui.guru.LoginActivityGuru;
import com.example.elearning_smkn_ngambon.ui.guru.MainActivityGuru;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    TextView tvloginGuru;

    EditText etNisn, etPassword;

    private boolean passwordVisible;
    private SharedPreferences sharedPreferences;
    private final String url = apiconfig.LOGIN_ENDPOINT;

    public String str_nisn;
    public String str_password;

    private ProgressDialog progressDialog;

    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btn_login);
        etNisn = findViewById(R.id.et_nisn);
        etPassword = findViewById(R.id.et_password);
        tvloginGuru = findViewById(R.id.tv_login_guru);

        tvloginGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentKeLoginGuru = new Intent(LoginActivity.this, LoginActivityGuru.class);
                startActivity(intentKeLoginGuru);
            }
        });

        etPassword.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int inType = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= etPassword.getRight() - etPassword.getCompoundDrawables()[inType].getBounds().width()) {
                        int selection = etPassword.getSelectionEnd();
                        if (passwordVisible) {
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        } else {
                            etPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;

                        }
                        etPassword.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }

        });

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        checkLoginStatus();
        checkLoginStatusGuru();


    }



    public void Login(View view) {

        if(etNisn.getText().toString().isEmpty()){
            Toast.makeText(this, "Masukkan nisn anda", Toast.LENGTH_SHORT).show();
        }
        else if(etPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Masukkan Password", Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Sedang Memuat data..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            str_nisn = etNisn.getText().toString().trim();
            str_password = etPassword.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            JSONObject userObject = jsonObject.getJSONObject("siswa");
                            int nisn = userObject.getInt("nisn");
                            String namaSiswa = userObject.getString("nama_siswa");
                            String tempatLahir = userObject.getString("tempat_lahir");
                            String tanggalLahir = userObject.getString("tanggal_lahir");
                            String password = userObject.getString("password");
                            String nomorHp = userObject.getString("nomor_hp");
                            int idKelas = userObject.getInt("id_kelas");
                            String namaKelas = userObject.getString("nama_kelas");

                            sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            // Simpan data ke SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("nisn", nisn);
                            editor.putString("nama_siswa", namaSiswa);
                            editor.putString("tempat_lahir", tempatLahir);
                            editor.putString("tanggal_lahir", tanggalLahir);
                            editor.putString("password", password);
                            editor.putString("alamat", password);
                            editor.putString("nomor_hp", nomorHp);
                            editor.putInt("id_kelas", idKelas);
                            editor.putString("nama_kelas", namaKelas);
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();

                            etNisn.setText("");
                            etPassword.setText("");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Terjadi kesalahan. Periksa koneksi internet Anda.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onErrorResponse: ", error );
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nisn", str_nisn);
                    params.put("password", str_password);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(request);
        }
    }


    private void checkLoginStatus() {
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // Pengguna sudah login sebelumnya, arahkan ke halaman beranda
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void checkLoginStatusGuru() {
        boolean isLoggedInGuru = sharedPreferences.getBoolean("isLoggedInGuru", false);
        if (isLoggedInGuru) {
            // Pengguna sudah login sebelumnya, arahkan ke halaman beranda
            Intent intent = new Intent(LoginActivity.this, MainActivityGuru.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // Menutup aplikasi saat tombol kembali ditekan
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        super.onBackPressed();
    }
}