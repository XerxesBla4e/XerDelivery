package com.example.xermart;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Typeface tf = Typeface.createFromAsset(getAssets(),"fonts/Railway-Italic-Variable.ttf");
        TextView hello = findViewById(R.id.hello);
        hello.setTypeface(tf);
    }
}