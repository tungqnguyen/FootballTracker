package quangtungnguyen.footballtracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import quangtungnguyen.footballtracker.Data.ResultsData;
import quangtungnguyen.footballtracker.Video.VideoHighlight;
import quangtungnguyen.footballtracker.utils.TeamIconCallback;
import quangtungnguyen.footballtracker.utils.VolleyCallback;
import quangtungnguyen.footballtracker.utils.VolleyUtils;

public class MyTeam extends Fragment {
    SharedPreferences mPrefs;
    public static final String teamPref = "teamPref";
    public static final String teamFixtureUrl = "teamFixtureUrl";
    public static final String LEAGUELINK = "http://api.football-data.org/v2/competitions/2021/teams";
    public static final String LEAGUESTANDING = "http://api.football-data.org/v2/competitions/2021/standings";
    HashMap<String, ArrayList<String>> teamInfo;


    TextView currentTeamTextView;
    TextView teamStanding, teamPoints;
    TextView resultDate1, resultDate2, resultDate3;
    TextView homeTeam1, homeTeam2, homeTeam3;
    TextView awayTeam1, awayTeam2, awayTeam3;
    TextView result1, result2, result3;
    ImageView favTeamIcon;
    ImageView homeIcon1, homeIcon2, homeIcon3;
    ImageView awayIcon1, awayIcon2, awayIcon3;
    CardView videoHighLights;
    CardView aboutThisApp;
    Button editBtn;
    ImageView resultsBtn;

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
        View view = lf.inflate(R.layout.my_team, container, false);
        teamStanding = view.findViewById(R.id.teamStanding);
        teamPoints = view.findViewById(R.id.teamPoints);
        favTeamIcon = view.findViewById(R.id.favTeamIcon);
        resultDate1 = view.findViewById(R.id.resultDate1);
        resultDate2 = view.findViewById(R.id.resultDate2);
        resultDate3 = view.findViewById(R.id.resultDate3);
        result1 = view.findViewById(R.id.result1);
        result2 = view.findViewById(R.id.result2);
        result3 = view.findViewById(R.id.result3);
        homeTeam1 = view.findViewById(R.id.homeTeam1);
        homeTeam2 = view.findViewById(R.id.homeTeam2);
        homeTeam3 = view.findViewById(R.id.homeTeam3);
        awayTeam1 = view.findViewById(R.id.awayTeam1);
        awayTeam2 = view.findViewById(R.id.awayTeam2);
        awayTeam3 = view.findViewById(R.id.awayTeam3);
        homeIcon1 = view.findViewById(R.id.homeIcon1);
        homeIcon2 = view.findViewById(R.id.homeIcon2);
        homeIcon3 = view.findViewById(R.id.homeIcon3);
        awayIcon1 = view.findViewById(R.id.awayIcon1);
        awayIcon2 = view.findViewById(R.id.awayIcon2);
        awayIcon3 = view.findViewById(R.id.awayIcon3);
        currentTeamTextView = view.findViewById(R.id.currentTeamTextView);
        aboutThisApp = view.findViewById(R.id.aboutThisApp);
        aboutThisApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AboutPage.class);
                startActivity(intent);
            }
        });
        resultsBtn = view.findViewById(R.id.resultsBtn);
        editBtn = view.findViewById(R.id.editBtn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getTeams();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        videoHighLights = view.findViewById(R.id.videoHighlights);
        videoHighLights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                final String teamSelected = mPrefs.getString(teamPref, null);
                Intent intent = new Intent(getContext(), VideoHighlight.class);
                intent.putExtra("teamName", teamSelected);
                startActivity(intent);
            }
        });
        final String teamSelected = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(teamPref, null);
        if (teamSelected != null) {
            currentTeamTextView.setText(teamSelected);
            displayTeamStanding(teamSelected);
            Home.displayTeamIcon(getContext(), teamInfo.get(teamSelected).get(0), favTeamIcon);
            getFixtureResults(teamInfo.get(teamSelected).get(2) + "?status=FINISHED", teamInfo);
        }
        return view;

    }

    //https://stackoverflow.com/questions/10024739/how-to-determine-when-fragment-becomes-visible-in-viewpager
    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            final String teamSelected = mPrefs.getString(teamPref, null);
            //if first shown and user havent picked team yet
            if (teamSelected == null) {
                //if first time ask for team selection
                try {
                    getTeams();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void getTeams() throws JSONException {

        //https://www.youtube.com/watch?v=nlqtyfshUkc --how to setup custom dialog
        final AlertDialog.Builder alert = new AlertDialog.Builder(MyTeam.this.getContext());
        View mView = getLayoutInflater().inflate(R.layout.alert_select_team, null);
        alert.setTitle("Pick your favorite team");
        final Spinner spinner = (Spinner) mView.findViewById(R.id.spinner);
        String[] spinnerArray;
        //get keys of teams hashMap to set data for adapter
        Set<String> keys = teamInfo.keySet();
        //fill array with 1 extra field
        spinnerArray = keys.toArray(new String[keys.size()]);
        // add a suggestion for spinner
        String options[] = new String[spinnerArray.length + 1];
        options[0] = "Select a team…";
        System.arraycopy(spinnerArray, 0, options, 1, spinnerArray.length);
        //now set adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //https://stackoverflow.com/questions/20597584/how-to-limit-the-height-of-spinner-drop-down-view-in-android
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);
            // Get private mPopup member variable and try cast to ListPopupWindow
            ListPopupWindow popupWindow = (ListPopupWindow) popup.get(spinner);
            // Set popupWindow height to 1000px
            popupWindow.setHeight(1000);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("Ok", null);
        alert.setView(mView);
        final AlertDialog dialog = alert.create();
        dialog.setCancelable(false);
        dialog.show();
        //https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
        //get reference to positive button of dialog to listen for onclick event
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                // If user don't select any team and click ok, do nothing, otherwise prompt user with their selection
                if (!spinner.getSelectedItem().toString().equalsIgnoreCase("Select a team…")) {
                    //store user selection into shared preferences
                    SharedPreferences.Editor editor = mPrefs.edit();

                    String selectedTeam = spinner.getSelectedItem().toString();
                    String teamFixture = teamInfo.get(selectedTeam).get(2) + "?status=FINISHED";
                    editor.putString(teamPref, selectedTeam);
                    editor.putString(teamFixtureUrl, teamFixture);
                    editor.apply();
                    mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    final String teamSelected = mPrefs.getString(teamPref, null);
                    Toast.makeText(getContext(), "You have selected " + spinner.getSelectedItem().toString() + " as your favorite team", Toast.LENGTH_SHORT).show();
                    wantToCloseDialog = true;
                    currentTeamTextView.setText(teamSelected);
                    displayTeamStanding(selectedTeam);
                    String teamIconUrl = teamInfo.get(selectedTeam).get(0);
                    Home.displayTeamIcon(getContext(), teamIconUrl, favTeamIcon);
                    getFixtureResults(teamFixture, teamInfo);
                }
                if (wantToCloseDialog)
                    dialog.dismiss();
            }
        });
    }

    // these are for testing life cycle of this fragment
    @Override
    public void onResume() {
        super.onResume();
        System.out.println("ON RESUME is running");

    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("ON START is running");

    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("ON PAUSE is running");

    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("ON STOP is running");

    }

    public void displayTeamStanding(final String teamName) {
        VolleyUtils.getJsonObject(getContext(), LEAGUESTANDING, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                JSONArray standings = result.getJSONArray("standings")
                        .getJSONObject(0).getJSONArray("table");
                for (int i = 0; i < standings.length(); i++) {
                    JSONObject currentTeam = standings.getJSONObject(i);
                    if (currentTeam.getJSONObject("team").getString("name").equalsIgnoreCase(teamName)) {
                        String pos = currentTeam.getString("position");
                        String points = currentTeam.getString("points");
                        teamStanding.setText(pos + ". " + teamName);
                        teamPoints.setText(points + " pts");
                    }
                }
            }
        });
    }

    public void getFixtureResults(final String teamResultsUrl, final HashMap<String, ArrayList<String>> teamInfo) {
        VolleyUtils.getJsonObject(getContext(), teamResultsUrl, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {
                final ArrayList<ResultsData> matchResults = new ArrayList<>();
                final JSONArray fixtures = result.getJSONArray("matches");
                System.out.println("fixture result fetched, link is " + teamResultsUrl);
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                final SimpleDateFormat date = new SimpleDateFormat("EEE dd MMMM yyyy");
                //only get first 3 results
                for (int i = 0; i < fixtures.length(); i++) {
                    JSONObject currentFixture = fixtures.getJSONObject(i);
                    if (currentFixture.getString("status").equalsIgnoreCase("FINISHED") && matchResults.size() < 3) {
                        try {
                            Date d = sdf.parse(currentFixture.getString("utcDate"));
                            String formattedDate = date.format(d);
                            String homeTeamName = currentFixture.getJSONObject("homeTeam").getString("name");
                            String awayTeamName = currentFixture.getJSONObject("awayTeam").getString("name");
                            JSONObject pastFixture = currentFixture.getJSONObject("score");
                            String scoreHome = pastFixture.getJSONObject("fullTime").getString("homeTeam");
                            String scoreAway = pastFixture.getJSONObject("fullTime").getString("awayTeam");

                            matchResults.add(new ResultsData(teamInfo.get(homeTeamName).get(1),
                                    teamInfo.get(awayTeamName).get(1),
                                    scoreAway, scoreHome,
                                    teamInfo.get(homeTeamName).get(0), teamInfo.get(awayTeamName).get(0), formattedDate));
                        } catch (ParseException | NullPointerException e) {
                            e.printStackTrace();
                            System.out.println("Team data is not available");
                        }
                    } else {
                        resultsBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), Fixtures.class);
                                final String teamName = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(teamPref, "No team selected");
                                intent.putExtra("title", teamName + "'s Matches");
                                intent.putExtra("isResults", true);
                                intent.putExtra("teamInfo", teamInfo);
                                intent.putExtra("allFixtures", fixtures.toString());
                                startActivity(intent);
                            }
                        });
                        displayFixtureResults(matchResults);
                        return;
                    }
                }
            }
        });
    }

    public void displayFixtureResults(ArrayList<ResultsData> matchResults) {

        final ResultsData pastResult = matchResults.get(0);
        final ResultsData pastResult2 = matchResults.get(1);
        final ResultsData pastResult3 = matchResults.get(2);

        resultDate1.setText(pastResult.getDate());
        resultDate2.setText(pastResult2.getDate());
        resultDate3.setText(pastResult3.getDate());

        homeTeam1.setText(pastResult.getHomeTeamName());
        homeTeam2.setText(pastResult2.getHomeTeamName());
        homeTeam3.setText(pastResult3.getHomeTeamName());

        awayTeam1.setText(pastResult.getAwayTeamName());
        awayTeam2.setText(pastResult2.getAwayTeamName());
        awayTeam3.setText(pastResult3.getAwayTeamName());

        result1.setText(pastResult.getHomeGoal() + " - " + pastResult.getAwayGoal());
        result2.setText(pastResult2.getHomeGoal() + " - " + pastResult2.getAwayGoal());
        result3.setText(pastResult3.getHomeGoal() + " - " + pastResult3.getAwayGoal());

        Glide.with(getContext()).load(pastResult.getIconHomeTeam()).into(homeIcon1);

        Glide.with(getContext()).load(pastResult.getIconAwayTeam()).into(awayIcon1);

        Glide.with(getContext()).load(pastResult2.getIconHomeTeam()).into(homeIcon2);

        Glide.with(getContext()).load(pastResult2.getIconAwayTeam()).into(awayIcon2);

        Glide.with(getContext()).load(pastResult3.getIconHomeTeam()).into(homeIcon3);

        Glide.with(getContext()).load(pastResult3.getIconAwayTeam()).into(awayIcon3);

    }
}

