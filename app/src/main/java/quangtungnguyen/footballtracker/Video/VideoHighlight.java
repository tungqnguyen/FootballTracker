package quangtungnguyen.footballtracker.Video;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import quangtungnguyen.footballtracker.Adapter.YouTubeAdapter;
import quangtungnguyen.footballtracker.Data.VideoData;
import quangtungnguyen.footballtracker.R;

public class VideoHighlight extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //https://stackoverflow.com/questions/22395417/error-strictmodeandroidblockguardpolicy-onnetwork?noredirect=1&lq=1
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        String teamSelected = getIntent().getStringExtra("teamName");
        // if team selected null, default is Maroon 5 music videos
        if(teamSelected == null){
            System.out.println("team select is null switch to default value Maroon 5");
            teamSelected = "Maroon 5";

        }
        setContentView(R.layout.video_highlight_layout);
        //create TeamVideos object to get videoID of that team
        TeamVideos teamVideos = new TeamVideos();
        //get video ids of the team selected
        ArrayList<VideoData> videoData = teamVideos.getVideoData(teamSelected);
        recyclerView=(RecyclerView)findViewById(R.id.video_list);
        recyclerView.setHasFixedSize(true);
        //to use RecycleView, you need a layout manager. default is LinearLayoutManager
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        //getVideoData(teamSelected);
        YouTubeAdapter adapter=new YouTubeAdapter(VideoHighlight.this,videoData );
        recyclerView.setAdapter(adapter);
    }

}
