package quangtungnguyen.footballtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import quangtungnguyen.footballtracker.Adapter.FixtureAdapter;
import quangtungnguyen.footballtracker.Data.MatchData;
import quangtungnguyen.footballtracker.utils.FixtureResultsCallback;

public class Fixtures extends AppCompatActivity {
    //    private String url;
    private RecyclerView recyclerView;
    private ArrayList<MatchData> fixtures;
    private boolean isResults;
    private String title;
    private TextView titleFixture;
    //    private  String teamUrls;
    HashMap<String, ArrayList<String>> teamInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_fixtures);
        titleFixture = (TextView) findViewById(R.id.titleFixture);
        // check if the intent is asked to display team fixture or competition fixtures
        isResults = getIntent().getExtras().getBoolean("isResults");
        title = getIntent().getStringExtra("title");
        titleFixture.setText(title);
        fixtures = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.fixturesLists);
        recyclerView.setHasFixedSize(true);
        //to use RecycleView, you need a layout manager. default is LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        final FixtureAdapter mAdapter = new FixtureAdapter(fixtures, this);
        recyclerView.setAdapter(mAdapter);
        teamInfo = (HashMap<String, ArrayList<String>>) getIntent().getSerializableExtra("teamInfo");
        try {
            JSONArray array = new JSONArray(getIntent().getStringExtra("allFixtures"));
            Home.getFixtures(array, true, teamInfo, new FixtureResultsCallback() {
                @Override
                public void onFinished(ArrayList<MatchData> matches) {
                    if (!isResults) {
                        for (MatchData match : matches) {
                            if (match.getStatus().equalsIgnoreCase("SCHEDULED")) {
                                fixtures.add(match);
                            }
                        }
                    }
                    // if is a team fixture then sort latest results to older results
                    else {
                        for (int i = matches.size() - 1; i >= 0; i--) {
                            if (!matches.get(i).getStatus().equalsIgnoreCase("SCHEDULED")) {
                                fixtures.add(matches.get(i));
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }
}
