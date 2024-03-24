package com.example.elearning_smkn_ngambon.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.elearning_smkn_ngambon.R;
import com.example.elearning_smkn_ngambon.fragment.ChatFragment;
import com.example.elearning_smkn_ngambon.fragment.DashboardFragment;
import com.example.elearning_smkn_ngambon.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;

    DashboardFragment dashboardFragment = new DashboardFragment();

    ProfileFragment profileFragment = new ProfileFragment();

    ChatFragment chatFragment = new ChatFragment();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);


        String tabToOpen = getIntent().getStringExtra("tab");
        if (tabToOpen != null && tabToOpen.equals("profile")) {
            // Open the Akun tab
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.menu_profile);
        } else if (tabToOpen != null && tabToOpen.equals("chat")) {
            // Open the Jadwal Panen tab
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, chatFragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.menu_chat);
        } else if (tabToOpen != null && tabToOpen.equals("dashboard")) {
            // Open the PerkembanganModel tab
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, dashboardFragment).commit();
            bottomNavigationView.setSelectedItemId(R.id.menu_dashboard);
        } else{
            // Set default fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, dashboardFragment).commit();

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId(); // Mendapatkan ID item yang dipilih

        if (itemId == R.id.menu_dashboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, dashboardFragment).commit();
            return true;
        } else if (itemId == R.id.menu_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, profileFragment).commit();
            return true;
        } else if (itemId == R.id.menu_chat) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, chatFragment).commit();
            return true;

        }
            return false;
    }
}