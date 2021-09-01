package com.example.xermart.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.xermart.Activity.Main;
import com.example.xermart.Logn;
import com.example.xermart.R;
import com.example.xermart.Users;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Signup extends AppCompatActivity {
    static final int IMG_PICKED_CODE = 11;


    ImageView img;
    Button btn;
    Uri image;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private EditText etemail, etname, etpassword, etrepassword;
    private String email, name, password, repassword;
    private ProgressBar progressBar;
    private  ImageView backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        hooks();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signup.super.onBackPressed();
            }
        });
        mAuth = FirebaseAuth.getInstance();

    }

    public void hooks() {
        etemail = findViewById(R.id.logem);
        etname = findViewById(R.id.signname);
        etpassword = findViewById(R.id.logpass);
        etrepassword = findViewById(R.id.repass);
        progressBar = findViewById(R.id.progressBar2);
        btn = findViewById(R.id.btnimg);
        img = findViewById(R.id.imageView2);
        backbtn = findViewById(R.id.back_presssed);
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        storageReference = FirebaseStorage.getInstance().getReference().child("UserImages");
    }


    private boolean validateFields() {
        String email = etemail.getText().toString().trim();
        String name = etname.getText().toString().trim();
        String password = etpassword.getText().toString().trim();
        String repassword = etrepassword.getText().toString().trim();
        ProgressDialog dialog = new ProgressDialog(Signup.this);

        if (email.isEmpty()) {
            etemail.setError("Email Cant be empty");
            etemail.requestFocus();
            return false;
        } else if (name.isEmpty()) {
            etname.setError("Name Field Cant be empty");
            etname.requestFocus();
            return false;
        } else if (password.isEmpty()) {
            etpassword.setError("Password Field Cant be empty");
            etpassword.requestFocus();
            return false;
        } else if (repassword.isEmpty()) {
            etrepassword.setError("RePassword Field Cant be empty");
            etrepassword.requestFocus();
            return false;
        } else if (!password.equals(repassword)) {
            etrepassword.setError("Passwords Dont Match");
            etrepassword.requestFocus();
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etemail.setError("Email is invalid");
            etemail.requestFocus();
            return false;
        }else if (password.length() < 6) {
            etpassword.setError("Min password is 6 characters!");
            etpassword.requestFocus();
            return false;
        }
        return true;
    }

    public void signup(View view) {

        if (!validateFields()) {
            return;
        }

        String email = etemail.getText().toString().trim();
        String name = etname.getText().toString().trim();
        String password = etpassword.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Users users = new Users(name, email);

                        databaseReference
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(Signup.this, Logn.class);
                                    startActivity(intent);
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    Toast.makeText(Signup.this, "Registeration Failed", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
                    }
                });

    }

    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Profile"), IMG_PICKED_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMG_PICKED_CODE && resultCode == RESULT_OK && data.getData() != null) {
            image = data.getData();
            img.setImageURI(image);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void HaveAccnt(View view) {
        startActivity(new Intent(Signup.this, Logn.class));
    }
}