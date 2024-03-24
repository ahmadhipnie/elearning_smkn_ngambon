package com.example.elearning_smkn_ngambon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.elearning_smkn_ngambon.R;

public class FaqActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    EditText etPertanyaanFaq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        TextView tvFaq = findViewById(R.id.tv_faq);
        tvFaq.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_back, 0, 0, 0);


        tvFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ketika tv_ubah_password ditekan, jalankan Intent
                Intent intent = new Intent(FaqActivity.this, MainActivity.class);
                intent.putExtra("tab", "profile");
                startActivity(intent);
                finish();
            }
        });

        etPertanyaanFaq = findViewById(R.id.etPertanyaanFaq);
        Button btnKirimWhatsApp = findViewById(R.id.btnKirimWhatsApp);
        btnKirimWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kirimPesanWhatsApp();
            }
        });

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
    }

    private void kirimPesanWhatsApp() {
        String pertanyaan = etPertanyaanFaq.getText().toString().trim();

        if (TextUtils.isEmpty(pertanyaan)) {
            // Validasi jika pertanyaan kosong
            etPertanyaanFaq.setError("Pertanyaan tidak boleh kosong");
            return;
        }

        // Nomor WhatsApp yang ingin dihubungi
        String nomorWhatsApp = "+6281333100497";

        // Data diri dari SharedPreferences
        String namaPengirim = sharedPreferences.getString("nama_siswa", "");
        int nisn = sharedPreferences.getInt("nisn", 0);

        String nisnString = String.valueOf(nisn);

        // Membuat pesan dengan data diri
        String pesan = "Pertanyaan dari: " + namaPengirim +
                "\nNISN : " + nisnString +
                "\nPertanyaan : " +
                "\n\n" + pertanyaan;

        // Membuat format URI untuk intent WhatsApp
        String url = "https://api.whatsapp.com/send?phone=" + nomorWhatsApp + "&text=" + Uri.encode(pesan);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}