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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class SubCategory extends AppCompatActivity {

    int flags;
    private Toolbar toolbar;
    String cat_id,imagetxt,poss;
    int pos;
    RecyclerView subCat;
    myadapter adapter;
    ImageView image;

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
        pos = intent.getIntExtra("pos",0);
        getSupportActionBar().setTitle(cat_id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        subCat = (RecyclerView) findViewById(R.id.subCat);


        int a = pos++;
        poss = "Cat"+pos;

        Log.d("SSSS",poss);
        GridLayoutManager manager5 = new GridLayoutManager(this, 2);
        subCat.setLayoutManager(manager5);


        FirebaseRecyclerOptions<CatSetGet> options =
                new FirebaseRecyclerOptions.Builder<CatSetGet>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Groceries").child("Categories").child(poss).child("SubCat"), CatSetGet.class)
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

    public class myadapter extends FirebaseRecyclerAdapter<CatSetGet,myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<CatSetGet> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, int position, @NonNull CatSetGet model) {

            holder.name.setText(model.getName());

            Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(SubCategory.this, Product.class);
                    intent.putExtra("cat_id", model.getName());
                    intent.putExtra("pos",position);
                    intent.putExtra("cat",poss);
                    startActivity(intent);

                }
            });
        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_model2, parent, false);
            return new myadapter.myviewholder(view);
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


}