package my.dinesh.hungervalley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ConfirmOrder extends AppCompatActivity {

    int flags, fValue, temp;
    private Toolbar toolbar;
    private DatabaseReference mCartListDatabase, mDatabase;
    String uId, restaurantId;
    TextView to_pay, name, mobile, txt_location, txt_locality, txt_landmark, edit_address, mobile2;
    ProgressBar progressbar;
    Button place;
    DatabaseReference mOrderDatabase, mAdminOrdreDatabase, mAdminDatabase;

    String pName, price, qty, s;

    String tempPrice;

    ArrayList<CartDataSetGet> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order2);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(" ");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();

        to_pay = (TextView) findViewById(R.id.to_pay);
        name = (TextView) findViewById(R.id.name);
        mobile = (TextView) findViewById(R.id.mobile);
        txt_location = (TextView) findViewById(R.id.location);
        txt_locality = (TextView) findViewById(R.id.locality);
        txt_landmark = (TextView) findViewById(R.id.landmark);
        edit_address = (TextView) findViewById(R.id.edit_address);
        mobile2 = (TextView) findViewById(R.id.mobile2);
        place = (Button) findViewById(R.id.place);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        uId = (shared.getString("user_id", ""));
        restaurantId = (shared.getString("restaurant", ""));

        mCartListDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

        mOrderDatabase = FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(uId).child(restaurantId);
        mAdminOrdreDatabase = FirebaseDatabase.getInstance().getReference().child("Orders List").child("Admin View").child(uId).child(restaurantId);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(uId);

        mAdminDatabase = FirebaseDatabase.getInstance().getReference().child("Admin").child("Logout");


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference usersRef = database.getReference("Users").child(uId);

        mCartListDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressbar.setVisibility(View.VISIBLE);

                to_pay.setText(dataSnapshot.child("Total price").getValue().toString());


                int a = Integer.parseInt(to_pay.getText().toString());

                fValue = a + 20;

                to_pay.setText(String.valueOf(fValue));

                progressbar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    tempPrice = dataSnapshot.child("Total Price").getValue().toString();

                    fValue = Integer.parseInt(tempPrice) + fValue;


                } else {


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressbar.setVisibility(View.VISIBLE);

                String txt_username = dataSnapshot.child("username").getValue().toString();
                String txt_mobile = dataSnapshot.child("mobile_number").getValue().toString();
                name.setText(txt_username);
                mobile.setText(txt_mobile);

                txt_location.setText(dataSnapshot.child("Address").child("location").getValue().toString());
                txt_locality.setText(dataSnapshot.child("Address").child("locality").getValue().toString());
                txt_landmark.setText(dataSnapshot.child("Address").child("landmark").getValue().toString());
                mobile2.setText(dataSnapshot.child("Address").child("Mobile").getValue().toString());

                progressbar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edit_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ConfirmOrder.this, EditAddress.class);
                startActivity(intent);

            }
        });

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mAdminDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("status").getValue().toString().equals("on")) {


                            new AlertDialog.Builder(ConfirmOrder.this)
                                    .setMessage("are you sure want to place this Order?")
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {
                                            ConfirmOrder.super.onBackPressed();


                                            mCartListDatabase.child(restaurantId).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    progressbar.setVisibility(View.VISIBLE);

                                                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                                                        map.put("pName", uniqueKeySnapshot.child("pName").getValue().toString());
                                                        map.put("price", uniqueKeySnapshot.child("price").getValue().toString());
                                                        map.put("quantity", uniqueKeySnapshot.child("quantity").getValue().toString());


                                                        mOrderDatabase.child(uniqueKeySnapshot.getKey()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {


                                                                mOrderDatabase = FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(uId);


                                                                map1.put("Total Price", fValue);
                                                                map1.put("Status", "Pending");

                                                                mOrderDatabase.updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                    }
                                                                });

                                                            }
                                                        });


                                                    }

                                                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                                                        map.put("pName", uniqueKeySnapshot.child("pName").getValue().toString());
                                                        map.put("price", uniqueKeySnapshot.child("price").getValue().toString());
                                                        map.put("quantity", uniqueKeySnapshot.child("quantity").getValue().toString());


                                                        mAdminOrdreDatabase.child(uniqueKeySnapshot.getKey()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {


                                                                map1.put("Total Price", fValue);
                                                                map1.put("Status", "Pending");

                                                                mOrderDatabase = FirebaseDatabase.getInstance().getReference().child("Orders List").child("Admin View").child(uId);
                                                                mOrderDatabase.updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {


                                                                        mCartListDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                                                                                SharedPreferences.Editor editor1 = mPrefs.edit();
                                                                                editor1.remove("restaurant");

                                                                                progressbar.setVisibility(View.GONE);

                                                                                Toast.makeText(ConfirmOrder.this, "Order has been placed successfully", Toast.LENGTH_LONG).show();

                                                                                Intent intent = new Intent(ConfirmOrder.this, MainActivity.class);
                                                                                startActivity(intent);
                                                                                finish();

                                                                            }
                                                                        });


                                                                    }
                                                                });


                                                            }
                                                        });

                                                    }


                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });


                                        }
                                    }).create().show();


                        } else {


                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ConfirmOrder.this);
                            builder1.setMessage("Restaurant is closed!");
                            builder1.setCancelable(false);

                            builder1.setPositiveButton(
                                    "Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });


                            AlertDialog alert11 = builder1.create();
                            alert11.show();


                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
    }


}