package quangtungnguyen.footballtracker.utils;

import org.json.JSONException;

import java.util.HashMap;

public interface TeamNameAndUrlCallback {
    void onReturn(String teamName, String imageUrl) throws JSONException;

}
