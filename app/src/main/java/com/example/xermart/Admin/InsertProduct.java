package com.example.xermart.Admin;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.xermart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class InsertProduct extends AppCompatActivity {

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;
    ImageButton imageButton;
    EditText edtitem, edtprice;
    Button btnadd;
    private static int GALLERY_CODE = 988;
    Uri imageUri = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_product);

        hooks();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Item Image"), GALLERY_CODE);
            }
        });
    }

    private void hooks() {
        imageButton = findViewById(R.id.imageButton);
        edtitem = findViewById(R.id.itemname);
        edtprice = findViewById(R.id.price);
        btnadd = findViewById(R.id.btnadd);
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference().child("Item");
        mStorage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && data.getData() != null) {
            imageUri = data.getData();
            imageButton.setImageURI(imageUri);
        }
        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itmname = edtitem.getText().toString();
                String itprice = edtprice.getText().toString();

                if (!(itmname.isEmpty() && itprice.isEmpty())) {
                    progressDialog.setTitle("Uploading.......");
                    progressDialog.show();

                    StorageReference filepath = mStorage.getReference().child("imagePost").child(imageUri.getLastPathSegment());
                    filepath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String t = task.getResult().toString();

                                    DatabaseReference newPost = mRef.push();

                                    newPost.child("ItemName").setValue(itmname);
                                    newPost.child("Price").setValue(itprice);
                                    newPost.child("image").setValue(task.getResult().toString());
                                    progressDialog.dismiss();
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}