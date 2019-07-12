package my.dinesh.hungervalley;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class MainSliderAdapter extends SliderAdapter {

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder viewHolder) {
        switch (position) {
            case 0:
                viewHolder.bindImageSlide("https://firebasestorage.googleapis.com/v0/b/hungervalley-89d92.appspot.com/o/Main%20Banner%2Fbackground-board-chillies-1435895.jpg?alt=media&token=d7eb0669-aa36-4b40-9e2e-e7c3903ab696");
                break;
            case 1:
                viewHolder.bindImageSlide("https://firebasestorage.googleapis.com/v0/b/hungervalley-89d92.appspot.com/o/Main%20Banner%2FUPDATE.jpg?alt=media&token=128c4589-476e-419d-a665-604d283b928b");
                break;
            case 2:
                viewHolder.bindImageSlide("https://firebasestorage.googleapis.com/v0/b/hungervalley-89d92.appspot.com/o/Main%20Banner%2Fcake-chocolate-chocolate-cake-132694.jpg?alt=media&token=6e6f7cd3-6e16-4c53-bf13-31bd5316862b");
                break;

            case 3:
                viewHolder.bindImageSlide("https://firebasestorage.googleapis.com/v0/b/hungervalley-89d92.appspot.com/o/Main%20Banner%2Fbackground-board-chilliesOOOO-1435895.jpg?alt=media&token=8a72671d-e09e-4617-b2ac-1432ea3680c2");
                break;
        }
    }
}