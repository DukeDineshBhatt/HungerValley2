package my.dinesh.hungervalley;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.dinesh.hungervalley.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pierfrancescosoffritti.youtubeplayer.player.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.youtubeplayer.player.YouTubePlayerView;


import java.util.List;
import java.util.List;

public class YoutubeRecyclerAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    public static final int VIEW_TYPE_NORMAL = 1;
    private List<YoutubeVideo> mYoutubeVideos;
    DisplayMetrics displayMetrics = new DisplayMetrics();

    public YoutubeRecyclerAdapter(List<YoutubeVideo> youtubeVideos) {
        mYoutubeVideos = youtubeVideos;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_youtube_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (mYoutubeVideos != null && mYoutubeVideos.size() > 0) {
            return mYoutubeVideos.size();
        } else {
            return 1;
        }
    }

    public void setItems(List<YoutubeVideo> youtubeVideos) {
        mYoutubeVideos = youtubeVideos;
        notifyDataSetChanged();
    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.textViewTitle)
        TextView textWaveTitle;
        @BindView(R.id.btnPlay)
        ImageView playButton;
        @BindView(R.id.imageViewItem)
        ImageView imageViewItems;
        @BindView(R.id.youtube_view)
        YouTubePlayerView youTubePlayerView;
        @BindView(R.id.parent_layout)
        ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void clear() {
        }

        public void onBind(int position) {
            super.onBind(position);
            final YoutubeVideo mYoutubeVideo = mYoutubeVideos.get(position);
            ((Activity) itemView.getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            if (mYoutubeVideo.getText() != null) {
                textWaveTitle.setText(mYoutubeVideo.getText());
            }
            if (mYoutubeVideo.getImageUrl() != null) {
                Glide.with(itemView.getContext())
                        .load(mYoutubeVideo.getImageUrl()).
                        apply(new RequestOptions().override(width - 36, 700))
                        .into(imageViewItems);
            }
            imageViewItems.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.VISIBLE);
            youTubePlayerView.setVisibility(View.GONE);

            //7895665801

            playButton.setOnClickListener(view -> {
                imageViewItems.setVisibility(View.GONE);
                playButton.setVisibility(View.GONE);
                youTubePlayerView.setVisibility(View.VISIBLE);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(R.id.textViewTitle, ConstraintSet.TOP, R.id.youtube_view, ConstraintSet.BOTTOM, 0);
                constraintSet.applyTo(constraintLayout);

                youTubePlayerView.getLayoutParams().height = 700;
                youTubePlayerView.requestLayout();



                youTubePlayerView.initialize(
                        initializedYouTubePlayer -> initializedYouTubePlayer.addListener(
                                new AbstractYouTubePlayerListener() {
                                    @Override
                                    public void onReady() {
                                        initializedYouTubePlayer.loadVideo(mYoutubeVideo.getVideoId(), 0);
                                    }
                                }), true);


            });
        }
    }
}