package quangtungnguyen.footballtracker.GoogleMap;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import quangtungnguyen.footballtracker.R;

// Week 6 tutorial FIT 3027 - Android version
//https://www.youtube.com/watch?v=CCZPUeY94MU&t=631s
public class MapControl extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    private GoogleMap mMap;
    private LatLng userLoc = null;
    private LatLng mCurrentLoc;
    private String locName;
    private ArrayList<LatLng> mList = new ArrayList<>();
    private static final int LOCATION_REQUEST = 500;
    //add key
    String key = "&key=AIzaSyC4PpbCZQoB5SZJdIHHn0r5dIntpq5en4s";

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map_control);
        mCurrentLoc = getIntent().getExtras().getParcelable("location");
        locName = getIntent().getStringExtra("locName");
        userLoc = getIntent().getExtras().getParcelable("userLoc");

        System.out.println("locName from mapControl " + locName);
        System.out.println("location from mapControl " + mCurrentLoc);
        System.out.println("userLoc from mapControl " + userLoc);

        SupportMapFragment mapFrag = (SupportMapFragment) this.getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFrag.getMapAsync(this);
    }


    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        String url = getRequestUrl(userLoc, mCurrentLoc);
        TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
        taskRequestDirections.execute(url);
        mMap.setOnMapLongClickListener(this);

        // initial user location with marker
        mMap.addMarker( new MarkerOptions().position(userLoc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        // initial destination location
        setTargetLocation();
        setFocus(userLoc);
    }
    public void setTargetLocation() {
            //mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(mCurrentLoc)
                    .title(locName));
    }
    public void setFocus(LatLng loc) {
        if(mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        }
    }


        @Override
    public void onMapLongClick(final LatLng latLng) {
            // set current user location based on user long click for testing purpose since emulator can't locate current location
            MarkerOptions marker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            if(userLoc != null){
                mMap.clear();
                setTargetLocation();
                // add marker for user location
                mMap.addMarker(marker);
                userLoc = latLng;
            }
            else{
                mMap.addMarker(marker);
                userLoc = latLng;
                setFocus(latLng);
            }

            String url = getRequestUrl(userLoc, mCurrentLoc);
            TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
            taskRequestDirections.execute(url);
    }
    //https://www.youtube.com/watch?v=CCZPUeY94MU&t=631s
    private String getRequestUrl(LatLng origin, LatLng dest) {
        //Value of origin
        String str_org = "origin=" + origin.latitude +","+origin.longitude;
        //Value of destination
        String str_dest = "destination=" + dest.latitude+","+dest.longitude;
        //Set value enable the sensor
        String sensor = "sensor=false";
        //Mode for find direction
        String mode = "mode=driving";
        //Build the full param
        String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode;
        //Output format
        String output = "json";

        //Create url to request
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param + key;
        return url;
    }

    //https://www.youtube.com/watch?v=jg1urt3FGCY&t=1098s
    private String requestDirection(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try{
            URL url = new URL(reqUrl);
            System.out.println("google map request API " + reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            //Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }
    //https://www.youtube.com/watch?v=jg1urt3FGCY&t=1098s
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }
    //https://www.youtube.com/watch?v=jg1urt3FGCY&t=1098s
    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try {
                responseString = requestDirection(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }
//https://www.youtube.com/watch?v=jg1urt3FGCY&t=1098s
    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }
//https://www.youtube.com/watch?v=jg1urt3FGCY&t=1098s
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }
    public interface OnMapClicked {
        void onMapClicked(String locName, LatLng position);
    }
}

