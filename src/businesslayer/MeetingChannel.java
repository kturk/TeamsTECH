package businesslayer;

public class MeetingChannel {

    private static int count = 0;
    private int id;
    private String channelName;
    private boolean isPrivate; // CSV does not have this column. Assumed as "true" except default channel.
    private Meeting meeting;
    private TeamManager manager;

    public MeetingChannel(TeamManager manager) {
        this.setId(++count);
        this.manager = manager;
    }

    public MeetingChannel(String channelName, boolean isPrivate, String defaultChannelMeetingDate, TeamManager manager) {
        this.setId(++count);
        this.channelName = channelName;
        this.isPrivate = isPrivate;
        this.meeting = new Meeting(defaultChannelMeetingDate);
        this.manager = manager;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    @Override
    public String toString() {
        return "MeetingChannel{" +
                "channelName='" + channelName + '\'' +
                ", isPrivate=" + isPrivate +
                ", meeting=" + meeting +
                '}';
    }
}
