package com.example.xermart.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.xermart.R;

public class FragmentCategory extends Fragment {
    private static final String TAG = "Category Fragment";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView: started");

        View view = inflater.inflate(R.layout.prodcategoryfrag, container, false);

        return view;
    }
}
