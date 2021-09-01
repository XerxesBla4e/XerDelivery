package com.example.xermart.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.xermart.R;
import com.example.xermart.models.Tracking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String email;
    DatabaseReference locations;
    Double lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locations = FirebaseDatabase.getInstance().getReference().child("Locations");

        if (getIntent() != null) {
            email = getIntent().getStringExtra("email");
            lat = getIntent().getDoubleExtra("lat", 0);
            lng = getIntent().getDoubleExtra("lng", 0);
        }

        if (!TextUtils.isEmpty(email))
            loadLocationForThisUser(email);
    }

    private void loadLocationForThisUser(String email) {
        Query client_location = locations.orderByChild("email").equalTo(email);

        client_location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapShot : snapshot.getChildren()) {
                    Tracking tracking = postSnapShot.getValue(Tracking.class);

                    //add marker for client location
                    LatLng clientLocation = new LatLng(Double.parseDouble(tracking.getLat()),
                            Double.parseDouble(tracking.getLng()));

                    Location deliveryGuy = new Location("");
                    deliveryGuy.setLatitude(lat);
                    deliveryGuy.setLongitude(lng);

                    Location client = new Location("");
                    client.setLatitude(Double.parseDouble(tracking.getLat()));
                    client.setLongitude(Double.parseDouble(tracking.getLng()));

                    mMap.clear();

                    //add client marker on map
                    mMap.addMarker(new MarkerOptions()
                            .position(clientLocation)
                            .title(tracking.getEmail())
                            .snippet("Distance" + new DecimalFormat("#.#").format(distance(deliveryGuy, client))+"km")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 12.0f));

                    //marker for delivery guy
                    LatLng deliveryguy = new LatLng(lng, lat);
                    mMap.addMarker(new MarkerOptions().position(deliveryguy).title(FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private double distance(Location deliveryGuy, Location client) {
        double theta = deliveryGuy.getLongitude() - client.getLongitude();
        double dist = Math.sin(deg2rad(deliveryGuy.getLatitude()))
                * Math.sin(deg2rad(client.getLatitude()))
                * Math.cos(deg2rad(deliveryGuy.getLatitude()))
                * Math.cos(deg2rad(client.getLatitude()))
                * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


    }
}