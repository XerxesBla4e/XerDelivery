package com.example.xermart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xermart.Activity.Main;
import com.example.xermart.Login.Signup;
import com.example.xermart.User.AllCategories;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logn extends AppCompatActivity implements View.OnClickListener {

    private EditText etemail, etpassword;
    private String semail, spass;
    private Button btn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextView frgtpass, signup;
    private ImageView backbtn;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String shared_db = "log_data";
    private static final String email_key = "Email";
    private static final String pass_key = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logn);

        hooks();

        String e = sharedPreferences.getString(email_key, "");
        String p = sharedPreferences.getString(pass_key, "");

        etemail.setText(e);
        etpassword.setText(p);

        btn.setOnClickListener(this);
        frgtpass.setOnClickListener(this);
        signup.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        backbtn = findViewById(R.id.back_presssed);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logn.super.onBackPressed();
            }
        });
    }

    public void hooks() {
        etemail = findViewById(R.id.uemail);
        etpassword = findViewById(R.id.upass);
        btn = findViewById(R.id.button);
        progressBar = findViewById(R.id.progressB);
        frgtpass = findViewById(R.id.fgtps);
        signup = findViewById(R.id.sgnup1);
        sharedPreferences = getSharedPreferences(shared_db, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                userLogin();
                break;
            case R.id.sgnup1:
                startActivity(new Intent(Logn.this, Signup.class));
                break;
            case R.id.fgtps:
                startActivity(new Intent(Logn.this, forgotpass.class));
                break;
        }
    }

    private void userLogin() {
        String semail = etemail.getText().toString().trim();
        String spass = etpassword.getText().toString().trim();

        if (semail.isEmpty()) {
            etemail.setError("Email is required!");
            etemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(semail).matches()) {
            etemail.setError("Please Enter a valid Email!");
            etemail.requestFocus();
            return;
        }

        if (spass.isEmpty()) {
            etpassword.setError("Password is required!");
            etpassword.requestFocus();
            return;
        }

        if (spass.length() < 6) {
            etpassword.setError("Min password is 6 characters!");
            etpassword.requestFocus();
            return;
        }

        editor.putString(email_key, semail);
        editor.putString(pass_key, spass);
        editor.apply();

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(semail, spass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            if (user.isEmailVerified()) {
                                startActivity(new Intent(Logn.this, Main.class));
                                progressBar.setVisibility(View.GONE);
                            } else {
                                user.sendEmailVerification();
                                Toast.makeText(Logn.this, "Check Your Email To verify Account", Toast.LENGTH_LONG).show();
                            }

                        } else {
                            Toast.makeText(Logn.this, "Failed to Login! Please Check your credentials", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }
}