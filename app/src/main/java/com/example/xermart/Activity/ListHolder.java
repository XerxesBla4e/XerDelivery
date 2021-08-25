package com.example.xermart.Activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xermart.ItemClickListener;
import com.example.xermart.R;

public class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtEmail;
    ItemClickListener itemClickListener;

    public ListHolder(View itemView) {
        super(itemView);
        txtEmail = (TextView) itemView.findViewById(R.id.txt_email);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition());
    }
}
