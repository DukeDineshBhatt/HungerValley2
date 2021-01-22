package my.dinesh.hungervalley;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<CartDataSetGet> profiles;

    public MyAdapter(Context c, ArrayList<CartDataSetGet> p) {
        context = c;
        profiles = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.pName.setText(profiles.get(position).getpName());
        holder.price.setText(profiles.get(position).getPrice().toString());
        holder.quantity.setText(profiles.get(position).getQuantity().toString());
        //holder.quantity.setText(profiles.get(position).getType().toString());

        if (profiles.get(position).getType().toString().equals("Non-Veg")) {

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


    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pName, price, quantity;
        ImageView type;

        public MyViewHolder(View itemView) {
            super(itemView);
            pName = (TextView) itemView.findViewById(R.id.name);
            price = (TextView) itemView.findViewById(R.id.price);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            type = (ImageView) itemView.findViewById(R.id.type_image);

        }


    }
}