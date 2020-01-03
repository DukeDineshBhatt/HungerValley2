package my.dinesh.hungervalley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FeedActivity extends BaseActivity {


    int flags;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private DatabaseReference mFeedDatabase;
    String uId;

    private int[] tabIcons = {
            R.drawable.one,
            R.drawable.networking

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);


        recyclerView = (RecyclerView) findViewById(R.id.upload_list);

        SharedPreferences shared = getSharedPreferences("myAppPrefs", MODE_PRIVATE);
        uId = (shared.getString("user_id", ""));

        FirebaseApp.initializeApp(this);

        mFeedDatabase = FirebaseDatabase.getInstance().getReference().child("Feeds");
        mFeedDatabase.keepSynced(true);

        linearLayoutManager = new LinearLayoutManager(FeedActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        FirebaseRecyclerAdapter<MyFeedData, FriendsViewHolder> friendsRecyclerView = new FirebaseRecyclerAdapter<MyFeedData, FriendsViewHolder>(

                MyFeedData.class,
                R.layout.list_feed_item,
                FriendsViewHolder.class,
                mFeedDatabase

        ) {
            @Override
            protected void populateViewHolder(FriendsViewHolder viewHolder, MyFeedData model, int position) {

                final String list_user_id = getRef(position).getKey();

                mFeedDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String likes = dataSnapshot.child("Likes Count").getValue().toString();

                        final String image = dataSnapshot.child("Image").getValue().toString();

                        if (dataSnapshot.child("Likes").hasChild(uId)) {

                            viewHolder.like.setBackgroundResource(R.drawable.liked);

                        } else {

                            viewHolder.like.setBackgroundResource(R.drawable.like);
                        }

                        viewHolder.likes_count.setText(likes + " likes");
                        viewHolder.setName(dataSnapshot.child("Caption").getValue().toString());
                        viewHolder.setImage(image);

                        viewHolder.like.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (dataSnapshot.child("Likes").hasChild(uId)) {

                                    viewHolder.like.setBackgroundResource(R.drawable.liked);

                                } else {
                                    viewHolder.like.setBackgroundResource(R.drawable.like);
                                }


                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });

            }
        };

        recyclerView.setAdapter(friendsRecyclerView);


    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView like;
        TextView likes_count;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            like = (ImageView) itemView.findViewById(R.id.like);
            likes_count = (TextView) itemView.findViewById(R.id.like_count);


        }


        public void setName(String name) {
            TextView userName = (TextView) mView.findViewById(R.id.name);
            userName.setText(name);
        }

        public void setImage(String image) {


            if (!image.equals("default")) {
                ImageView imageView = (ImageView) mView.findViewById(R.id.image);
                Picasso
                        .with(mView.getContext())
                        .load(image)
                        .into(imageView);

            }

        }


    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(FeedActivity.this, MainActivity.class);
        startActivity(intent);
        finish();


    }

    @Override
    int getContentViewId() {
        return R.layout.activity_feed;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.feed;
    }
}
