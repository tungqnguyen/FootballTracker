package quangtungnguyen.footballtracker.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import quangtungnguyen.footballtracker.Data.MatchData;
import quangtungnguyen.footballtracker.Home;
import quangtungnguyen.footballtracker.R;

public class FixtureAdapter extends RecyclerView.Adapter<FixtureAdapter.FixtureItemViewHolder>{

    private ArrayList<MatchData> itemsData;
    private Context context;

    public FixtureAdapter(final ArrayList<MatchData> itemsData, Context context) {
        this.context = context;
        this.itemsData = itemsData;

    }
    public FixtureItemViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fixture_item_layout, null);
        // create ResultViewHolder
        FixtureItemViewHolder viewHolder = new FixtureItemViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder( FixtureItemViewHolder holder, int position) {
        Home.displayFixture(context,itemsData.get(position),holder.homeNameFixture,holder.awayNameFixture, holder.resultOrTimeFixture,
                holder.dateFixture, holder.homeIconFixture, holder.awayIconFixture);

    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }
    // inner class to hold a reference to each item of RecyclerView
    public static class FixtureItemViewHolder extends RecyclerView.ViewHolder {

        public TextView homeNameFixture;
        public TextView awayNameFixture;
        public ImageView homeIconFixture;
        public ImageView awayIconFixture;
        public TextView resultOrTimeFixture;
        public TextView dateFixture;

        public FixtureItemViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            homeIconFixture = (ImageView) itemLayoutView.findViewById(R.id.homeIconFixture);
            awayIconFixture = (ImageView) itemLayoutView.findViewById(R.id.awayIconFixture);
            homeNameFixture = (TextView) itemLayoutView.findViewById(R.id.homeNameFixture);
            awayNameFixture = (TextView) itemLayoutView.findViewById(R.id.awayNameFixture);
            resultOrTimeFixture = (TextView) itemLayoutView.findViewById(R.id.result_or_timeFixture);
            dateFixture = (TextView) itemLayoutView.findViewById(R.id.dateFixture);
        }
    }}
