package quangtungnguyen.footballtracker.Data;

public class ResultsData {
    private String homeTeamName;
    private String awayTeamName;
    private String awayGoal;
    private String homeGoal;
    private String iconHomeTeam;
    private String iconAwayTeam;
    private String date = null;

    public ResultsData(String homeTeamName, String awayTeamName, String awayGoal, String homeGoal, String iconHomeTeam, String iconAwayTeam) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.awayGoal = awayGoal;
        this.homeGoal = homeGoal;
        this.iconHomeTeam = iconHomeTeam;
        this.iconAwayTeam = iconAwayTeam;
    }

    public ResultsData(String homeTeamName, String awayTeamName, String awayGoal, String homeGoal, String iconHomeTeam, String iconAwayTeam, String date) {
        this.homeTeamName = homeTeamName;
        this.awayTeamName = awayTeamName;
        this.awayGoal = awayGoal;
        this.homeGoal = homeGoal;
        this.iconHomeTeam = iconHomeTeam;
        this.iconAwayTeam = iconAwayTeam;
        this.date = date;
    }
    public String getHomeTeamName() {
        return homeTeamName;
    }

    public String getAwayTeamName() {
        return awayTeamName;
    }

    public String getAwayGoal() {
        return awayGoal;
    }

    public String getHomeGoal() {
        return homeGoal;
    }

    public String getIconHomeTeam() {
        return iconHomeTeam;
    }
    public String getIconAwayTeam() {
        return iconAwayTeam;
    }
    public String getDate() {
        return date;
    }
}


