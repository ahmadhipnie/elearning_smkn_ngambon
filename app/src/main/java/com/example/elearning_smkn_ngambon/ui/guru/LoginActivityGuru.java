package com.example.elearning_smkn_ngambon.ui.guru;

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
import com.example.elearning_smkn_ngambon.ui.LoginActivity;
import com.example.elearning_smkn_ngambon.ui.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivityGuru extends AppCompatActivity {

    TextView tvLoginMurid;

    EditText etNipGuru, etPasswordGuru;

    private boolean passwordVisibleGuru;
    Button btnLoginGuru;
    private String str_nip;
    private String str_password_guru;
    private SharedPreferences sharedPreferences;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_guru);

        tvLoginMurid = findViewById(R.id.tv_login_murid);
        etNipGuru = findViewById(R.id.et_nip_guru);
        etPasswordGuru = findViewById(R.id.et_password_guru);
        btnLoginGuru = findViewById(R.id.btn_login_guru);

        tvLoginMurid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentkeLoginMurid = new Intent(LoginActivityGuru.this, LoginActivity.class);
                startActivity(intentkeLoginMurid);
            }
        });



        etPasswordGuru.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                final int inType = 2;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= etPasswordGuru.getRight() - etPasswordGuru.getCompoundDrawables()[inType].getBounds().width()) {
                        int selection = etPasswordGuru.getSelectionEnd();
                        if (passwordVisibleGuru) {
                            etPasswordGuru.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                            etPasswordGuru.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisibleGuru = false;
                        } else {
                            etPasswordGuru.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                            etPasswordGuru.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisibleGuru = true;

                        }
                        etPasswordGuru.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }

        });

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        
        btnLoginGuru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginGuru();
            }
        });

    }

    public void loginGuru() {

        if(etNipGuru.getText().toString().isEmpty()){
            Toast.makeText(this, "Masukkan nip anda", Toast.LENGTH_SHORT).show();
        }
        else if(etPasswordGuru.getText().toString().isEmpty()){
            Toast.makeText(this, "Masukkan Password", Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Sedang Memuat data..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            str_nip = etNipGuru.getText().toString().trim();
            str_password_guru = etPasswordGuru.getText().toString().trim();

            StringRequest request = new StringRequest(Request.Method.POST, apiconfig.LOGIN_GURU_ENDPOINT, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    progressDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) {
                            JSONObject userObject = jsonObject.getJSONObject("guru");
                            int nip = userObject.getInt("nip");
                            String namaGuru = userObject.getString("nama_guru");
                            String password = userObject.getString("password");
                            String nomorHp = userObject.getString("nomor_hp");

                            sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
                            // Simpan data ke SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("nip", nip);
                            editor.putString("nama_guru", namaGuru);
                            editor.putString("alamat", password);
                            editor.putString("nomor_hp", nomorHp);
                            editor.putBoolean("isLoggedInGuru", true);
                            editor.apply();

                            etNipGuru.setText("");
                            etPasswordGuru.setText("");
                            Intent intent = new Intent(LoginActivityGuru.this, MainActivityGuru.class);
                            startActivity(intent);
                            Toast.makeText(LoginActivityGuru.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LoginActivityGuru.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivityGuru.this, "Terjadi kesalahan. Periksa koneksi internet Anda.", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "onErrorResponse: ", error );
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("nip", str_nip);
                    params.put("password", str_password_guru);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivityGuru.this);
            requestQueue.add(request);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}