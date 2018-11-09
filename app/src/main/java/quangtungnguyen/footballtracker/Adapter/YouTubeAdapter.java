package quangtungnguyen.footballtracker.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import quangtungnguyen.footballtracker.Data.VideoData;
import quangtungnguyen.footballtracker.R;

import android.app.Activity;
import android.content.Intent;

import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
/**
 * Created by ofaroque on 8/13/15.
 */
//https://androidtutorialmagic.wordpress.com/my-mini-project/multiple-youtube-video-in-recyclerviewlistview-in-android/
public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.VideoInfoHolder> {

    private static final String API_KEY = "AIzaSyC4PpbCZQoB5SZJdIHHn0r5dIntpq5en4s";
    ArrayList<VideoData> videoData;
    Context ctx;

    public YouTubeAdapter(Context context, ArrayList<VideoData> videoData) {
        this.videoData = videoData;
        this.ctx = context;
    }

    @Override
    public VideoInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_youtube_item_layout, parent, false);
        return new VideoInfoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideoInfoHolder holder, final int position) {

        holder.videoTitle.setText(videoData.get(position).getVideoTitle());

        holder.youTubeThumbnailView.initialize(API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, final YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(videoData.get(position).getVideoUrl());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailView.setVisibility(View.VISIBLE);
                        holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                        youTubeThumbnailLoader.release();
                    }
                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });

            }



            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoData.size();
    }

    public class VideoInfoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        YouTubeThumbnailView youTubeThumbnailView;
        TextView videoTitle;

        public VideoInfoHolder(View itemView) {
            super(itemView);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
            youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
            videoTitle = (TextView) itemView.findViewById(R.id.videoTitle);
            //when click on view play video
            youTubeThumbnailView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) ctx, API_KEY, videoData.get(getLayoutPosition()).getVideoUrl(),0,true,false);
            ctx.startActivity(intent);
        }

    }
}
