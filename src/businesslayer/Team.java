package businesslayer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Team implements ITeam{

    private String name;
    private String id;
    private MeetingChannel defaultChannel;
    private List<MeetingChannel> meetingChannels;
    private List<User> owners;
    private List<User> members;

    public Team() {}

    public Team(String name, String id, String defaultChannelName, String defaultChannelMeetingDate) {
        this.id = id;
        this.name = name;
        this.defaultChannel = new MeetingChannel(defaultChannelName, false, defaultChannelMeetingDate);
        this.meetingChannels = new ArrayList<MeetingChannel>();
        this.owners = new ArrayList<User>();
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
    public List<User> getTeamOwners() {
        return owners;
    }

    public void setOwners(List<User> owners) {
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
        if(this.meetingChannels.size() > 0 && meetingChannel != null){
            System.out.println("sea");
            this.meetingChannels.remove(meetingChannel);
        }
    }

    @Override
    public void addParticipantToMeetingChannel(User user, MeetingChannel meetingChannel) {
        meetingChannel.getParticipants().add(user);
    }

    @Override
    public void removeParticipantToMeetingChannel(User user, MeetingChannel meetingChannel) {
        meetingChannel.getParticipants().remove(user);
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
        this.getDefaultChannel().addParticipant(user);
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
        return meetingChannel.getParticipants();
    }

    @Override
    public MeetingChannel getDefaultChannel() {
        return defaultChannel;
    }

    @Override
    public Hashtable<String, Integer> getDistinctNumbers() {
        int studentNumber = 0;
        int instructorNumber = 0;
        int teachingAssistantNumber = 0;

        for(User user: this.members){
            if(user.getClassName().equals("Instructor"))
                instructorNumber += 1;
            else if(user.getClassName().equals("Teaching Assistant"))
                teachingAssistantNumber += 1;
            else
                studentNumber += 1;
        }

        Hashtable<String, Integer> distinctNumbers = new Hashtable<String, Integer>();
        distinctNumbers.put("Instructor", instructorNumber);
        distinctNumbers.put("Teaching Assistant", teachingAssistantNumber);
        distinctNumbers.put("Student", studentNumber);

        return distinctNumbers;
    }


    public String toCSV() {
        String name = this.name;
        String id = this.id;
        String defaultChannel = this.defaultChannel.getChannelName();
        String defaultMeetingDay;
        if (!this.defaultChannel.getMeeting().getDate().equals(""))
            defaultMeetingDay = this.defaultChannel.getMeeting().getDate() + " " + this.defaultChannel.getMeeting().getTime();
        else
            defaultMeetingDay = "";
        List<String> channelNames = new ArrayList<String>();
        List<String> meetingDays = new ArrayList<String>();
        List<String> participants = new ArrayList<String>();

        for(MeetingChannel channel: this.meetingChannels){
            channelNames.add(channel.getChannelName());
            if (!channel.getMeeting().getDate().equals(""))
                meetingDays.add(channel.getMeeting().getDate() + " " + channel.getMeeting().getTime());
            else
                meetingDays.add("");
            char ch='"';
            List<String> participantIds = new ArrayList<String>();
            if (channel.getParticipants() != null){
                for (User user : channel.getParticipants()) {
                    participantIds.add(Integer.toString(user.getId()));
                }
                String participantsString = String.join(", ", participantIds);
                participants.add(ch + participantsString + ch);
            }
            else
                participants.add("");

        }

        String returnedString = name + "," + id + "," + defaultChannel + "," + defaultMeetingDay;

        for(int i=0; i<channelNames.size(); i++) {
            if(channelNames.get(i) != null) returnedString += "," + channelNames.get(i);
            else returnedString += ",";

            if(meetingDays.get(i) != null) returnedString += "," + meetingDays.get(i);
            else returnedString += ",";

            if(participants.get(i) != null) returnedString += "," + participants.get(i);
            else returnedString += ",";

        }

        return returnedString;
    }
}
