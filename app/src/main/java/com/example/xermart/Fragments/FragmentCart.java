package com.example.xermart.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applozic.mobicomkit.api.ApplozicMqttService;
import com.example.xermart.Admin.CartAdapter;
import com.example.xermart.CartProducts.DatabaseHelper;
import com.example.xermart.CartProducts.DatabaseManager;
import com.example.xermart.Constants;
import com.example.xermart.R;
import com.example.xermart.Users;
import com.example.xermart.models.CartModel;
import com.example.xermart.models.Orders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

public class FragmentCart extends Fragment {
    private static final String TAG = "Cart Fragment";

    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    List<CartModel> cartModelList;
    DatabaseManager databaseManager;
    Button btnorder;
    TextView totalamt;
    DatabaseReference ordeDbRef;
    private String uphonenumber;
    private DatabaseReference databaseReference;
    FirebaseUser user;
    private String userID;
    private String uemail;
    FirebaseAuth firebaseAuth;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cartrecycler, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users userProfile = snapshot.getValue(Users.class);

                if (userProfile != null) {
                    uemail = userProfile.getEmail();
                    uphonenumber = userProfile.getPhonenumber();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        databaseManager = new DatabaseManager(getContext());
        try {
            databaseManager.open();
        } catch (SQLDataException e) {
            e.printStackTrace();
        }

        btnorder = (Button) view.findViewById(R.id.btnodr);
        totalamt = (TextView) view.findViewById(R.id.total);
        ordeDbRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Orders> orderlist = new ArrayList<Orders>();
                Cursor cursor = databaseManager.fetch();
                if (cursor.moveToFirst()) {
                    do {

                        String quantity = cursor.getString(cursor.getColumnIndex(DatabaseHelper.QUANTITY));
                        String item = cursor.getString(cursor.getColumnIndex(DatabaseHelper.ITEM));
                        String total = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TOTAL_AMOUNT));

                        orderlist.add(new Orders(uemail,
                                uphonenumber, item, quantity, total));


                    }
                    while (cursor.moveToNext());

                    if (orderlist.size() > 0) {
                        for (Orders o : orderlist) {
                            ordeDbRef.push().setValue(o);
                        }
                        Toast.makeText(getContext(), "Order Submitted Succesfully", Toast.LENGTH_SHORT).show();
                        databaseManager.delete();
                    }
                }
            }

            /*
            private void preparedNotificationMessage(String orderId) {

                String NOTIFICATION_TOPIC = "/topic/" + Constants.FCM_TOPIC;
                String NOTIFICATION_TITLE = "/New Order/" + orderId;
                String NOTIFICATION_MESSAGE = "Congratulations...! You have new order";
                String NOTIFICATION_TYPE = "New Order";

                //prepare json what to send and where to send
                JSONObject notificationJo = new JSONObject();
                JSONObject notificationBodyJo = new JSONObject();

                try {
                    notificationBodyJo.put("NotificationType", NOTIFICATION_TYPE);
                    notificationBodyJo.put("buyerUid", firebaseAuth.getUid());
                } catch (Exception e) {

                }
            }*/
        });

        recyclerView = view.findViewById(R.id.cartable);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new

                LinearLayoutManager(getContext()));
        cartModelList = new ArrayList<CartModel>();

        Cursor cursor = databaseManager.fetch();
        if (cursor.moveToFirst()) {
            do {
                cartModelList.add(new CartModel(cursor.getInt(0), cursor.getString(1), cursor.getInt(2)
                        , cursor.getInt(3), cursor.getString(4)));

            } while (cursor.moveToNext());

            int totalPrices = 0;

            for (int i = 0; i < cartModelList.size(); i++) {
                totalPrices += cartModelList.get(i).getTotal();
            }
            totalamt.setText("Total Amount:" + totalPrices + "");

            cartAdapter = new CartAdapter(getContext(), cartModelList);
            recyclerView.setAdapter(cartAdapter);
            cartAdapter.notifyDataSetChanged();

        } else {
            Toast.makeText(getContext(), "No Items In Cart", Toast.LENGTH_SHORT).show();
        }

        return view;

    }
}
