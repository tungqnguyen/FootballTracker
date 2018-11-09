package quangtungnguyen.footballtracker.Data;

public class TeamData {
    private String teamPos;
    private String teamName;
    private String played;
    private String win;
    private String draw;
    private String loss;
    private String goalDiff;
    private String points;
    private String teamUrl;

    public TeamData(String teamPos, String teamName, String played, String win, String draw, String loss, String goalDiff, String points, String teamUrl) {
        this.teamPos = teamPos;
        this.teamName = teamName;
        this.played = played;
        this.win = win;
        this.draw = draw;
        this.loss = loss;
        this.goalDiff = goalDiff;
        this.points = points;
        this.teamUrl = teamUrl;
    }

    public String getTeamUrl() {
        return teamUrl;
    }
    public String getTeamPos() {
        return teamPos;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getPlayed() {
        return played;
    }

    public String getWins() {
        return win;
    }

    public String getDraws() {
        return draw;
    }

    public String getLosses() {
        return loss;
    }

    public String getGoalDiff() {
        return goalDiff;
    }

    public String getPoints() {
        return points;
    }
}
