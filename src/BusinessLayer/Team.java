package BusinessLayer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Team implements ITeam{

    private int id;
    private String name;
    private List<MeetingChannel> meetingChannels;
    private List<Academician> owners;
    private List<User> members;

    public Team() {
        MeetingChannel defaultChannel = new MeetingChannel("General", false);
        this.meetingChannels = new ArrayList<MeetingChannel>();
        this.meetingChannels.add(defaultChannel);
    }

    public Team(int id, String name) {
        this.id = id;
        this.name = name;
        MeetingChannel defaultChannel = new MeetingChannel("General", false);
        this.meetingChannels = new ArrayList<MeetingChannel>();
        this.meetingChannels.add(defaultChannel);
        this.owners = new ArrayList<Academician>();
        this.members = new ArrayList<User>();

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    public void updateMeetingDayOfMeetingChannel(LocalDate localDate, MeetingChannel meetingChannel) {
        meetingChannel.getMeeting().setDate(localDate);
    }

    @Override
    public void updateMeetingTimeOfMeetingChannel(LocalTime localTime, MeetingChannel meetingChannel) {
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





}
