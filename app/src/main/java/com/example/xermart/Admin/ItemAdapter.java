package com.example.xermart.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xermart.Activity.Cart;
import com.example.xermart.R;
import com.example.xermart.models.CartModel;
import com.example.xermart.models.ItemModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemAdapter extends FirebaseRecyclerAdapter<ItemModel, ItemAdapter.ViewHolder> {
    String imageUri;
    Context context;


    public ItemAdapter(Context context, FirebaseRecyclerOptions<ItemModel> options) {
        super(options);
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.designrecycler, parent, false);
        return new ViewHolder(view);
    }


    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, int i, ItemModel itemModel) {
        viewHolder.tvname.setText(itemModel.getItemName());
        viewHolder.tvprice.setText(itemModel.getPrice());

        imageUri = null;
        imageUri = itemModel.getImage();
        Picasso.with(context).load(imageUri).into(viewHolder.imageView);

        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Cart.class);

                intent.putExtra("image", itemModel.getImage());
                intent.putExtra("item", itemModel.getItemName());
                intent.putExtra("price", itemModel.getPrice());

                context.startActivity(intent);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvname, tvprice;
        Button btn;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.recycleritemimg);
            tvname = itemView.findViewById(R.id.recycleritem1);
            tvprice = itemView.findViewById(R.id.recyclerprice1);
            btn = itemView.findViewById(R.id.details);
        }
    }

}

