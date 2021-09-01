package com.example.xermart.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.xermart.ItemClickListener;
import com.example.xermart.R;
import com.example.xermart.models.Tracking;
import com.example.xermart.models.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DeliveryGuy extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    //firebase
    DatabaseReference onlineRef, currentUserRef, counterRef, location;
    FirebaseRecyclerAdapter<User, ListHolder> adapter;

    RecyclerView deliveryGuy;
    RecyclerView.LayoutManager layoutManager;

    //Location
    private static final int MY_PERMISSION_REQUEST_CODE = 71;
    private static final int PLAY_SERVICES_RES_REQUEST = 72;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_guy);

        //init views
        deliveryGuy = (RecyclerView) findViewById(R.id.delavailable);
        deliveryGuy.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        deliveryGuy.setLayoutManager(layoutManager);


        location = FirebaseDatabase.getInstance().getReference("Locations");
        onlineRef = FirebaseDatabase.getInstance().getReference().child(".info/connected");
        counterRef = FirebaseDatabase.getInstance().getReference("lastOnline");
        currentUserRef = FirebaseDatabase.getInstance().getReference("lastOnline")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {

                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }

        setupSystem();

        updateList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {

                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
            }
        }
    }

    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            //update firebase
            location.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            String.valueOf(mLastLocation.getLatitude()),
                            String.valueOf(mLastLocation.getLongitude())))
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(DeliveryGuy.this, "Successfully Updated Location Details", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DeliveryGuy.this, "Ooops something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(DeliveryGuy.this, "Couldnt get the location", Toast.LENGTH_SHORT).show();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RES_REQUEST).show();
            } else {
                Toast.makeText(this, "This device is not supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }


    private void updateList() {

        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(counterRef, User.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<User, ListHolder>(options) {
            @NonNull
            @Override
            public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_layout, parent, false);
                return new ListHolder(view);
            }

            @Override
            protected void onBindViewHolder(ListHolder listHolder, int i, User user) {
                listHolder.txtEmail.setText(user.getEmail());

                 listHolder.txtEmail.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         if (!user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {

                             Intent map = new Intent(DeliveryGuy.this, LocationActivity.class);
                             map.putExtra("email",user.getEmail());
                             map.putExtra("lat", mLastLocation.getLatitude());
                             map.putExtra("lng", mLastLocation.getLongitude());
                             startActivity(map);
                         }
                     }
                 });
            }
        };
                adapter.notifyDataSetChanged();
                deliveryGuy.setAdapter(adapter);

        };


    private void setupSystem() {
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue(Boolean.class)) {
                    currentUserRef.onDisconnect().removeValue();
                    counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "Online"));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        counterRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    User user = postSnapshot.getValue(User.class);
                    Log.d("LOG", "" + user.getEmail() + "is" + user.getStatus());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu1, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_track:
                counterRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(new User(FirebaseAuth.getInstance().getCurrentUser().getEmail(), "Online"));
                break;
            case R.id.logout:
                currentUserRef.removeValue();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    protected void onStart() {
        super.onStart();
        adapter.startListening();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    protected void onStop() {
        adapter.stopListening();
        if(mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
}