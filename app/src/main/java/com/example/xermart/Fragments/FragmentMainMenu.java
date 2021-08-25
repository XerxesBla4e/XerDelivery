package com.example.xermart.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.xermart.Activity.DeliveryGuy;
import com.example.xermart.Activity.Main;
import com.example.xermart.R;


public class FragmentMainMenu extends Fragment {
    private static final String TAG = "Main Fragment";

    private Button product;
    private Button marketloc;
    private Button order;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashfrag, container, false);

        product = (Button) view.findViewById(R.id.btnprod);
        marketloc = (Button) view.findViewById(R.id.btnshop);
        order = (Button) view.findViewById(R.id.btnord);

        Log.d(TAG, "OnCreateView: started");

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main) getActivity()).setViewPager(1);
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Main) getActivity()).setViewPager(2);
            }
        });

        marketloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DeliveryGuy.class);
                startActivity(intent);
            }
        });

        return view;
    }
}