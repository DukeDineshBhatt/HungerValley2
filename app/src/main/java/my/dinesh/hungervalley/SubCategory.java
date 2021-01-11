package my.dinesh.hungervalley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SubCategory extends AppCompatActivity {

    int flags;
    private Toolbar toolbar;
    String cat_id, imagetxt, poss;
    int pos;
    RecyclerView subCat;
    myadapter adapter;
    ImageView image;
    private LinearLayoutManager linearLayoutManager;

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

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        cat_id = intent.getStringExtra("cat_id");
        imagetxt = intent.getStringExtra("image");
        pos = intent.getIntExtra("pos", 0);
        getSupportActionBar().setTitle(cat_id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        subCat = (RecyclerView) findViewById(R.id.subCat);
        int a = pos++;
        poss = "Cat" + pos;


        Log.d("SSSS", poss);

        linearLayoutManager = new LinearLayoutManager(this);

        subCat.setLayoutManager(linearLayoutManager);

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

            holder.res_name.setText(model.getRes());
            holder.price.setText(Integer.toString(model.getPrice()));

            String key = getRef(position).getKey();
            String type = model.getType();

            Log.d("DDDD", key);

            holder.food_name.setText(model.getFoodName());

            if (!type.equals("Non-Veg")) {

                Glide.with(holder.type_image.getContext()).load(R.drawable.veg).into(holder.type_image);


            } else {

                Glide.with(holder.type_image.getContext()).load(R.drawable.non_veg).into(holder.type_image);

            }

        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_subcat, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {
            ImageView img, type_image;
            TextView res_name, food_name, price;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.imageView5);
                res_name = (TextView) itemView.findViewById(R.id.res_name);
                food_name = (TextView) itemView.findViewById(R.id.food_name);
                price = (TextView) itemView.findViewById(R.id.price);
                type_image = (ImageView) itemView.findViewById(R.id.type_image);

            }
        }
    }


}