package com.example.xermart.Activity;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.xermart.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Locations extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng market1 = new LatLng(0.3116, 32.5794);
        mMap.addMarker(new MarkerOptions().position(market1).title("Nakasero M"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(market1));

        LatLng market2 = new LatLng(0.3506, 32.5717);
        mMap.addMarker(new MarkerOptions().position(market2).title("Kalerwa M"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(market2));

        LatLng market3 = new LatLng(0.3123, 32.5770);
        mMap.addMarker(new MarkerOptions().position(market3).title("Owino M"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(market3));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(market3,12.0f));

    }
}