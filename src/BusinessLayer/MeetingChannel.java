package BusinessLayer;

import java.util.List;

public class MeetingChannel {

    private static int id;
    private String channelName;
    private boolean isPrivate;
    private Meeting meeting;
    private List<User> userList;

    public MeetingChannel() {
        this.id++;
    }

    public MeetingChannel(String channelName, boolean isPrivate) {
        this.id++;
        this.channelName = channelName;
        this.isPrivate = isPrivate;
        this.meeting = null;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        MeetingChannel.id = id;
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

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
}
