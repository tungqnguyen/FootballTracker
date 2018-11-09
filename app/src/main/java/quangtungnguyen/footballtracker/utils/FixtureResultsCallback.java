package quangtungnguyen.footballtracker.utils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import quangtungnguyen.footballtracker.Data.MatchData;

public interface FixtureResultsCallback {
    void onFinished(ArrayList<MatchData> fixtures);

}
