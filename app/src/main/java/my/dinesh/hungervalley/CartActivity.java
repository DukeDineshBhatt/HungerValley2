package my.dinesh.hungervalley;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartActivity extends BaseActivity {

    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mCartListDatabase;
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    LinearLayout layout_empty;
    RelativeLayout layout;
    String uId, restaurantId;
    int flags, discount_int, final_total_price, intent_total_price;
    TextView package_fee, txt_package_fee;

    DatabaseReference mCartDatabase, mRestaurantDatabase, mUserDatabase, mAdmindatabase;
    String location;
    MyAdapter adapter;
    TextView restaurant, total_price, to_pay, discount;

    ArrayList<CartDataSetGet> list;
    Button place;
    ImageView banner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = (RecyclerView) findViewById(R.id.upload_list);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
        layout = (RelativeLayout) findViewById(R.id.layout);
        restaurant = (TextView) findViewById(R.id.restaurant);
        total_price = (TextView) findViewById(R.id.total_price);
        to_pay = (TextView) findViewById(R.id.to_pay);
        discount = (TextView) findViewById(R.id.discount);
        place = (Button) findViewById(R.id.place);
        banner = (ImageView) findViewById(R.id.banner);
        package_fee = (findViewById(R.id.package_fee));
        txt_package_fee = (findViewById(R.id.txt_package_fees));

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        uId = (shared.getString("user_id", ""));


        intent_total_price = getIntent().getIntExtra("total_price", 0);
        Log.d("TOTAL", "" + intent_total_price);

        //SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        restaurantId = (shared.getString("restaurant", ""));

        mCartListDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");
        mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);
        mAdmindatabase = FirebaseDatabase.getInstance().getReference().child("Admin");

        mAdmindatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                txt_package_fee.setText(dataSnapshot.child("Fee Name").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uId);
        mAdmindatabase = FirebaseDatabase.getInstance().getReference().child("Admin").child("Locations");



        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Address").exists()) {

                    progressBar.setVisibility(View.VISIBLE);

                    location = dataSnapshot.child("Address").child("location").getValue().toString();

                    mAdmindatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot areaSnapshot : dataSnapshot.getChildren()) {
                                if (areaSnapshot.child("areaName").getValue().toString().equals(location)) {

                                    package_fee.setText(areaSnapshot.child("areaPrice").getValue().toString());

                                    progressBar.setVisibility(View.GONE);
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            progressBar.setVisibility(View.GONE);

                        }
                    });


                } else {
                    package_fee.setText("20");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

            }
        });

        mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    progressBar.setVisibility(View.GONE);
                    layout_empty.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.VISIBLE);

                    if (dataSnapshot.child("Total price").exists()) {

                        intent_total_price = Integer.parseInt(dataSnapshot.child("Total price").getValue().toString());

                    } else {

                        mCartDatabase.child("Total price").setValue(intent_total_price);
                    }

                   /* mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });*/

                    SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
                    restaurantId = (shared.getString("restaurant", ""));

                    restaurant.setText(restaurantId);

                    mRestaurantDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(restaurantId);

                    mRestaurantDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String banner_url = dataSnapshot.child("Banner").getValue().toString();

                            Picasso
                                    .with(getApplicationContext())
                                    .load(banner_url)
                                    .into(banner);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    linearLayoutManager = new LinearLayoutManager(CartActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setNestedScrollingEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);

                    mCartListDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId).child(restaurantId);

                    mCartListDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            list = new ArrayList<CartDataSetGet>();

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                mCartListDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);
                                mCartListDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        total_price.setText(dataSnapshot.child("Total price").getValue().toString());

                                        int a = Integer.parseInt(total_price.getText().toString());

                                        int f = a + Integer.parseInt(package_fee.getText().toString());

                                        mRestaurantDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                if (dataSnapshot.child("Discount").exists()) {

                                                    String s = dataSnapshot.child("Discount").getValue().toString();
                                                    discount_int = Integer.parseInt(s);

                                                    discount.setText(s);

                                                    float final_Value = f - discount_int;

                                                    to_pay.setText(String.valueOf(final_Value));

                                                } else {

                                                    to_pay.setText(String.valueOf(f));

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                restaurant.setText(restaurantId);

                                CartDataSetGet p = dataSnapshot1.getValue(CartDataSetGet.class);
                                list.add(p);

                            }

                            adapter = new MyAdapter(CartActivity.this, list);
                            recyclerView.setAdapter(adapter);

                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(CartActivity.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
                        }
                    });


                } else {

                    progressBar.setVisibility(View.GONE);
                    layout_empty.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

            }
        });


        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mUserDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("Address").exists()) {

                            Intent intent = new Intent(CartActivity.this, ConfirmOrder.class);

                            intent.putExtra("tax_price", Integer.parseInt(package_fee.getText().toString()));
                            startActivity(intent);

                        } else {

                            Intent intent = new Intent(CartActivity.this, SetAddress.class);
                            startActivity(intent);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


    }


    @Override
    int getContentViewId() {
        return R.layout.activity_cart;

    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.cart;
    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(CartActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) CartActivity.this.getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if (isConnected) {
            Log.d("Network", "Connected");
            return true;
        } else {
            checkNetworkConnection();
            Log.d("Network", "Not Connected");
            return false;
        }
    }

    public void checkNetworkConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
