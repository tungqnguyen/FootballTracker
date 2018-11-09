package quangtungnguyen.footballtracker.Data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class MatchLocationData {

    private LatLng mLatLng;
    private String homeTeamNameInfo;
    private String awayTeamNameInfo;
    private String stadiumLocationInfo;
    private String timeInfo;
    private String homeTeamUrl;
    private String awayTeamUrl;
    private String dateInfo;


    public MatchLocationData(LatLng mLatlng, String homeTeamNameInfo, String awayTeamnameInfo, String stadiumLocationInfo,
                             String timeInfo, String dateInfo, String homeTeamUrl, String awayTeamUrl){
        this.mLatLng = mLatlng;
        this.homeTeamNameInfo = homeTeamNameInfo;
        this.awayTeamNameInfo = awayTeamnameInfo;
        this.stadiumLocationInfo = stadiumLocationInfo;
        this.timeInfo = timeInfo;
        this.homeTeamUrl = homeTeamUrl;
        this.awayTeamUrl = awayTeamUrl;
        this.dateInfo = dateInfo;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }
    public String getHomeTeamNameInfo() {
        return homeTeamNameInfo;
    }

    public String getAwayTeamNameInfo() {
        return awayTeamNameInfo;
    }

    public String getStadiumLocationInfo() {
        return stadiumLocationInfo;
    }

    public String getTimeInfo() {
        return timeInfo;
    }

    public String getHomeTeamUrl() {
        return homeTeamUrl;
    }

    public String getAwayTeamUrl() {
        return awayTeamUrl;
    }
    public String getDateInfo() {
        return dateInfo;
    }

}
