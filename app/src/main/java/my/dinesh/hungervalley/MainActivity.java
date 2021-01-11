package my.dinesh.hungervalley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ss.com.bannerslider.Slider;
import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;


public class MainActivity extends BaseActivity {

    private Toolbar toolbar;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String SELECTED_ITEM = "arg_selected_item";
    Window window;
    int flags;
    int mSelectedItem;
    //ImageView bannersmall;
    List<Cat> list3;

    private Slider slider;
    private RecyclerView recyclerView, cat;
    ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mRestaurantDatabase, mImageDatabase, mCartDatabase, mMainBannerDatabase, mGroceryDatabase;
    String uId;

    List<MyDataSetGet> list;

    String Rname;

    ImageView img1, img2, img3, img4, img5;

    String banner1, banner2, banner3, banner4;

    myadapter adapter;
    mainAdapter adapter1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("hasLoggedIn", true);
        editor.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Hunger Valley");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        uId = (shared.getString("user_id", ""));

        window = getWindow();
        FirebaseApp.initializeApp(this);

        list = new ArrayList<>();

        Slider.init(new PicassoImageLoadingService(MainActivity.this));

        slider = findViewById(R.id.banner_slider1);


        recyclerView = (RecyclerView) findViewById(R.id.upload_list);
        cat = findViewById(R.id.cat);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //bannersmall = (ImageView) findViewById(R.id.bannerSmall);

        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);
        img5 = (ImageView) findViewById(R.id.img5);

        FirebaseApp.initializeApp(this);

        mRestaurantDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        mRestaurantDatabase.keepSynced(true);

        mGroceryDatabase = FirebaseDatabase.getInstance().getReference().child("Groceries").child("Categories");
        mGroceryDatabase.keepSynced(true);

        mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");

        mImageDatabase = FirebaseDatabase.getInstance().getReference().child("Main Images");
        mImageDatabase.keepSynced(true);

        mMainBannerDatabase = FirebaseDatabase.getInstance().getReference().child("Main Banner");
        mMainBannerDatabase.keepSynced(true);

        progressBar.setVisibility(View.VISIBLE);

        mMainBannerDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                banner1 = dataSnapshot.child("Banner1").getValue().toString();
                banner2 = dataSnapshot.child("Banner2").getValue().toString();
                banner3 = dataSnapshot.child("Banner3").getValue().toString();
                banner4 = dataSnapshot.child("Banner4").getValue().toString();


                Slider.init(new PicassoImageLoadingService(MainActivity.this));

                slider = findViewById(R.id.banner_slider1);

                slider.setAdapter(new MainSliderAdapter());
                slider.setInterval(3000);
                slider.hideIndicators();

                //delay for testing empty view functionality
                slider.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        slider.setAdapter(new MainSliderAdapter());


                    }
                }, 3000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setNestedScrollingEnabled(false);

        cat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //cat.setHasFixedSize(true);
        //cat.setNestedScrollingEnabled(false);


        if (isNetworkConnectionAvailable() == true) {

            progressBar.setVisibility(View.VISIBLE);

            FirebaseRecyclerOptions<CatSetGet> options =
                    new FirebaseRecyclerOptions.Builder<CatSetGet>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Categories"), CatSetGet.class)
                            .build();


            adapter = new myadapter(options);
            cat.setAdapter(adapter);


            FirebaseRecyclerOptions<MyDataSetGet> options1 =
                    new FirebaseRecyclerOptions.Builder<MyDataSetGet>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Restaurants"), MyDataSetGet.class)
                            .build();

            adapter1 = new mainAdapter(options1);
            recyclerView.setAdapter(adapter1);


            mImageDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String img_one = dataSnapshot.child("Image1").getValue().toString();
                    String img_two = dataSnapshot.child("Image2").getValue().toString();
                    String img_three = dataSnapshot.child("Image3").getValue().toString();
                    String img_four = dataSnapshot.child("Image4").getValue().toString();
                    String img_five = dataSnapshot.child("Image5").getValue().toString();

                    Picasso
                            .with(MainActivity.this)
                            .load(img_one)
                            .into(img1);
                    Picasso
                            .with(MainActivity.this)
                            .load(img_two)
                            .into(img2);
                    Picasso
                            .with(MainActivity.this)
                            .load(img_three)
                            .into(img3);
                    Picasso
                            .with(MainActivity.this)
                            .load(img_four)
                            .into(img4);

                    Picasso
                            .with(MainActivity.this)
                            .load(img_five)
                            .into(img5);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        adapter1.startListening();
    }

    public class myadapter extends FirebaseRecyclerAdapter<CatSetGet, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<CatSetGet> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull CatSetGet model) {

            holder.name.setText(model.getName());

            Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MainActivity.this, SubCategory.class);
                    intent.putExtra("pos", position);
                    intent.putExtra("cat_id", model.getName());
                    intent.putExtra("image", model.getImage());
                    startActivity(intent);

                }
            });
        }

        @NonNull
        @Override
        public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_model2, parent, false);
            return new myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {
            ImageView img;
            TextView name;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.imageView5);
                name = (TextView) itemView.findViewById(R.id.textView18);

            }
        }
    }

    public class mainAdapter extends FirebaseRecyclerAdapter<MyDataSetGet, mainAdapter.myviewholder> {

        public mainAdapter(@NonNull FirebaseRecyclerOptions<MyDataSetGet> options1) {
            super(options1);
        }

        @Override
        protected void onBindViewHolder(@NonNull mainAdapter.myviewholder holder, int position, @NonNull MyDataSetGet model) {

            holder.name.setText(model.getRestaurant_name());
            holder.type.setText(model.getRestaurant_type());


            Rname = model.getRestaurant_name().toString();
            final String rating = Double.toString(model.getRating());
            final String discount = Integer.toString(model.getDiscount());

            holder.rating.setText(rating);

            if (!holder.image.equals("default")) {
                Picasso
                        .with(getApplicationContext())
                        .load(model.getBanner())
                        .into(holder.image);

            }

            if (model.getStatus() != null) {

                holder.main_view.setAlpha(0.6f);

            }

            int b = Integer.parseInt(discount);
            if (b > 0) {

                holder.layout_discount.setVisibility(View.VISIBLE);


            } else {

                holder.layout_discount.setVisibility(View.GONE);
            }

            float a = Float.parseFloat(rating);

            if (a > 4.0) {

                holder.layout_rating.setBackgroundResource(R.drawable.star_bg);
            } else if (a > 3.0) {

                holder.layout_rating.setBackgroundResource(R.drawable.star_bg_two);
            } else {

                holder.layout_rating.setBackgroundResource(R.drawable.star_bg_three);
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    mCartDatabase.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChildren()) {

                                new AlertDialog.Builder(MainActivity.this)
                                        .setMessage("Your cart will be empty once you will change the restaurant!")
                                        .setNegativeButton(android.R.string.no, null)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface arg0, int arg1) {
                                                MainActivity.super.onBackPressed();

                                                mCartDatabase.child(uId).removeValue();

                                                Intent chatIntent = new Intent(MainActivity.this, SingleRestaurant.class);
                                                //final String s = ((Application) getApplicationContext()).setSomeVariable(Rname);
                                                chatIntent.putExtra("restauranr_id", model.Restaurant_name);
                                                startActivity(chatIntent);
                                            }
                                        }).create().show();


                            } else {

                                Intent chatIntent = new Intent(MainActivity.this, SingleRestaurant.class);
                                //final String s = ((Application) getApplicationContext()).setSomeVariable(Rname);
                                chatIntent.putExtra("restauranr_id", model.Restaurant_name);
                                startActivity(chatIntent);


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            });


        }

        @NonNull
        @Override
        public mainAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
            return new mainAdapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {
            View mView;
            LinearLayout layout_discount, layout_rating;
            TextView rating, status, name, type;
            LinearLayout main_view;
            ImageView image;

            public myviewholder(@NonNull View itemView) {
                super(itemView);

                layout_discount = (LinearLayout) itemView.findViewById(R.id.layout_discount);
                layout_rating = (LinearLayout) itemView.findViewById(R.id.layout_rating);
                rating = (TextView) itemView.findViewById(R.id.rating);
                name = (TextView) itemView.findViewById(R.id.name);
                type = (TextView) itemView.findViewById(R.id.type);
                main_view = itemView.findViewById(R.id.main_view);
                status = itemView.findViewById(R.id.status);
                image = itemView.findViewById(R.id.image);


                mView = itemView;

            }


        }
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.restaurant;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    public boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm =
                (ConnectivityManager) MainActivity.this.getSystemService(CONNECTIVITY_SERVICE);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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


    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finishAffinity();
    }


    public class MainSliderAdapter extends SliderAdapter {

        @Override
        public int getItemCount() {
            return 4;
        }

        @Override
        public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
            switch (position) {

                case 0:
                    viewHolder.bindImageSlide(banner1);
                    break;
                case 1:
                    viewHolder.bindImageSlide(banner2);
                    break;
                case 2:
                    viewHolder.bindImageSlide(banner3);
                    break;

                case 3:
                    viewHolder.bindImageSlide(banner4);
                    break;
            }
        }
    }


}


