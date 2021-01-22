package my.dinesh.hungervalley;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.glide.slider.library.tricks.ViewPagerEx;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    ArrayList<LinkDataModel> list;

    private Slider slider;
    private RecyclerView recyclerView, cat, link_recycler;
    ProgressBar progressBar;
    private LinearLayoutManager linearLayoutManager;
    private DatabaseReference mRestaurantDatabase, mImageDatabase, mCartDatabase, mMainBannerDatabase, mGroceryDatabase, mLinkDatabase, mVideosDatabase;
    String uId;

    List<MyDataSetGet> list1;

    String Rname;

    ImageView img1, img2, img3, img4, img5;

    String banner1, banner2, banner3, banner4;

    myadapter adapter;
    mainAdapter adapter1;
    LinkAdapter linkadapter;

    SliderView sliderView;
    private SliderAdapterExample Sadapter;

    ViewPager viewPager;
    TabLayout indicator;

    List<Integer> color;
    List<String> colorName;

    List<String> mSliderItems;

    LinkDataModel mymodel;

    @BindView(R.id.recyclerViewFeed)
    RecyclerView recyclerViewFeed;
    YoutubeRecyclerAdapter mRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("hasLoggedIn", true);
        editor.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);


        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        uId = (shared.getString("user_id", ""));

        window = getWindow();
        FirebaseApp.initializeApp(this);

        list = new ArrayList<>();

        Slider.init(new PicassoImageLoadingService(MainActivity.this));

        Sadapter = new SliderAdapterExample(this);

        // slider = findViewById(R.id.banner_slider1);

        //sliderView = findViewById(R.id.slider);

        recyclerView = (RecyclerView) findViewById(R.id.upload_list);
        cat = findViewById(R.id.cat);
        link_recycler = findViewById(R.id.link_recycler);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //sliderView = findViewById(R.id.imageSlider);

        //viewPager=(ViewPager)findViewById(R.id.viewPager);
        //indicator=(TabLayout)findViewById(R.id.indicator);

        mSliderItems = new ArrayList<String>();


        FirebaseApp.initializeApp(this);

        mRestaurantDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        mRestaurantDatabase.keepSynced(true);

        mVideosDatabase = FirebaseDatabase.getInstance().getReference().child("Videos");
        mVideosDatabase.keepSynced(true);

        mRestaurantDatabase = FirebaseDatabase.getInstance().getReference().child("Restaurants");
        mRestaurantDatabase.keepSynced(true);

        mGroceryDatabase = FirebaseDatabase.getInstance().getReference().child("Groceries").child("Categories");
        mGroceryDatabase.keepSynced(true);

        mCartDatabase = FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View");

        mImageDatabase = FirebaseDatabase.getInstance().getReference().child("Main Images");
        mImageDatabase.keepSynced(true);

        mLinkDatabase = FirebaseDatabase.getInstance().getReference().child("Main Images").child("Link Images");
        mLinkDatabase.keepSynced(true);

        mMainBannerDatabase = FirebaseDatabase.getInstance().getReference().child("Main Banner");
        mMainBannerDatabase.keepSynced(true);

        progressBar.setVisibility(View.VISIBLE);

       /* viewPager.setAdapter(new my.dinesh.hungervalley.SliderAdapter(this, mSliderItems));
        indicator.setupWithViewPager(viewPager, true);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);*/

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


        linearLayoutManager = new

                LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);


        cat.setLayoutManager(new

                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        link_recycler.setLayoutManager(new

                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


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
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter1);

            FirebaseRecyclerOptions<LinkDataModel> options2 =
                    new FirebaseRecyclerOptions.Builder<LinkDataModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Main Images").child("Link Images"), LinkDataModel.class)
                            .build();

            linkadapter = new LinkAdapter(options2);
            link_recycler.setNestedScrollingEnabled(false);
            link_recycler.setAdapter(linkadapter);


            ButterKnife.bind(MainActivity.this);

            ArrayList<YoutubeVideo> videoArrayList = new ArrayList<>();


            mVideosDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot uniqueKeySnapshot : dataSnapshot.getChildren()) {

                        YoutubeVideo video1 = new YoutubeVideo();

                        String a = uniqueKeySnapshot.child("Link").getValue().toString();
                        video1.setVideoId(a);

                        video1.setImageUrl("https://i.ytimg.com/vi/" + a + "/maxresdefault.jpg");

                        video1.setText(
                                uniqueKeySnapshot.child("Text").getValue().toString());


                        videoArrayList.add(video1);
                    }

                    // prepare data for list
                    List<YoutubeVideo> youtubeVideos = videoArrayList;


                    mRecyclerAdapter = new YoutubeRecyclerAdapter(youtubeVideos);

                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerViewFeed.setLayoutManager(mLayoutManager);
                    recyclerViewFeed.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewFeed.setAdapter(mRecyclerAdapter);

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
        linkadapter.startListening();
    }

    public class myadapter extends FirebaseRecyclerAdapter<CatSetGet, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<CatSetGet> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull CatSetGet model) {

            holder.name.setText(model.getName());

            Glide.with(holder.img.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.placeholder_restaurant)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(42)))
                    .into(holder.img);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

                name = (TextView) itemView.findViewById(R.id.textView18);
                img = (ImageView) itemView.findViewById(R.id.imageView5);

            }
        }
    }


    public class LinkAdapter extends FirebaseRecyclerAdapter<LinkDataModel, LinkAdapter.myviewholder> {
        public LinkAdapter(@NonNull FirebaseRecyclerOptions<LinkDataModel> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull LinkDataModel model) {

            Glide.with(holder.img.getContext()).
                    load(model.getImage())
                    .fitCenter()
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(38)))
                    .into(holder.img);


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!model.getLink().equals("null")) {

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(model.getLink()));
                        startActivity(intent);

                    }


                }
            });


        }

        @NonNull
        @Override
        public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.link_layout_model, parent, false);
            return new myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            ImageView img;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.image);

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

            Glide.with(holder.image.getContext()).
                    load(model.getBanner())
                    .placeholder(R.drawable.placeholder_restaurant)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(38)))
                    .apply(new RequestOptions().override(600))
                    .into(holder.image);


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
                                        .setTitle("Replace cart items?")
                                        .setMessage("Your cart have some items. Do you want to discard and change the restaurant?")
                                        .setNegativeButton(android.R.string.no, null)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface arg0, int arg1) {
                                                MainActivity.super.onBackPressed();

                                                mCartDatabase.child(uId).removeValue();

                                                Intent chatIntent = new Intent(MainActivity.this, SingleRestaurant.class);
                                                chatIntent.putExtra("restauranr_id", model.Restaurant_name);
                                                startActivity(chatIntent);
                                            }
                                        }).create().show();


                            } else {

                                Intent chatIntent = new Intent(MainActivity.this, SingleRestaurant.class);
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


    private List<YoutubeVideo> prepareList() {
        ArrayList<YoutubeVideo> videoArrayList = new ArrayList<>();
        // add first item
        YoutubeVideo video1 = new YoutubeVideo();
        video1.setImageUrl("https://i.ytimg.com/vi/zI-Pux4uaqM/maxresdefault.jpg");
        video1.setText(
                "Thugs Of Hindostan - Official Trailer | Amitabh Bachchan | Aamir Khan");
        video1.setVideoId("zI-Pux4uaqM");
        videoArrayList.add(video1);


        // add second item
        YoutubeVideo video2 = new YoutubeVideo();
        video2.setImageUrl("https://i.ytimg.com/vi/8ZK_S-46KwE/maxresdefault.jpg");
        video2.setText(
                "Colors for Children to Learning with Baby Fun Play with Color Balls Dolphin...");
        video2.setVideoId("8ZK_S-46KwE");
        // add third item
        YoutubeVideo video3 = new YoutubeVideo();
        video3.setImageUrl("https://i.ytimg.com/vi/8czMWUH7vW4/hqdefault.jpg");
        video3.setText("Air Hostess Accepts Marriage Proposal Mid-Air, Airline Fires her.");
        video3.setVideoId("8czMWUH7vW4");
        // add four item
        YoutubeVideo video4 = new YoutubeVideo();
        video4.setImageUrl("https://i.ytimg.com/vi/YrQVYEb6hcc/maxresdefault.jpg");
        video4.setText("EXPERIMENT Glowing 1000 degree METAL BALL vs Gunpowder (100 grams)");
        video4.setVideoId("YrQVYEb6hcc");
        // add four item
        YoutubeVideo video5 = new YoutubeVideo();
        video5.setImageUrl("https://i.ytimg.com/vi/S84Fuo2rGoY/maxresdefault.jpg");
        video5.setText("What happened after Jauhar of Padmavati");
        video5.setVideoId("S84Fuo2rGoY");
        videoArrayList.add(video1);
        videoArrayList.add(video2);
        videoArrayList.add(video3);
        videoArrayList.add(video4);
        return videoArrayList;
    }

}


