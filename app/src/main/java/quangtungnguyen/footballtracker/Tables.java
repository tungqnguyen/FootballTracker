package quangtungnguyen.footballtracker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import quangtungnguyen.footballtracker.Adapter.LeagueStandingsAdapter;
import quangtungnguyen.footballtracker.Data.TeamData;
import quangtungnguyen.footballtracker.utils.VolleyCallback;
import quangtungnguyen.footballtracker.utils.VolleyUtils;

public class Tables extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<TeamData> teamsData = new ArrayList<>();
    HashMap<String, ArrayList<String>> teamInfo;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        teamInfo = (HashMap<String, ArrayList<String>>) bundle.getSerializable("data");
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =  lf.inflate(R.layout.tables, container, false);

        final Spinner spinner = (Spinner) view.findViewById(R.id.leagueSpinner);
        // in the future we will support table from multiple league, for now it will only display Premier League 17/18
        String[] spinnerArray = {"Premier League 17/18","La Liga 17/18", "Bundesliga 17/18"};
        //now set adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String selectedItem = parent.getItemAtPosition(position).toString(); //this is your selected item
                if(selectedItem.equalsIgnoreCase("Premier League 17/18")) {
                    System.out.println("reached from case "+ selectedItem);
                    getTables("http://api.football-data.org/v1/competitions/445/leagueTable");
                }
                if(selectedItem.equalsIgnoreCase("La Liga 17/18")) {
                    System.out.println("reached from case "+ selectedItem);
                    getTables("http://api.football-data.org/v1/competitions/455/leagueTable");
                }
                if(selectedItem.equalsIgnoreCase("Bundesliga 17/18")) {
                System.out.println("reached from case "+ selectedItem);
                getTables("http://api.football-data.org/v1/competitions/452/leagueTable");
                }

            }
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.leagueTables);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LeagueStandingsAdapter(teamsData, this.getContext());
        mRecyclerView.setAdapter(mAdapter);
        //getTables("http://api.football-data.org/v1/competitions/445/leagueTable");
        return view;
    }

    public void getTables(String url){
        VolleyUtils.getJsonObject(getContext(), url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                teamsData.clear();
                teamsData.add(new TeamData("Pos", "Club", "P", "W","D", "L", "GD", "Pts", null));
                JSONArray standing = result.getJSONArray("standing");
                for(int i = 0; i < standing.length(); i++){
                    JSONObject currentTeam = standing.getJSONObject(i);
                    String teamUrl =  currentTeam.getString("crestURI");
                    teamsData.add(new TeamData(Integer.toString(currentTeam.getInt("position")), currentTeam.getString("teamName"),
                            Integer.toString(currentTeam.getInt("playedGames")), Integer.toString(currentTeam.getInt("wins")),
                            Integer.toString(currentTeam.getInt("draws")), Integer.toString(currentTeam.getInt("losses")),
                            Integer.toString(currentTeam.getInt("goalDifference")),Integer.toString(currentTeam.getInt("points")), teamUrl));
                }
                mAdapter.notifyDataSetChanged();

            }

        });
    }



}
