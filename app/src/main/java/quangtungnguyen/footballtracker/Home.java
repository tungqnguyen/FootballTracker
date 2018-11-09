package quangtungnguyen.footballtracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import quangtungnguyen.footballtracker.Adapter.ResultsAdapter;
import quangtungnguyen.footballtracker.Data.MatchData;
import quangtungnguyen.footballtracker.Data.ResultsData;
import quangtungnguyen.footballtracker.utils.FixtureResultsCallback;
import quangtungnguyen.footballtracker.utils.TeamIconCallback;
import quangtungnguyen.footballtracker.utils.TeamNameAndUrlCallback;
import quangtungnguyen.footballtracker.utils.VolleyCallback;
import quangtungnguyen.footballtracker.utils.VolleyUtils;

public class Home extends Fragment {

    Button allFixturesBtn;
    Button allResultsBtn;
    TextView dateUpcoming;
    TextView homeTeamUpcoming;
    TextView awayTeamUpcoming;
    TextView timeUpcoming;
    String urlUpcoming = "http://api.football-data.org/v2/competitions/PL/matches?status=SCHEDULED";
    String urlPast = "http://api.football-data.org/v2/competitions/PL/matches?status=FINISHED";
    ImageView homeIconUpcoming, awayIconUpcoming;
    private RecyclerView mRecyclerView;
    HashMap<String, ArrayList<String>> teamInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        teamInfo = (HashMap<String, ArrayList<String>>) bundle.getSerializable("data");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.home, container, false);
        // you have the view now --> can get reference to widgets in view
        dateUpcoming = (TextView) view.findViewById(R.id.dateUpcoming);
        homeTeamUpcoming = (TextView) view.findViewById(R.id.homeTeamUpcoming);
        awayTeamUpcoming = (TextView) view.findViewById(R.id.awayTeamUpcoming);
        timeUpcoming = (TextView) view.findViewById(R.id.timeUpcoming);
        homeIconUpcoming = (ImageView) view.findViewById(R.id.homeIconUpcoming);
        awayIconUpcoming = (ImageView) view.findViewById(R.id.awayIconUpcoming);
        allFixturesBtn = (Button) view.findViewById(R.id.allFixturesBtn);
        allResultsBtn = (Button) view.findViewById(R.id.allResultsBtn);
        // prepare reference to recycler view and initial layoutManager
        mRecyclerView = (RecyclerView) view.findViewById(R.id.latestResults);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        getData();
        return view;
    }
    //get all fixtures and listen for button click to display
    private void getData() {
        VolleyUtils.getJsonObject(getContext(), urlUpcoming, new VolleyCallback() {
            @Override
            public void onSuccess(final JSONObject result) throws JSONException {
                final JSONArray allFixtures = result.getJSONArray("matches");
                final JSONObject fixture = result.getJSONArray("matches").getJSONObject(0);
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                //convert to current time zone
                sdf.setTimeZone(TimeZone.getTimeZone("EAT"));
                final SimpleDateFormat date = new SimpleDateFormat("EEEE dd MMMM yyyy");
                final SimpleDateFormat time = new SimpleDateFormat("HH:mm");
                Date d = null;
                try {
                    d = sdf.parse(fixture.getString("utcDate"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final String formattedDate = date.format(d);
                final String formattedTime = time.format(d);

                String homeTeamName = fixture.getJSONObject("homeTeam").getString("name");
                String awayTeamName = fixture.getJSONObject("awayTeam").getString("name");
                final String homeIconUrl = teamInfo.get(homeTeamName).get(0);
                final String awayIconUrl = teamInfo.get(awayTeamName).get(0);
                // create a MatchData object corresponding to one fixture -- make this a callback and create any requiring object with given data
                MatchData match = new MatchData(formattedDate, homeTeamName, homeIconUrl,
                        awayTeamName, awayIconUrl, formattedTime, fixture.getString("status"));
                displayFixture(getContext(), match, homeTeamUpcoming, awayTeamUpcoming,
                        timeUpcoming, dateUpcoming, homeIconUpcoming, awayIconUpcoming);

                allFixturesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Fixtures.class);
                        intent.putExtra("isResults", false);
                        intent.putExtra("title", "Premier League 17/18 Fixtures");
                        intent.putExtra("teamInfo", teamInfo);
                        intent.putExtra("allFixtures", allFixtures.toString());
                        startActivity(intent);
                    }
                });
            }
        });
        VolleyUtils.getJsonObject(getContext(), urlPast, new VolleyCallback() {
            @Override
            public void onSuccess(final JSONObject result) throws JSONException {
                final JSONArray matchResults = result.getJSONArray("matches");
                displayPastResults(matchResults);
                allResultsBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), Fixtures.class);
                        intent.putExtra("isResults", true);
                        intent.putExtra("title", "Premier League 17/18 Results");
                        intent.putExtra("teamInfo", teamInfo);
                        intent.putExtra("allFixtures", matchResults.toString());
                        startActivity(intent);
                    }
                });
            }
        });

    }

    public void displayPastResults(JSONArray matchResults) throws JSONException {

        final ArrayList<ResultsData> resultsData = new ArrayList<>();
        // get all result matches
        ArrayList<JSONObject> resultMatches = new ArrayList<>();
        // only take first 5 latest results
        for (int i = matchResults.length() - 1; matchResults.length()-1-i < 5; i--) {
            resultMatches.add(matchResults.getJSONObject(i));
        }
        final ResultsAdapter adapter = new ResultsAdapter(getContext(), resultsData);
        mRecyclerView.setAdapter(adapter);
        // only display first 5 results
        for (int i = 0; i < resultMatches.size(); i++) {
            final JSONObject currentFixture = resultMatches.get(i);
            final String homeTeamName = currentFixture.getJSONObject("homeTeam").getString("name");
            final String awayTeamName = currentFixture.getJSONObject("awayTeam").getString("name");
            final JSONObject scores = currentFixture.getJSONObject("score").getJSONObject("fullTime");
            final String homeTeamCode = teamInfo.get(homeTeamName).get(1);
            String awayTeamCode = teamInfo.get(awayTeamName).get(1);
            final String homeTeamIconUrl = teamInfo.get(homeTeamName).get(0);
            final String awayTeamIconUrl = teamInfo.get(awayTeamName).get(0);
            ResultsData data = new ResultsData(homeTeamCode, awayTeamCode, scores.getString("awayTeam"),
                    scores.getString("homeTeam"), homeTeamIconUrl, awayTeamIconUrl);
            resultsData.add(data);
            adapter.notifyDataSetChanged();
        }
    }

    public static void getFixtures(JSONArray jsonFixtures, final boolean getAll, final HashMap<String, ArrayList<String>> teamInfo,
                                   final FixtureResultsCallback callback) throws JSONException, ParseException {

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        //convert to current time zone
        sdf.setTimeZone(TimeZone.getTimeZone("EAT"));
        final SimpleDateFormat date = new SimpleDateFormat("EEEE dd MMMM yyyy");
        final SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        ArrayList<MatchData> matches = new ArrayList<>();

        for (int i = 0; i < jsonFixtures.length(); i++) {
            JSONObject currentFixture = jsonFixtures.getJSONObject(i);
            String homeTeamName = currentFixture.getJSONObject("homeTeam").getString("name");
            String awayTeamName = currentFixture.getJSONObject("awayTeam").getString("name");
            String status = currentFixture.getString("status");
            // attempt to get url icon for the team, in case team is outside of Premier League, ignore fixture and print error
            try{
               String homeTeamIcon = teamInfo.get(homeTeamName).get(0);
               String awayTeamIcon = teamInfo.get(awayTeamName).get(0);
                // parse fixture date to Date object for ease of conversion
                Date d = sdf.parse(currentFixture.getString("utcDate"));
                String formattedDate = date.format(d);
                // if this fixture has finished, calculate result
                if (status.equalsIgnoreCase("FINISHED")) {
                    JSONObject scores = currentFixture.getJSONObject("score");
                    String score = scores.getJSONObject("fullTime").getString("homeTeam") + " - "
                            + scores.getJSONObject("fullTime").getString("awayTeam");
                    matches.add(new MatchData(formattedDate, homeTeamName, homeTeamIcon, awayTeamName, awayTeamIcon, score, status));
                } else {
                    // if is on scheduled then create MatchData object with time
                    String formattedTime = time.format(d);
                    matches.add(new MatchData(formattedDate, homeTeamName, homeTeamIcon, awayTeamName, awayTeamIcon, formattedTime, status));
                }
                if (!getAll) {
                    break;
                }
            }
            catch (NullPointerException e){
                e.printStackTrace();
                continue;
            }


        }
        callback.onFinished(matches);
    }

    public static void displayFixture(final Context context, MatchData fixture, final TextView homeTeamName,
                                      final TextView awayTeamName, final TextView timeOrResult,
                                      final TextView fixtureDate, final ImageView homeIcon,
                                      final ImageView awayIcon) {
        homeTeamName.setText(fixture.getHomeNameFixture());
        awayTeamName.setText(fixture.getAwayNameFixture());
        timeOrResult.setText(fixture.getTimeOrResultFixture());
        fixtureDate.setText(fixture.getDateFixture());
        displayTeamIcon(context, fixture.getHomeIconFixture(), homeIcon);
        displayTeamIcon(context, fixture.getAwayIconFixture(), awayIcon);
    }

    public static void displayTeamIcon(final Context context, String teamUrl, final ImageView placeholder) {
        Glide.with(context).load(teamUrl).into(placeholder);
    }

    // return hash map that has key as team name with code and image url as value
    public static void getTeamIconUrls(Context context, final String leagueTeamUrl, final TeamIconCallback callback) {
        VolleyUtils.getJsonObject(context, leagueTeamUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                HashMap<String, ArrayList<String>> teamIconUrls = new HashMap<>();
                JSONArray teams = result.getJSONArray("teams");
                for (int i = 0; i < teams.length(); i++) {
                    String iconUrl = teams.getJSONObject(i).getString("crestUrl");
                    String teamName = teams.getJSONObject(i).getString("name");
                    String teamCode = teams.getJSONObject(i).getString("tla");
                    String teamFixture = Home.getTeamUrl(teams.getJSONObject(i).getString("id")) + "/matches";
                    //System.out.println(teamFixture);
                    ArrayList<String> values = new ArrayList<>();
                    values.add(iconUrl);
                    values.add(teamCode);
                    values.add(teamFixture);
                    teamIconUrls.put(teamName, values);
                }
                callback.onReturnTeamInfo(teamIconUrls);
            }
        });
    }

    public static String getTeamUrl(String teamId) {
        return "http://api.football-data.org/v2/teams/" + teamId;
    }


}


