package my.dinesh.hungervalley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static my.dinesh.hungervalley.Application.getContext;

public class OrderActivity extends BaseActivity {

    int flags;
    RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    LinearLayout layout_pending, layout_onway, layout_empty, main_layout;
    private DatabaseReference myDatabase;
    String userId;
    ProgressBar progressbar;
    TextView total, status, txt_pending, txt_onway, txt_empty;
    ImageView pending, onway, empty;

    private myadapter adapter;

    TextView timerTextView;

    Button cancel_order;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        }
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);
        }

        progressbar = findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        total = findViewById(R.id.total);
        layout_pending = findViewById(R.id.layout_pending);
        layout_onway = findViewById(R.id.layout_onway);
        layout_empty = findViewById(R.id.empty_layout);
        pending = findViewById(R.id.pending);
        onway = findViewById(R.id.onway);
        empty = findViewById(R.id.empty);
        main_layout = findViewById(R.id.main_layout);
        txt_empty = findViewById(R.id.txt_empty);
        txt_onway = findViewById(R.id.txt_onway);
        txt_pending = findViewById(R.id.txt_pending);
        cancel_order = findViewById(R.id.cancel);

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        userId = (shared.getString("user_id", ""));

        linearLayoutManager = new LinearLayoutManager(OrderActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<OrderSetGet> options =
                new FirebaseRecyclerOptions.Builder<OrderSetGet>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(userId).child("Orders"), OrderSetGet.class)
                        .build();

        adapter = new myadapter(options);
        adapter.startListening();
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        timerTextView = (TextView) findViewById(R.id.timerTextView);


        FirebaseDatabase.getInstance().getReference().child("Orders List").child("User View").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("Total Price")) {

                    total.setText(String.valueOf(dataSnapshot.child("Total Price").getValue()));

                    if (dataSnapshot.child("Status").getValue().toString().equals("Pending")) {

                        layout_empty.setVisibility(View.GONE);
                        layout_onway.setVisibility(View.GONE);
                        layout_pending.setVisibility(View.VISIBLE);

                        FirebaseDatabase.getInstance().getReference().child("Main Images").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Glide.with(getContext()).
                                        load(dataSnapshot.child("Pending").child("Image").getValue().toString())
                                        .fitCenter()
                                        .into(pending);

                                txt_pending.setText(dataSnapshot.child("Pending").child("Text").getValue().toString());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else if (dataSnapshot.child("Status").getValue().toString().equals("OnWay")) {


                        layout_pending.setVisibility(View.GONE);
                        layout_empty.setVisibility(View.GONE);
                        layout_onway.setVisibility(View.VISIBLE);

                        FirebaseDatabase.getInstance().getReference().child("Main Images").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Glide.with(getContext()).
                                        load(dataSnapshot.child("OnWay").child("Image").getValue().toString())
                                        .fitCenter()
                                        .into(onway);

                                txt_onway.setText(dataSnapshot.child("OnWay").child("Text").getValue().toString());

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }


                } else {

                    main_layout.setVisibility(View.GONE);
                    layout_onway.setVisibility(View.GONE);
                    layout_pending.setVisibility(View.GONE);
                    layout_empty.setVisibility(View.VISIBLE);

                    FirebaseDatabase.getInstance().getReference().child("Main Images").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Glide.with(getContext()).
                                    load(dataSnapshot.child("Empty").child("Image").getValue().toString())
                                    .fitCenter()
                                    .into(empty);

                            txt_empty.setText(dataSnapshot.child("Empty").child("Text").getValue().toString());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    @Override
    int getContentViewId() {
        return R.layout.activity_order;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.order;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(OrderActivity.this, MainActivity.class);
        startActivity(intent);
        finish();


    }


    public class myadapter extends FirebaseRecyclerAdapter<OrderSetGet, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<OrderSetGet> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, int position, @NonNull OrderSetGet model) {

            holder.restro.setText(model.getRes());
            holder.price.setText(model.getPrice());
            holder.product_name.setText(model.getpName());
            holder.quantity.setText(model.getQuantity());

            if (model.getType().equals("Non-Veg")) {

                Picasso
                        .get()
                        .load(R.drawable.non_veg)
                        .into(holder.type);
            } else {

                Picasso
                        .get()
                        .load(R.drawable.veg)
                        .into(holder.type);
            }


           /* holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(SingleOrderActivity.this, SingleOrderActivity.class);
                    intent.putExtra("user_id", Id);
                    startActivity(intent);

                }
            });*/
        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_list, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            TextView restro, price, product_name, quantity;
            ImageView type;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                price = (TextView) itemView.findViewById(R.id.price);
                restro = (TextView) itemView.findViewById(R.id.restro);
                product_name = (TextView) itemView.findViewById(R.id.product_name);
                quantity = (TextView) itemView.findViewById(R.id.qnty);
                type = (ImageView) itemView.findViewById(R.id.type_image);

            }
        }
    }


}