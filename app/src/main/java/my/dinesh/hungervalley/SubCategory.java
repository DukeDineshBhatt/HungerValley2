package my.dinesh.hungervalley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SubCategory extends AppCompatActivity {

    int flags;
    private Toolbar toolbar;
    String cat_id, imagetxt, poss;
    int pos;
    int totalPrice = 0;
    int count;
    RecyclerView subCat;
    myadapter adapter;
    ImageView image;
    ProgressBar progressBar;
    String uId, bookskey, pricee, reasonString, mykey;
    private LinearLayoutManager linearLayoutManager;
    DatabaseReference mCartDatabase, mAdminDatabase, mRestaurantDatabase;
    LinearLayout cartLayout;
    boolean isRestaurantOff = false;
    boolean isOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        image = (ImageView) findViewById(R.id.image);
        cartLayout = (LinearLayout) findViewById(R.id.cart_layout);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        cat_id = intent.getStringExtra("cat_id");
        imagetxt = intent.getStringExtra("image");
        pos = intent.getIntExtra("pos", 0);
        getSupportActionBar().setTitle(cat_id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        subCat = (RecyclerView) findViewById(R.id.subCat);

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        uId = (shared.getString("user_id", ""));

        int a = pos++;
        poss = "Cat" + pos;


        Log.d("SSSS", poss);

        linearLayoutManager = new LinearLayoutManager(this);

        mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

        mAdminDatabase = FirebaseDatabase.getInstance().getReference().child("Admin").child("Logout");

        subCat.setLayoutManager(linearLayoutManager);

        mAdminDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("status").getValue().toString().equals("on")) {

                    isOn = true;
                } else {

                    isOn = false;

                    if (dataSnapshot.child("Reason").exists()) {

                        reasonString = dataSnapshot.child("Reason").getValue().toString();

                    } else {
                        reasonString = " ";

                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseRecyclerOptions<SubModel> options =
                new FirebaseRecyclerOptions.Builder<SubModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Categories").child(poss).child("Menu"), SubModel.class)
                        .build();


        adapter = new myadapter(options);
        subCat.setAdapter(adapter);

        Glide.with(image.getContext()).load(imagetxt).into(image);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    public class myadapter extends FirebaseRecyclerAdapter<SubModel, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<SubModel> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, int position, @NonNull SubModel model) {

            holder.setIsRecyclable(false);

            holder.res_name.setText(model.getRes());
            holder.price.setText(Integer.toString(model.getPrice()));

            String type = model.getType();

            holder.food_name.setText(model.getFoodName());

            if (!type.equals("Non-Veg")) {

                Glide.with(holder.type_image.getContext()).load(R.drawable.veg).into(holder.type_image);

            } else {

                Glide.with(holder.type_image.getContext()).load(R.drawable.non_veg).into(holder.type_image);

            }

            mRestaurantDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurants").child(model.getRes());
            mRestaurantDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    if (dataSnapshot.child("Status").exists()) {

                        isRestaurantOff = true;

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            mCartDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    if (dataSnapshot.hasChildren()) {

                        cartLayout.setVisibility(View.VISIBLE);

                        cartLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(SubCategory.this, CartActivity.class);
                                startActivity(intent);

                            }
                        });

                        for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                            if (!uniqueKeySnapshot.getKey().equals("Total price")) {

                                bookskey = uniqueKeySnapshot.getKey();

                            }

                            for (DataSnapshot a : uniqueKeySnapshot.getChildren()) {

                                mykey = a.getKey();

                            }

                        }

                        pricee = Integer.toString(model.getPrice());

                        holder.add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (isRestaurantOff == false) {


                                    if (isOn) {


                                        if (model.getRes().equals(bookskey)) {

                                            if (mykey.equals(model.getFoodName())) {

                                                Toast.makeText(SubCategory.this, "This Item is already added in cart!", Toast.LENGTH_SHORT).show();
                                            } else {


                                                cartLayout.setClickable(false);

                                                progressBar.setVisibility(View.VISIBLE);

                                                holder.layout_button.setVisibility(View.VISIBLE);
                                                holder.add.setVisibility(View.GONE);


                                                int count = Integer.parseInt(String.valueOf(holder.textCount.getText()));

                                                mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                                HashMap<String, Object> cartMap = new HashMap<>();
                                                cartMap.put("pName", model.getFoodName());
                                                cartMap.put("price", model.getPrice());
                                                cartMap.put("quantity", count);
                                                cartMap.put("Type", model.getType());

                                                mCartDatabase.child("User View").child(uId).child(model.getRes()).child(model.getFoodName())
                                                        .updateChildren(cartMap)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()) {

                                                                    mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

                                                                    mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                                            totalPrice = model.getPrice() + Integer.parseInt(dataSnapshot.child("Total price").getValue().toString());

                                                                            mCartDatabase.child("Total price").setValue(String.valueOf(totalPrice));

                                                                            cartLayout.setOnClickListener(new View.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(View v) {

                                                                                    Intent intent = new Intent(SubCategory.this, CartActivity.class);
                                                                                    startActivity(intent);

                                                                                }
                                                                            });

                                                                            progressBar.setVisibility(View.GONE);

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                            progressBar.setVisibility(View.GONE);
                                                                        }
                                                                    });


                                                                }

                                                            }
                                                        });


                                            }


                                        } else {

                                            AlertDialog.Builder builder1 = new AlertDialog.Builder(SubCategory.this);
                                            builder1.setTitle("Replace Cart Item?");
                                            builder1.setMessage("Your cart already have some items. Do you want to discard and change the restaurant? ");
                                            builder1.setCancelable(true);
                                            builder1.setNegativeButton(android.R.string.no, null);

                                            builder1.setPositiveButton(
                                                    "Ok",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                            mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

                                                            mCartDatabase.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {

                                                                    Toast.makeText(SubCategory.this, "Your Cart is Refreshed.", Toast.LENGTH_SHORT).show();

                                                                    Intent intent = new Intent(SubCategory.this, MainActivity.class);
                                                                    startActivity(intent);
                                                                    finish();

                                                                }
                                                            });


                                                            dialog.cancel();


                                                        }
                                                    });


                                            AlertDialog alert11 = builder1.create();
                                            alert11.show();

                                        }


                                    } else {

                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SubCategory.this);
                                        builder1.setTitle("Currently not accepting orders");
                                        builder1.setMessage(reasonString);
                                        builder1.setCancelable(true);

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


                                } else {

                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SubCategory.this);
                                    builder1.setTitle("This Restaurant is Currently not accepting orders");
                                    builder1.setCancelable(true);

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
                        });


                        holder.buttonInc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressBar.setVisibility(View.VISIBLE);
                                cartLayout.setClickable(false);

                                count = Integer.parseInt(String.valueOf(holder.textCount.getText()));
                                count++;


                                mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                HashMap<String, Object> cartMap = new HashMap<>();
                                cartMap.put("pName", model.getFoodName());
                                cartMap.put("price", model.getPrice() * count);
                                cartMap.put("quantity", count);
                                cartMap.put("Type", model.getType());

                                mCartDatabase.child("User View").child(uId).child(model.getRes()).child(model.getFoodName())
                                        .updateChildren(cartMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    pricee = Integer.toString(model.getPrice());

                                                    mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

                                                    mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            totalPrice = model.getPrice() + Integer.parseInt(dataSnapshot.child("Total price").getValue().toString());

                                                            mCartDatabase.child("Total price").setValue(String.valueOf(totalPrice));
                                                            holder.textCount.setText(String.valueOf(count));
                                                            progressBar.setVisibility(View.GONE);
                                                            cartLayout.setClickable(true);


                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                                }

                                            }
                                        });

                            }
                        });

                        holder.buttonDec.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressBar.setVisibility(View.VISIBLE);
                                cartLayout.setClickable(false);

                                count = Integer.parseInt(String.valueOf(holder.textCount.getText()));

                                if (count == 1) {

                                    holder.layout_button.setVisibility(View.GONE);
                                    holder.add.setVisibility(View.VISIBLE);

                                    mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

                                    mCartDatabase.removeValue();

                                    progressBar.setVisibility(View.GONE);


                                } else if (count > 0) {

                                    count--;

                                    mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                    HashMap<String, Object> cartMap = new HashMap<>();
                                    cartMap.put("pName", model.getFoodName());
                                    cartMap.put("price", model.getPrice() * count);
                                    cartMap.put("quantity", count);
                                    cartMap.put("Type", model.getType());

                                    mCartDatabase.child("User View").child(uId).child(model.getRes()).child(model.getFoodName())
                                            .updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {


                                                        mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

                                                        mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                totalPrice = -(model.getPrice() - Integer.parseInt(dataSnapshot.child("Total price").getValue().toString()));

                                                                mCartDatabase.child("Total price").setValue(String.valueOf(totalPrice));
                                                                holder.textCount.setText(String.valueOf(count));

                                                                progressBar.setVisibility(View.GONE);
                                                                cartLayout.setClickable(true);

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                progressBar.setVisibility(View.GONE);

                                                            }
                                                        });
                                                    }

                                                }
                                            });

                                }

                            }
                        });


                    } else {

                        SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putString("restaurant", model.getRes());
                        editor.commit();

                        cartLayout.setVisibility(View.GONE);

                        holder.add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (isRestaurantOff == false) {

                                    if (isOn) {

                                        progressBar.setVisibility(View.VISIBLE);
                                        cartLayout.setClickable(false);

                                        holder.layout_button.setVisibility(View.VISIBLE);
                                        holder.add.setVisibility(View.GONE);

                                        int count = Integer.parseInt(String.valueOf(holder.textCount.getText()));

                                        mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                        pricee = Integer.toString(model.getPrice());
                                        HashMap<String, Object> cartMap = new HashMap<>();
                                        cartMap.put("pName", model.getFoodName());
                                        cartMap.put("price", model.getPrice());
                                        cartMap.put("quantity", count);
                                        cartMap.put("Type", model.getType());

                                        mCartDatabase.child("User View").child(uId).child(model.getRes()).child(model.getFoodName())
                                                .updateChildren(cartMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {

                                                            mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

                                                            mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                                                    mCartDatabase.child("Total price").setValue(model.getPrice());

                                                                    cartLayout.setVisibility(View.VISIBLE);
                                                                    progressBar.setVisibility(View.GONE);
                                                                    cartLayout.setClickable(true);

                                                                    cartLayout.setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {

                                                                            Intent intent = new Intent(SubCategory.this, CartActivity.class);
                                                                            startActivity(intent);

                                                                        }
                                                                    });


                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    progressBar.setVisibility(View.GONE);
                                                                }
                                                            });


                                                        }

                                                    }
                                                });


                                    } else {

                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SubCategory.this);
                                        builder1.setTitle("Currently not accepting orders");
                                        builder1.setMessage(reasonString);
                                        builder1.setCancelable(true);

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

                                } else {

                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(SubCategory.this);
                                    builder1.setTitle("This Restaurant is Currently not accepting orders");
                                    builder1.setCancelable(true);

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
                        });

                        holder.buttonInc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressBar.setVisibility(View.VISIBLE);

                                count = Integer.parseInt(String.valueOf(holder.textCount.getText()));
                                count++;


                                mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List");

                                HashMap<String, Object> cartMap = new HashMap<>();
                                cartMap.put("pName", model.getFoodName());
                                cartMap.put("price", Integer.parseInt(pricee) * count);
                                cartMap.put("quantity", count);
                                cartMap.put("Type", model.getType());

                                mCartDatabase.child("User View").child(uId).child(model.getRes()).child(model.getFoodName())
                                        .updateChildren(cartMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if (task.isSuccessful()) {

                                                    mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

                                                    mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            totalPrice = Integer.parseInt(pricee) + Integer.parseInt(dataSnapshot.child("Total price").getValue().toString());

                                                            mCartDatabase.child("Total price").setValue(String.valueOf(totalPrice));
                                                            holder.textCount.setText(String.valueOf(count));

                                                            progressBar.setVisibility(View.GONE);

                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                            progressBar.setVisibility(View.GONE);
                                                        }
                                                    });

                                                }

                                            }
                                        });

                            }
                        });

                        holder.buttonDec.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                progressBar.setVisibility(View.VISIBLE);

                                count = Integer.parseInt(String.valueOf(holder.textCount.getText()));

                                if (count == 1) {

                                    holder.layout_button.setVisibility(View.GONE);
                                    holder.add.setVisibility(View.VISIBLE);

                                    mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

                                    mCartDatabase.child(model.getRes()).child(model.getFoodName()).removeValue();

                                    mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            totalPrice = -(Integer.parseInt(pricee) - Integer.parseInt(dataSnapshot.child("Total price").getValue().toString()));

                                            mCartDatabase.child("Total price").setValue(String.valueOf(totalPrice));
                                            progressBar.setVisibility(View.GONE);


                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });

                                } else if (count > 0) {

                                    count--;

                                    mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List");


                                    HashMap<String, Object> cartMap = new HashMap<>();
                                    cartMap.put("pName", model.getFoodName());
                                    cartMap.put("price", Integer.parseInt(pricee) * count);
                                    cartMap.put("quantity", count);
                                    cartMap.put("Type", model.getType());

                                    mCartDatabase.child("User View").child(uId).child(model.getRes()).child(model.getFoodName())
                                            .updateChildren(cartMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {


                                                        mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View").child(uId);

                                                        mCartDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                                totalPrice = -(Integer.parseInt(pricee) - Integer.parseInt(dataSnapshot.child("Total price").getValue().toString()));

                                                                mCartDatabase.child("Total price").setValue(String.valueOf(totalPrice));
                                                                holder.textCount.setText(String.valueOf(count));
                                                                progressBar.setVisibility(View.GONE);

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }

                                                }
                                            });

                                }

                            }
                        });


                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_subcat, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {
            ImageView img, type_image;
            TextView res_name, food_name, price, textCount;
            Button buttonInc, buttonDec, add;
            LinearLayout layout_button;
            LinearLayout main_view;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.imageView5);
                res_name = (TextView) itemView.findViewById(R.id.res_name);
                food_name = (TextView) itemView.findViewById(R.id.food_name);
                price = (TextView) itemView.findViewById(R.id.price);
                type_image = (ImageView) itemView.findViewById(R.id.type_image);
                buttonInc = (Button) itemView.findViewById(R.id.btn_add);
                buttonDec = (Button) itemView.findViewById(R.id.btn_minus);
                add = (Button) itemView.findViewById(R.id.add);
                textCount = (TextView) itemView.findViewById(R.id.text);
                layout_button = (LinearLayout) itemView.findViewById(R.id.layout_button);
                main_view = itemView.findViewById(R.id.main_view);
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SubCategory.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}