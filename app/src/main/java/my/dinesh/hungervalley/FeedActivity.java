package my.dinesh.hungervalley;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FeedActivity extends AppCompatActivity {


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
        setContentView(R.layout.activity_feed);


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


       /* recyclerView = (RecyclerView) findViewById(R.id.upload_list);

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
                viewHolder.setIsRecyclable(false);

                final String list_user_id = getRef(position).getKey();

                mFeedDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String image = dataSnapshot.child("Image").getValue().toString();
                        String type = dataSnapshot.child("Type").getValue().toString();


                        viewHolder.setName(dataSnapshot.child("Caption").getValue().toString());
                        viewHolder.setImage(image);

                        if (type.equals("Video")) {
                            String url = dataSnapshot.child("url").getValue().toString();

                            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(FeedActivity.this, YoutubeActivity.class);
                                    intent.putExtra("url", url);
                                    startActivity(intent);

                                }
                            });
                        }


                        //for likes count
                        if (dataSnapshot.hasChild("Likes Count")) {

                            viewHolder.likes_count.setText(dataSnapshot.child("Likes Count").getValue().toString() + " likes");

                        }

                        //for comments count
                        if (dataSnapshot.hasChild("Comments Count")) {

                            viewHolder.comment.setVisibility(View.VISIBLE);
                            viewHolder.comment.setText("View all " + dataSnapshot.child("Comments Count").getValue().toString() + " comments");


                        } else {

                            viewHolder.comment.setVisibility(View.GONE);
                        }


                        if (dataSnapshot.child("Likes").child(uId).hasChild("Like")) {

                            viewHolder.like.setBackgroundResource(R.drawable.liked);

                            viewHolder.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    mFeedDatabase.child(list_user_id).child("Likes").child(uId).child("Like").removeValue();
                                    mFeedDatabase.child(list_user_id).child("Likes Count").setValue(Integer.parseInt(dataSnapshot.child("Likes Count").getValue().toString()) - 1);
                                    viewHolder.like.setBackgroundResource(R.drawable.like);


                                }
                            });


                        } else {

                            viewHolder.like.setBackgroundResource(R.drawable.like);

                            viewHolder.like.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    viewHolder.like.setBackgroundResource(R.drawable.liked);

                                    mFeedDatabase.child(list_user_id).child("Likes").child(uId).child("Like").setValue("yes");
                                    mFeedDatabase.child(list_user_id).child("Likes Count").setValue(Integer.parseInt(dataSnapshot.child("Likes Count").getValue().toString()) + 1);
                                    // viewHolder.likes_count.setText(dataSnapshot.child("Likes Count").getValue().toString() + " likes");

                                    if (dataSnapshot.hasChild("Likes Count")) {

                                        mFeedDatabase.child(list_user_id).child("Likes Count").setValue(Integer.parseInt(dataSnapshot.child("Likes Count").getValue().toString()) + 1);

                                    } else {

                                        mFeedDatabase.child(list_user_id).child("Likes Count").setValue(1);

                                    }
                                }
                            });
                        }

                        if (dataSnapshot.hasChild("Comments Count")) {

                            viewHolder.post.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View view) {
                                    String sUsername = viewHolder.editComment.getText().toString();

                                    if (sUsername.matches(" ") || sUsername.matches("")) {

                                    } else {

                                        mFeedDatabase.child(list_user_id).child("Likes").child(uId).child("Comment").push().setValue(sUsername);
                                        mFeedDatabase.child(list_user_id).child("Comments Count").setValue(Integer.parseInt(dataSnapshot.child("Comments Count").getValue().toString()) + 1);
                                        viewHolder.editComment.setText("");
                                        viewHolder.editComment.setHint("Add a comment..");

                                    }

                                }
                            });

                        } else {

                            viewHolder.post.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    mFeedDatabase.child(list_user_id).child("Likes").child(uId).child("Comment").push().setValue(viewHolder.editComment.getText().toString());
                                    viewHolder.editComment.setText(" ");

                                    if (dataSnapshot.hasChild("Comments Count")) {

                                        mFeedDatabase.child(list_user_id).child("Comments Count").setValue(Integer.parseInt(dataSnapshot.child("Comments Count").getValue().toString()) + 1);
                                        viewHolder.editComment.setHint("Add a comment..");

                                    } else {

                                        mFeedDatabase.child(list_user_id).child("Comments Count").setValue(1);
                                        viewHolder.editComment.setHint("Add a comment..");

                                    }

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };

        recyclerView.setAdapter(friendsRecyclerView);
*/

    }


    public static class FriendsViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView like;
        TextView likes_count, comment, post;
        EditText editComment;
        ImageView imageView;

        public FriendsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            like = (ImageView) itemView.findViewById(R.id.like);
            likes_count = (TextView) itemView.findViewById(R.id.like_count);
            comment = (TextView) itemView.findViewById(R.id.comment);
            post = (TextView) itemView.findViewById(R.id.post);
            editComment = (EditText) itemView.findViewById(R.id.editComment);
            imageView = (ImageView) itemView.findViewById(R.id.image);

        }

        public void setName(String name) {
            TextView userName = (TextView) mView.findViewById(R.id.caption);
            userName.setText(name);
        }

        public void setImage(String image) {


            if (!image.equals("default")) {

                Picasso
                       .get()
                        .load(image)
                        .into(imageView);

            }

        }


    }


 /*   @Override
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
    }*/
}
