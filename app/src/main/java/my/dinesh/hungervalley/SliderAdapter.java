package my.dinesh.hungervalley;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<Integer> color;
    private List<String> colorName;
    List<String> mSliderItems;
    LinkDataModel model;

    public SliderAdapter(Context context,  List<String> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;

    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);


        Glide.with(imageView.getContext())
                .load(model.getImage())
                .placeholder(R.drawable.placeholder_restaurant)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(42)))
                .into(imageView);

        linearLayout.setBackgroundColor(color.get(position));

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager viewPager = (ViewPager) container;
        View view = (View) object;
        viewPager.removeView(view);
    }
}