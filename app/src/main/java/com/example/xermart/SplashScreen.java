package com.example.xermart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.xermart.Login.Signup;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIMER = 5000;
    TextView appname;
    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        appname = findViewById(R.id.appnme);
        lottie = findViewById(R.id.lottie);

        appname.animate().translationY(-800).setDuration(2700).setStartDelay(0);
        lottie.animate().translationX(2000).setDuration(2000).setStartDelay(2900);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              Intent intent = new Intent(SplashScreen.this, Logn.class);
              startActivity(intent);
              finish();
            }
        },SPLASH_TIMER );
    }
}