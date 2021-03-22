package businesslayer;

import java.util.ArrayList;
import java.util.List;

public class MeetingChannel {

    private static int id;
    private String channelName;
    private boolean isPrivate;
    private Meeting meeting;
    private List<User> participants;

    // TODO check this.id or not
    public MeetingChannel() {
        id++;
    }

    public MeetingChannel(String channelName, boolean isPrivate, String defaultChannelMeetingDate) {
        // TODO check this.id or not
        id++;
        this.channelName = channelName;
        this.isPrivate = isPrivate;
        this.meeting = new Meeting(defaultChannelMeetingDate);
        this.participants = new ArrayList<User>();
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

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public void addParticipant(User user) { this.participants.add(user); }

    @Override
    public String toString() {
        return "MeetingChannel{" +
                "channelName='" + channelName + '\'' +
                ", isPrivate=" + isPrivate +
                ", meeting=" + meeting +
                ", userList=" + participants +
                '}';
    }
}
