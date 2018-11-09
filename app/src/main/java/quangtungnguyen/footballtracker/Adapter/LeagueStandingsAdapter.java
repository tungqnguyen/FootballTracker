package quangtungnguyen.footballtracker.Adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bumptech.glide.Glide;


import java.util.ArrayList;

import quangtungnguyen.footballtracker.R;
import quangtungnguyen.footballtracker.Data.TeamData;

//http://hmkcode.com/android-simple-recyclerview-widget-example/
public class LeagueStandingsAdapter extends RecyclerView.Adapter<LeagueStandingsAdapter.ViewHolder> {
    private ArrayList<TeamData> itemsData;
    private Context context;

    public LeagueStandingsAdapter(ArrayList<TeamData> itemsData, Context context) {
        this.context = context;
        this.itemsData = itemsData;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LeagueStandingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tables_item_layout, null);

        // create ResultViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        viewHolder.teamName.setText(itemsData.get(position).getTeamName());
        viewHolder.teamPos.setText(itemsData.get(position).getTeamPos());
        viewHolder.played.setText(itemsData.get(position).getPlayed());
        viewHolder.wins.setText(itemsData.get(position).getWins());
        viewHolder.draws.setText(itemsData.get(position).getDraws());
        viewHolder.losses.setText(itemsData.get(position).getLosses());
        viewHolder.goalDiff.setText(itemsData.get(position).getGoalDiff());
        viewHolder.points.setText(itemsData.get(position).getPoints());

        Glide.with(context).load(itemsData.get(position).getTeamUrl()).into(viewHolder.teamIcon);
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView teamPos;
        public TextView teamName;
        public TextView wins;
        public TextView draws;
        public TextView losses;
        public TextView goalDiff;
        public TextView played;
        public TextView points;
        public ImageView teamIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            teamIcon = (ImageView) itemLayoutView.findViewById(R.id.teamStandingIcon);
            teamPos = (TextView) itemLayoutView.findViewById(R.id.teamPos);
            teamName = (TextView) itemLayoutView.findViewById(R.id.teamName);
            played = (TextView) itemLayoutView.findViewById(R.id.played);
            wins = (TextView) itemLayoutView.findViewById(R.id.wins);
            draws = (TextView) itemLayoutView.findViewById(R.id.draws);
            losses = (TextView) itemLayoutView.findViewById(R.id.losses);
            goalDiff = (TextView) itemLayoutView.findViewById(R.id.goalDiff);
            points = (TextView) itemLayoutView.findViewById(R.id.points);

        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}