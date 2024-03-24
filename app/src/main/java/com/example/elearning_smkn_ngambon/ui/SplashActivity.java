package com.example.elearning_smkn_ngambon.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.elearning_smkn_ngambon.R;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;

    Animation logoAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        logo = findViewById(R.id.logoSplash);

        logoAnim = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        logo.setAnimation(logoAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },1000);
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