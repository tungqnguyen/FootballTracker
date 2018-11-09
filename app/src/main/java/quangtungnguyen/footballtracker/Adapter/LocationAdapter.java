package quangtungnguyen.footballtracker.Adapter;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import quangtungnguyen.footballtracker.Data.MatchLocationData;
import quangtungnguyen.footballtracker.Home;
import quangtungnguyen.footballtracker.GoogleMap.MapControl;
import quangtungnguyen.footballtracker.R;

public class LocationAdapter extends RecyclerView.Adapter <LocationAdapter.LocationViewHolder> implements MapControl.OnMapClicked{
    @NonNull
    private ArrayList<MatchLocationData> itemsData;
    private Context context;
    private LatLng userLoc;
    private final OnLocationSelectedListener mListener = new OnLocationSelectedListener() {
        @Override
        public void onLocationSelected(MatchLocationData item, int position) {

            Intent intent = new Intent(context, MapControl.class);
            //intent.putExtra("teamName", teamSelected);

            intent.putExtra("location", item.getLatLng());
            intent.putExtra("locName", item.getStadiumLocationInfo());
            // send user current location, default at Monash Caufield
            intent.putExtra("userLoc", new LatLng(-37.8770, 145.0443));
            context.startActivity(intent);

        }

    };


    public LocationAdapter(Context context, ArrayList<MatchLocationData> itemsData) {
        this.itemsData = itemsData;
        this.context = context;
        // set default user location
        userLoc = new LatLng(-37.8770, 145.0443);
    }
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item_layout, null);

        // create ResultViewHolder
        LocationAdapter.LocationViewHolder viewHolder = new LocationAdapter.LocationViewHolder(itemLayoutView, mListener);
        return viewHolder;    }

    @Override
    public void onBindViewHolder(final LocationViewHolder viewHolder, final int position) {
        viewHolder.homeTeamName.setText(itemsData.get(position).getHomeTeamNameInfo());
        viewHolder.awayTeamName.setText(itemsData.get(position).getAwayTeamNameInfo());
        // calculate distance from user location to stadium
        LatLng stadiumLoc = itemsData.get(position).getLatLng();
        float[] results = new float[5];
        Location.distanceBetween(userLoc.latitude,userLoc.longitude,stadiumLoc.latitude,stadiumLoc.longitude,results);
        //https://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java?rq=1
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        Float result = results[0] / 1000;
        // display location next to stadium name
        viewHolder.stadiumName.setText(itemsData.get(position).getStadiumLocationInfo() + " (" + df.format(result) + " km)");
        viewHolder.timeInfo.setText(itemsData.get(position).getTimeInfo());
        viewHolder.dateInfo.setText(itemsData.get(position).getDateInfo());

        Home.displayTeamIcon(this.context,itemsData.get(position).getHomeTeamUrl(),viewHolder.homeIcon);
        Home.displayTeamIcon(this.context,itemsData.get(position).getAwayTeamUrl(),viewHolder.awayIcon);

    }

    @Override
    public void onMapClicked(String locName, LatLng position) {

    }

    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView homeTeamName;
        public TextView awayTeamName;
        public ImageView homeIcon;
        public ImageView awayIcon;
        public TextView stadiumName;
        public TextView timeInfo;
        public TextView dateInfo;
        private OnLocationSelectedListener mListener;

        public LocationViewHolder(View itemLayoutView, OnLocationSelectedListener listener) {
            super(itemLayoutView);
            homeTeamName = (TextView) itemLayoutView.findViewById(R.id.homeTeamNameInfo);
            awayTeamName = (TextView) itemLayoutView.findViewById(R.id.awayTeamNameInfo);
            homeIcon = (ImageView) itemLayoutView.findViewById(R.id.homeIconInfo);
            awayIcon = (ImageView) itemLayoutView.findViewById(R.id.awayIconInfo);
            stadiumName = (TextView) itemLayoutView.findViewById(R.id.stadiumLocationInfo);
            timeInfo = (TextView) itemLayoutView.findViewById(R.id.timeInfo);
            dateInfo = (TextView) itemLayoutView.findViewById(R.id.dateInfo);


            mListener = listener;
            itemLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onLocationSelected(itemsData.get(getAdapterPosition()), getAdapterPosition());
        }
    }

    @Override
    public int getItemCount() {
        return itemsData.size();
    }
    public interface OnLocationSelectedListener {
        void onLocationSelected(MatchLocationData item, int position);
    }
}
