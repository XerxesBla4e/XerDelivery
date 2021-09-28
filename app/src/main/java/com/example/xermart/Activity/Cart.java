package com.example.xermart.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xermart.CartProducts.DatabaseHelper;
import com.example.xermart.CartProducts.DatabaseManager;
import com.example.xermart.R;
import com.squareup.picasso.Picasso;

import java.sql.SQLDataException;

public class Cart extends AppCompatActivity implements View.OnClickListener {
    String item;
    String price;
    String imageurl;
    int quantity = 0;
    int total;
    Button fetch;
    DatabaseManager databaseManager;
    private static final String TAG = "Cart";
    TextView vname, vmarket, itmname, itmprice;
    ImageView prodimg, increment, decrement;
    EditText qtty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        hooks();

        fetch.setOnClickListener(this);
        increment.setOnClickListener(this);
        decrement.setOnClickListener(this);

        databaseManager = new DatabaseManager(this);
        try {
            databaseManager.open();
        } catch (SQLDataException e) {
            e.printStackTrace();
        }

        if (getIntent() != null) {
            item = getIntent().getStringExtra("item");
            price = getIntent().getStringExtra("price");
            imageurl = getIntent().getStringExtra("image");
        }

        itmname.setText(item);
        itmprice.setText(price);
        Picasso.with(this).load(imageurl).into(prodimg);

    }

    private void hooks() {
        fetch = findViewById(R.id.insert);
        vname = findViewById(R.id.textvendorname);
        vmarket = findViewById(R.id.textmarket);
        itmname = findViewById(R.id.textitmname);
        itmprice = findViewById(R.id.textitmprice);
        prodimg = findViewById(R.id.prodimg);
        qtty = findViewById(R.id.qty);
        increment = findViewById(R.id.add);
        decrement = findViewById(R.id.minus);
    }

    private void display(int number) {
        qtty.setText("" + number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                quantity = quantity + 1;
                display(quantity);
                break;
            case R.id.minus:
                if (quantity > 0) {
                    quantity = quantity - 1;
                    display(quantity);
                }
                break;
            case R.id.insert:
                total = Integer.parseInt(price) * Integer.parseInt(qtty.getText().toString());
                int f = databaseManager.insert(item, Integer.parseInt(qtty.getText().toString()), total, imageurl);
                if (f > 1) {
                    Toast.makeText(this, "Item Added To Cart", Toast.LENGTH_SHORT).show();
                }
                break;
            default:


        }

    }
}
