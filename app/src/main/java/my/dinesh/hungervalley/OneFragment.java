package my.dinesh.hungervalley;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class OneFragment extends Fragment {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private DatabaseReference mFeedDatabase;

    public OneFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.upload_list);

        mFeedDatabase = FirebaseDatabase.getInstance().getReference().child("Feeds");
        mFeedDatabase.keepSynced(true);

        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);


        /*FirebaseRecyclerAdapter<MyDataSetGet, MainActivity.FriendsViewHolder> friendsRecyclerView = new FirebaseRecyclerAdapter<MyDataSetGet, MainActivity.FriendsViewHolder>(

                MyDataSetGet.class,
                R.layout.list_feed_item,
                MainActivity.FriendsViewHolder.class,
                mFeedDatabase

        ) {
            @Override
            protected void populateViewHolder(final MainActivity.FriendsViewHolder viewHolder, MyDataSetGet model, int position) {

                final String list_user_id = getRef(position).getKey();

                mFeedDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String name = dataSnapshot.child("Name").getValue().toString();

                        viewHolder.setName(name);


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                    }
                });

            }
        };

        recyclerView.setAdapter(friendsRecyclerView);
*/



        return view;

    }
}
