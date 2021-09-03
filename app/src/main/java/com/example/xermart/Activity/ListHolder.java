package com.example.xermart.Activity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xermart.R;

public class ListHolder extends RecyclerView.ViewHolder{
    public TextView txtEmail;

    public ListHolder(View itemView) {
        super(itemView);
        txtEmail = (TextView) itemView.findViewById(R.id.txt_email);
    }

}
