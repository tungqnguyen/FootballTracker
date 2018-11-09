package quangtungnguyen.footballtracker.Data;

public class VideoData {
    private String videoUrl;
    private String videoTitle;

    public VideoData(String videoId, String videoTitle) {
        this.videoUrl = videoId;
        this.videoTitle = videoTitle;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getVideoTitle() {
        return videoTitle;
    }
}
