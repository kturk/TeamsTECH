package businesslayer;

import java.util.ArrayList;
import java.util.List;

public class Team implements ITeam{

    private String name;
    private String id;
    private TeamManager manager;

    public Team(TeamManager manager) {
        this.manager = manager;
    }

    public Team(String name, String id, TeamManager manager) {
        this.id = id;
        this.name = name;
        this.manager = manager;
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

    @Override
    public List<IUser> getTeamOwners() {
        return manager.getTeamOwners(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void addMeetingChannel(MeetingChannel meetingChannel) {
        manager.addChannelToTeam(meetingChannel, this);
    }

    @Override
    public void addParticipantToMeetingChannel(IUser user, MeetingChannel meetingChannel) {
        meetingChannel.getParticipants().add(user);
    }

    @Override
    public void removeParticipantToMeetingChannel(IUser user, MeetingChannel meetingChannel) {
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
    public void addMember(IUser user) {
        manager.addUserToTeam(user, this);
    }

    @Override
    public void addTeamOwner(IUser user) {
        manager.addOwnerToTeam(user, this);
    }


    @Override
    public String getMeetingTimeOfMeetingChannel(MeetingChannel meetingChannel) {
        return meetingChannel.getMeeting().getDate().toString() + meetingChannel.getMeeting().getTime().toString();
    }

    @Override
    public List<IUser> getParticipantsOfMeetingChannel(MeetingChannel meetingChannel) {
        return meetingChannel.getParticipants();
    }

    public void remove(){
        manager.removeTeam(this);
    }
//    @Override
//    public Hashtable<String, Integer> getDistinctNumbers() {
//        int studentNumber = 0;
//        int instructorNumber = 0;
//        int teachingAssistantNumber = 0;
//
//        for(IUser user : this.members){
//            if(user.getClassName().equals("Instructor"))
//                instructorNumber += 1;
//            else if(user.getClassName().equals("Teaching Assistant"))
//                teachingAssistantNumber += 1;
//            else
//                studentNumber += 1;
//        }
//
//        Hashtable<String, Integer> distinctNumbers = new Hashtable<String, Integer>();
//        distinctNumbers.put("Instructor", instructorNumber);
//        distinctNumbers.put("Teaching Assistant", teachingAssistantNumber);
//        distinctNumbers.put("Student", studentNumber);
//
//        return distinctNumbers;
//    }

    public String toCSV() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(",");
        builder.append(this.id).append(",");

        for (MeetingChannel channel : manager.getTeamChannels(this)){
            builder.append(channel.getChannelName()).append(",");
            builder.append(channel.getMeeting().getDateTime());
            List<IUser> channelParticipants = manager.getChannelParticipants(channel);
            // if (!channelParticipants == null)
            List<String> participantIds = new ArrayList<String>();
            for (IUser user : channelParticipants)
                participantIds.add(Integer.toString(user.getId()));
            String participantIdString = String.join(", ", participantIds);
            char ch='"';
            participantIdString = "," + ch + participantIdString + ch;
            builder.append(participantIdString);

        }
        return builder.toString();

    }

//    public String toCSV() {
//        String name = this.name;
//        String id = this.id;
//        String defaultChannel = this.defaultChannel.getChannelName();
//        String defaultMeetingDay;
//        if (!this.defaultChannel.getMeeting().getDate().equals(""))
//            defaultMeetingDay = this.defaultChannel.getMeeting().getDate() + " " + this.defaultChannel.getMeeting().getTime();
//        else
//            defaultMeetingDay = "";
//        List<String> channelNames = new ArrayList<String>();
//        List<String> meetingDays = new ArrayList<String>();
//        List<String> participants = new ArrayList<String>();
//
//        for(MeetingChannel channel: this.meetingChannels){
//            channelNames.add(channel.getChannelName());
//            if (!channel.getMeeting().getDate().equals(""))
//                meetingDays.add(channel.getMeeting().getDate() + " " + channel.getMeeting().getTime());
//            else
//                meetingDays.add("");
//            char ch='"';
//            List<String> participantIds = new ArrayList<String>();
//            if (channel.getParticipants() != null){
//                for (IUser user : channel.getParticipants()) {
//                    participantIds.add(Integer.toString(user.getId()));
//                }
//                String participantsString = String.join(", ", participantIds);
//                participants.add(ch + participantsString + ch);
//            }
//            else
//                participants.add("");
//
//        }
//
//        String returnedString = name + "," + id + "," + defaultChannel + "," + defaultMeetingDay;
//
//        for(int i=0; i<channelNames.size(); i++) {
//            if(channelNames.get(i) != null) returnedString += "," + channelNames.get(i);
//            else returnedString += ",";
//
//            if(meetingDays.get(i) != null) returnedString += "," + meetingDays.get(i);
//            else returnedString += ",";
//
//            if(participants.get(i) != null) returnedString += "," + participants.get(i);
//            else returnedString += ",";
//
//        }
//
//        return returnedString;
//    }
}
