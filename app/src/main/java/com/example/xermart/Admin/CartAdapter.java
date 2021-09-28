package com.example.xermart.Admin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xermart.Activity.Cart;
import com.example.xermart.CartProducts.DatabaseManager;
import com.example.xermart.Fragments.FragmentCart;
import com.example.xermart.R;
import com.example.xermart.models.CartModel;
import com.example.xermart.models.ItemModel;
import com.squareup.picasso.Picasso;

import java.sql.SQLDataException;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    String imageUri;
    DatabaseManager databaseManager;

    Context context;
    List<CartModel> cartModelList;

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartrow, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartModel cartModel = cartModelList.get(position);

        holder.tvitmname.setText(cartModel.getItemname());
        holder.tvquantity.setText(String.valueOf(cartModel.getQuantity()));
        holder.tvtotal.setText(String.valueOf(cartModel.getTotal()));


        imageUri = null;
        imageUri = cartModel.getImage();
        Picasso.with(context).load(imageUri).into(holder.imageView);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseManager = new DatabaseManager(context);
                try {
                    databaseManager.open();
                } catch (SQLDataException e) {
                    e.printStackTrace();
                }
                int f = databaseManager.delete(cartModel.getID());
                if(f>0){
                    Toast.makeText(context,"Successfully Deleted Item",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvitmname, tvquantity, tvtotal;
        Button remove;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.recycleritemimg);
            tvitmname = itemView.findViewById(R.id.recycleritem1);
            tvquantity = itemView.findViewById(R.id.recyclerprice1);
            tvtotal = itemView.findViewById(R.id.recycleramt);
            remove = itemView.findViewById(R.id.remove);
        }
    }

}
