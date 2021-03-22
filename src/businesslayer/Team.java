package businesslayer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Team implements ITeam{

    private String name;
    private String id;

    public Team() {}

    public Team(String name, String id) {
        this.id = id;
        this.name = name;
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
    public void addTeamOwner(IUser user) {
        this.getTeamOwners().add(user);
    }


    @Override
    public String getMeetingTimeOfMeetingChannel(MeetingChannel meetingChannel) {
        return meetingChannel.getMeeting().getDate().toString() + meetingChannel.getMeeting().getTime().toString();
    }

    @Override
    public List<IUser> getParticipantsOfMeetingChannel(MeetingChannel meetingChannel) {
        return meetingChannel.getParticipants();
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
        String name = this.name;
        String id = this.id;

        return name + "," + id;
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
