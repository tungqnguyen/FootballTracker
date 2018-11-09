package quangtungnguyen.footballtracker.Adapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;

import java.util.ArrayList;

import quangtungnguyen.footballtracker.R;
import quangtungnguyen.footballtracker.Data.ResultsData;

//http://hmkcode.com/android-simple-recyclerview-widget-example/
public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder> {
    private ArrayList<ResultsData> itemsData;
    private Context context;

    public ResultsAdapter( Context context, ArrayList<ResultsData> itemsData) {
        this.itemsData = itemsData;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.past_results_item_layout, null);

        // create ResultViewHolder
        ResultViewHolder viewHolder = new ResultViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ResultViewHolder viewHolder, final int position) {
        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData
        viewHolder.homeTeamName.setText(itemsData.get(position).getHomeTeamName());
        viewHolder.awayTeamName.setText(itemsData.get(position).getAwayTeamName());
        viewHolder.result.setText(itemsData.get(position).getHomeGoal() + " - " + itemsData.get(position).getAwayGoal());
        Glide.with(context).load(itemsData.get(position).getIconHomeTeam()).into(viewHolder.homeIcon);
        Glide.with(context).load(itemsData.get(position).getIconAwayTeam()).into(viewHolder.awayIcon);
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ResultViewHolder extends RecyclerView.ViewHolder {

        public TextView homeTeamName;
        public TextView awayTeamName;
        public ImageView homeIcon;
        public ImageView awayIcon;
        public TextView result;

        public ResultViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            homeTeamName = (TextView) itemLayoutView.findViewById(R.id.homeTeamName);
            awayTeamName = (TextView) itemLayoutView.findViewById(R.id.awayTeamName);
            homeIcon = (ImageView) itemLayoutView.findViewById(R.id.homeIcon);
            awayIcon = (ImageView) itemLayoutView.findViewById(R.id.awayIcon);
            result = (TextView) itemLayoutView.findViewById(R.id.result);

        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }
}