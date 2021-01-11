package my.dinesh.hungervalley;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class Product extends AppCompatActivity {

    String cat_id, poss, cat;
    int flags;
    int pos;
    private Toolbar toolbar;
    RecyclerView product;

    myadapter adapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        flags = getWindow().getDecorView().getSystemUiVisibility(); // get current flag
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;   // add LIGHT_STATUS_BAR to flag
        getWindow().getDecorView().setSystemUiVisibility(flags);
        getWindow().setStatusBarColor(Color.WHITE);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        cat_id = intent.getStringExtra("cat_id");
        cat = intent.getStringExtra("cat");

        pos = intent.getIntExtra("pos", 0);
        getSupportActionBar().setTitle(cat_id);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        product = (RecyclerView) findViewById(R.id.subCat);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });

        int a = pos++;
        poss = "SubCat" + pos;

        linearLayoutManager = new LinearLayoutManager(Product.this);
        product.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<ProductModel> options =
                new FirebaseRecyclerOptions.Builder<ProductModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Groceries").child("Categories").child(cat).child("SubCat").child(poss).child("Products"), ProductModel.class)
                        .build();

        adapter = new myadapter(options);
        product.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }


    public class myadapter extends FirebaseRecyclerAdapter<ProductModel, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<ProductModel> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ProductModel model) {

            holder.name.setText(model.getName());

            if (model.getStock().equals("In Stock")) {
                holder.add.setEnabled(true);
            } else {
                holder.add.setEnabled(false);
            }

            holder.price.setText(Html.fromHtml("<font color=\"#000000\"><b>\u20B9  " + String.valueOf(model.getPrice())   +  " &#160 </b></font><strike>\u20B9 " + model.getDiscount() + "</strike>"));

            holder.stock.setText(model.getStock());

            Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(Product.this, "Added to cart.", Toast.LENGTH_SHORT).show();

                }
            });
        }

        @NonNull
        @Override
        public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_model, parent, false);
            return new myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {
            ImageView img;
            TextView name, price, stock;
            Button add;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                img = (ImageView) itemView.findViewById(R.id.imageView4);
                name = (TextView) itemView.findViewById(R.id.textView12);
                price = (TextView) itemView.findViewById(R.id.textView11);
                stock = (TextView) itemView.findViewById(R.id.textView64);
                add = itemView.findViewById(R.id.button5);

            }
        }
    }

}