package com.example.elearning_smkn_ngambon.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.ui.FaqActivity;
import com.example.elearning_smkn_ngambon.ui.KebijakanActivity;
import com.example.elearning_smkn_ngambon.ui.LoginActivity;
import com.example.elearning_smkn_ngambon.ui.UbahPasswordActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileFragment extends Fragment {


    SharedPreferences sharedPreferences;

    Button btnFaq, btnKebijakanAplikasi, btnUbahPassword, btnLogout;
    TextView tvNamaSiswa, tvNisn, tvTanggalLahir, tvNomorHp, tvAlamat, tvKelas;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvNamaSiswa = view.findViewById(R.id.tv_nama_siswa);
        tvNisn = view.findViewById(R.id.tv_nisn_siswa);
        tvTanggalLahir = view.findViewById(R.id.tv_tanggal_lahir);
        tvNomorHp = view.findViewById(R.id.tv_nomor_hp);
        tvAlamat = view.findViewById(R.id.tv_alamat);
        tvKelas = view.findViewById(R.id.tv_kelas);

        btnUbahPassword = view.findViewById(R.id.btn_ubah_password);
        btnKebijakanAplikasi = view.findViewById(R.id.btn_kebijakan_aplikasi);
        btnFaq = view.findViewById(R.id.btn_faq);
        btnLogout = view.findViewById(R.id.btn_logout);

        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String nama = sharedPreferences.getString("nama_siswa", "");
        int nisn = sharedPreferences.getInt("nisn", 0);
        String tempatLahir = sharedPreferences.getString("tempat_lahir", "");
        String tanggalLahir = sharedPreferences.getString("tanggal_lahir", "");
        String nomorHp = sharedPreferences.getString("nomor_hp", "");
        String alamat = sharedPreferences.getString("alamat", "");
        String kelas = sharedPreferences.getString("kelas", " - ");

        String nisnString = String.valueOf(nisn);

        tvNamaSiswa.setText(nama);
        tvNisn.setText(nisnString);
        tvTanggalLahir.setText(tempatLahir + ", " + formatDate(tanggalLahir));
        tvNomorHp.setText(nomorHp);
        tvAlamat.setText(alamat);
        tvKelas.setText(kelas);



        btnUbahPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ubahPasswordIntent = new Intent(requireActivity(), UbahPasswordActivity.class);
                startActivity(ubahPasswordIntent);
            }
        });

        btnKebijakanAplikasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kebijakanIntent = new Intent(requireActivity(), KebijakanActivity.class);
                startActivity(kebijakanIntent);
            }
        });

        btnFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent faqIntent = new Intent(requireActivity(), FaqActivity.class);
                startActivity(faqIntent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
            private void logoutUser() {
                // Menghapus nilai Shared Preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Navigasi ke halaman login atau halaman awal aplikasi
                // Gantikan "LoginActivity" dengan nama kelas LoginActivity jika menggunakan aktivitas, atau ganti dengan Fragment lain jika menggunakan Fragment
                startActivity(new Intent(requireActivity(), LoginActivity.class));
                requireActivity().finish();
            }
        }
        );



        return view;
    }

    private String formatDate(String dateString) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format sumber
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy"); // Format tujuan
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ""; // Mengembalikan string kosong jika gagal memformat tanggal
    }
}