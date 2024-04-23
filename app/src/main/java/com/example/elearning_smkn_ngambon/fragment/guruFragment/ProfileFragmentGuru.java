package com.example.elearning_smkn_ngambon.fragment.guruFragment;

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
import com.example.elearning_smkn_ngambon.ui.guru.LoginActivityGuru;
import com.example.elearning_smkn_ngambon.ui.guru.UbahPasswordActivityGuru;


public class ProfileFragmentGuru extends Fragment {

    private TextView tvNamaGuru, tvNipGuru, tvNomorHpGuru;
    private Button btnUbahPasswordGuru, btnLogoutGuru, btnKebijakanAplikasiGuru, btnFaqGuru;
    private SharedPreferences sharedPreferences;

   @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_profile_guru, container, false);

        tvNamaGuru = view.findViewById(R.id.tv_nama_guru);
        tvNipGuru = view.findViewById(R.id.tv_nip_guru);
        tvNomorHpGuru = view.findViewById(R.id.tv_nomor_hp_guru);

        btnUbahPasswordGuru = view.findViewById(R.id.btn_ubah_password_guru);
        btnLogoutGuru = view.findViewById(R.id.btn_logout_guru);
        btnKebijakanAplikasiGuru = view.findViewById(R.id.btn_kebijakan_aplikasi_guru);
        btnFaqGuru = view.findViewById(R.id.btn_faq_guru);

        sharedPreferences = getActivity().getSharedPreferences("UserData", getContext().MODE_PRIVATE);
        tvNamaGuru.setText(sharedPreferences.getString("nama_guru", " - "));
        tvNipGuru.setText(String.valueOf(sharedPreferences.getInt("nip", 0)));
        tvNomorHpGuru.setText(sharedPreferences.getString("nomor_hp", " - "));

           btnKebijakanAplikasiGuru.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent kebijakanIntent = new Intent(requireActivity(), KebijakanActivity.class);
                   startActivity(kebijakanIntent);
               }
           });

           btnFaqGuru.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent keFaqGuru = new Intent(getActivity(), FaqActivity.class);
                   startActivity(keFaqGuru);
               }
           });
           btnUbahPasswordGuru.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent keUbahPasswordGuru = new Intent(getActivity(), UbahPasswordActivityGuru.class);
                   startActivity(keUbahPasswordGuru);
               }
           });
           btnLogoutGuru.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.clear();
                   editor.apply();
                   startActivity(new Intent(getActivity(), LoginActivity.class));
                   getActivity().finish();
               }
           });
        return view;
        }
    }