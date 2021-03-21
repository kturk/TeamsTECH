package BusinessLayer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Team implements ITeam{

    private String name;
    private String id;
    private MeetingChannel defaultChannel;
    private List<MeetingChannel> meetingChannels;
    private List<Academician> owners;
    private List<User> members;

    public Team() {

    }

    public Team(String name, String id, String defaultChannelName, String defaultChannelMeetingDate) {
        this.id = id;
        this.name = name;
        this.defaultChannel = new MeetingChannel(defaultChannelName, false, defaultChannelMeetingDate);
        this.meetingChannels = new ArrayList<MeetingChannel>();
        this.owners = new ArrayList<Academician>();
        this.members = new ArrayList<User>();

    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<MeetingChannel> getMeetingChannels() {
        return meetingChannels;
    }

    public void setMeetingChannels(List<MeetingChannel> meetingChannels) {
        this.meetingChannels = meetingChannels;
    }

    @Override
    public List<Academician> getTeamOwners() {
        return owners;
    }

    public void setOwners(List<Academician> owners) {
        this.owners = owners;
    }

    @Override
    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    @Override
    public void addMeetingChannel(MeetingChannel meetingChannel) {
        this.meetingChannels.add(meetingChannel);
    }

    @Override
    public void removeMeetingChannel(MeetingChannel meetingChannel) {
        if(this.meetingChannels.size() > 1){
            this.meetingChannels.remove(meetingChannel);
        }
    }

    @Override
    public void addParticipantToMeetingChannel(User user, MeetingChannel meetingChannel) {
        meetingChannel.getUserList().add(user);
    }

    @Override
    public void removeParticipantToMeetingChannel(User user, MeetingChannel meetingChannel) {
        meetingChannel.getUserList().remove(user);
    }

    @Override
    public void updateMeetingDayOfMeetingChannel(String localDate, MeetingChannel meetingChannel) {
        meetingChannel.getMeeting().setDate(localDate);
    }

    @Override
    public void updateMeetingTimeOfMeetingChannel(String localTime, MeetingChannel meetingChannel) {
        meetingChannel.getMeeting().setTime(localTime);
    }

    @Override
    public void addMember(User user) {
        this.members.add(user);
    }

    @Override
    public void removeMember(User user) {
        if(this.members.size() > 0){
            this.members.remove(user);
        }
    }

//    @Override
//    public List<User> getTeamOwners() {
//
//        return null;
//    }

    @Override
    public void addTeamOwner(Academician academician) {
        this.getTeamOwners().add(academician);
    }


    @Override
    public String getMeetingTimeOfMeetingChannel(MeetingChannel meetingChannel) {
        String meetingTime = meetingChannel.getMeeting().getDate().toString() + meetingChannel.getMeeting().getTime().toString();
        return meetingTime;
    }

    @Override
    public List<User> getParticipantsOfMeetingChannel(MeetingChannel meetingChannel) {
        return meetingChannel.getUserList();
    }

    //TODO
    @Override
    public Hashtable<String, Integer> getDistinctNumbers() {
        return null;
    }

//    @Override
//    public String toString() {
//        return "Team{" +
//                "name='" + name + '\'' +
//                ", id='" + id + '\'' +
//                ", defaultChannel=" + defaultChannel +
//                ", meetingChannels=" + meetingChannels +
//                ", owners=" + owners +
//                ", members=" + members +
//                '}';
//    }
}
