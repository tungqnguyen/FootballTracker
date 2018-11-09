package quangtungnguyen.footballtracker.Data;

public class MatchData {

    private String dateFixture;
    private String homeNameFixture;
    private String homeIconFixture;
    private String awayNameFixture;
    private String awayIconFixture;
    private String timeOrResultFixture;
    private String status;

    public MatchData(String dateFixture, String homeNameFixture, String homeIconFixture, String awayNameFixture, String awayIconFixture, String timeOrResultFixture, String status) {
        this.dateFixture = dateFixture;
        this.homeNameFixture = homeNameFixture;
        this.homeIconFixture = homeIconFixture;
        this.awayNameFixture = awayNameFixture;
        this.awayIconFixture = awayIconFixture;
        this.timeOrResultFixture = timeOrResultFixture;
        this.status = status;
    }

    public String getStatus(){return status;}

    public String getDateFixture() {
        return dateFixture;
    }

    public String getHomeNameFixture() {
        return homeNameFixture;
    }

    public String getHomeIconFixture() {
        return homeIconFixture;
    }

    public String getAwayNameFixture() {
        return awayNameFixture;
    }

    public String getAwayIconFixture() {
        return awayIconFixture;
    }

    public String getTimeOrResultFixture() {
        return timeOrResultFixture;
    }
}
