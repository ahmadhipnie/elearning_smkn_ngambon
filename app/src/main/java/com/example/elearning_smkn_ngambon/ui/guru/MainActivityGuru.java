package com.example.elearning_smkn_ngambon.ui.guru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.fragment.ChatFragment;
import com.example.elearning_smkn_ngambon.fragment.guruFragment.BerandaFragmentGuru;
import com.example.elearning_smkn_ngambon.fragment.guruFragment.ProfileFragmentGuru;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivityGuru extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationViewGuru;

    BerandaFragmentGuru berandaFragmentGuru = new BerandaFragmentGuru();

    ProfileFragmentGuru profileFragmentGuru = new ProfileFragmentGuru();

    ChatFragment chatFragmentGuru = new ChatFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guru);


        bottomNavigationViewGuru = findViewById(R.id.bottomNavigationViewGuru);
        bottomNavigationViewGuru.setOnItemSelectedListener(this);


        String tabToOpen = getIntent().getStringExtra("tab_guru");
        if (tabToOpen != null && tabToOpen.equals("profile_guru")) {
            // Open the profile guru tab
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentGuru, profileFragmentGuru).commit();
            bottomNavigationViewGuru.setSelectedItemId(R.id.menu_profile_guru);
        } else if (tabToOpen != null && tabToOpen.equals("chat_guru")) {
            // Open the Jadwal chat guru tab
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentGuru, chatFragmentGuru).commit();
            bottomNavigationViewGuru.setSelectedItemId(R.id.menu_chat_guru);
        } else if (tabToOpen != null && tabToOpen.equals("beranda_guru")) {
            // Open the beranda guru tab
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentGuru, berandaFragmentGuru).commit();
            bottomNavigationViewGuru.setSelectedItemId(R.id.menu_dashboard_guru);
        } else{
            // Set default fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentGuru, berandaFragmentGuru).commit();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId(); // Mendapatkan ID item yang dipilih

        if (itemId == R.id.menu_dashboard_guru) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentGuru, berandaFragmentGuru).commit();
            return true;
        } else if (itemId == R.id.menu_profile_guru) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentGuru, profileFragmentGuru).commit();
            return true;
        } else if (itemId == R.id.menu_chat_guru) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragmentGuru, chatFragmentGuru).commit();
            return true;

        }
        return false;
    }
}