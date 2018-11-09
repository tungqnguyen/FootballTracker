package quangtungnguyen.footballtracker.GoogleMap;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import quangtungnguyen.footballtracker.Adapter.LocationAdapter;
import quangtungnguyen.footballtracker.Data.MatchLocationData;
import quangtungnguyen.footballtracker.Home;
import quangtungnguyen.footballtracker.R;
import quangtungnguyen.footballtracker.utils.TeamIconCallback;
import quangtungnguyen.footballtracker.utils.VolleyCallback;
import quangtungnguyen.footballtracker.utils.VolleyUtils;


public class NearbyStadium extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener{
    //private ArrayList<MatchLocationData> mSavedLocations;
    //private LocationAdapter.OnLocationSelectedListener mListener;
    private RecyclerView mRecyclerView;
    //private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] stadiums = {"Hisense Arena", "MCG","AAMI Park","Etihad Stadium", "Rod Laver Arena", "Dandenong Stadium"};
    String urlUpcoming = "http://api.football-data.org/v2/competitions/PL/matches?status=SCHEDULED";

    private ArrayList<LatLng> locations = new ArrayList<>();

    // Min and Max Update Intervals for our Location Service
    private static final long MAX_UPDATE_INTERVAL = 10000; // 10 Seconds
    private static final long MIN_UPDATE_INTERVAL = 2000; // 2 Seconds
    // Request code we will be checking for
    private static final int LOCATION_REQUEST_CODE = 1337;
    private GoogleApiClient mAPIClient;
    private boolean mCanAccessLocation;
    private LatLng mCurrentLocation;
    HashMap<String, ArrayList<String>> teamInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        teamInfo = (HashMap<String, ArrayList<String>>) bundle.getSerializable("data");

        mCanAccessLocation =
                (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED);
        // If we do not have permissions then request them
        if(!mCanAccessLocation) {
            requestPermissions();
        } else {
            setupAPIClient();
        }
        initLocations();
        // set to Caufield as default location
        mCurrentLocation = new LatLng(-37.8770, 145.0443);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View v = lf.inflate(R.layout.fragment_nearby_stadium, container,
                false);
        displayUpcomingMatchLocations(urlUpcoming);
        mRecyclerView = v.findViewById(R.id.locationRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        return v;
    }

    public void displayUpcomingMatchLocations(final String url){
        final ArrayList<MatchLocationData> matchResults = new ArrayList<>();
        VolleyUtils.getJsonObject(getContext(), url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                final JSONArray fixtures = result.getJSONArray("matches");
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                final SimpleDateFormat date = new SimpleDateFormat("dd/MM");
                final SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                sdf.setTimeZone(TimeZone.getTimeZone("EAT"));
                //only get first 5 results if available
                if(fixtures.length() != 0){
                    for (int i = 0; i < fixtures.length(); i++){
                        JSONObject currentFixture = fixtures.getJSONObject(i);
                        try {
                            Date d = sdf.parse(currentFixture.getString("utcDate"));
                            String formattedDate = date.format(d);
                            String formattedTime = time.format(d);
                            String homeTeamName = currentFixture.getJSONObject("homeTeam").getString("name");
                            String awayTeamName = currentFixture.getJSONObject("awayTeam").getString("name");
                            final String homeUrl = teamInfo.get(homeTeamName).get(0);
                            final String awayUrl = teamInfo.get(awayTeamName).get(0);
                            // the current api does not support match stadium, hence we have to fake locations
                            matchResults.add(new MatchLocationData(
                                    locations.get(i), homeTeamName,awayTeamName, stadiums[i],formattedTime,formattedDate,homeUrl,awayUrl));
                        } catch (ParseException| NullPointerException e) {
                            e.printStackTrace();
                            break;
                        }
                        //only take first 5 upcoming matches
                        if(matchResults.size() >= 5){
                            break;
                        }
                    }
                }
                //set adapter after we have successfully fetch data
                LocationAdapter adapter = new LocationAdapter(getContext(),matchResults);
                mRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void initLocations(){
        locations.add(new LatLng(-37.82229,144.983211));
        locations.add(new LatLng(-37.819954, 144.983398));
        locations.add(new LatLng(-37.825132,144.983782));
        locations.add(new LatLng(-37.817,144.946));
        locations.add(new LatLng(-37.82162,144.978536));
        locations.add(new LatLng(-37.910524,145.136218));
    }

    public void requestPermissions() {
        // We are checking if we need permission for fine location
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)) {
// http://stackoverflow.com/questions/41310510/what-is-the-differencebetween-
//            shouldshowrequestpermissionrationale-and-requestp
            new AlertDialog.Builder(getContext())
                    .setTitle("Permission required")
                    .setMessage("This is a map application. You need to enable" +
                            " location services for it to work!")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    LOCATION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {@Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                    })
                    .show();
        } else {
            // We do not need to show the user info we can just request permission
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        // Check with request code has been given to us
        switch (requestCode) {
            case LOCATION_REQUEST_CODE:
                // This is a location permission request so lets handle it
                if(grantResults.length > 0) {
                    // Can access coarse is equal to
                    mCanAccessLocation = (grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED);
                    System.out.println("permission granted onRequestPermissionResult ");
                }
                break;
            default:
                break;
        }
        // If at this point we have permissions for location attempt to start it
        setupAPIClient();
    }
    private void setupAPIClient() {
        if(mCanAccessLocation) {
// Check to see if our APIClient is null.
            if(mAPIClient == null) {
// Create our API Client and tell it to connect to Location Services
                mAPIClient = new GoogleApiClient.Builder(getContext())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                mAPIClient.connect();
            }
        } else {
// Display error saying we cannot start location service without permission
            Toast.makeText(getContext(), "Locations will not be displayed without permissions",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
    // This is our function that is called whenever we change locations
    // Update our current location variable
        mCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }
    @Override
    public void onStart() {
        if(mAPIClient != null)
            mAPIClient.connect();
        super.onStart();
    }
    @Override
    public void onStop() {
        if(mAPIClient != null)
            mAPIClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
// Android 6.0 & up added security permissions
// If the user rejects allowing access to location data then this try block
// will stop the application from crashing (Will not track location)
        try {
            // Set up a constant updater for location services
            LocationRequest locationRequest = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(MAX_UPDATE_INTERVAL)
                    .setFastestInterval(MIN_UPDATE_INTERVAL);
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mAPIClient, locationRequest, this);
        }
        catch (SecurityException secEx) {
            Toast.makeText(getContext(), "ERROR: Please enable location services",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
