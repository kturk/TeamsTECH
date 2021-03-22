package businesslayer;

import java.util.ArrayList;
import java.util.Hashtable;
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

    public void removeMeetingChannel(MeetingChannel meetingChannel){
        manager.removeChannelFromTeam(this, meetingChannel);
    }

    public List<IUser> getMembers(){
        return manager.getTeamMembers(this);
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
    public MeetingChannel getDefaultChannel(){
        List<MeetingChannel> channels = manager.getTeamChannels(this);
        return channels.get(0);
    }

    public void remove(){
        manager.removeTeam(this);
    }

    public void removeMember(IUser user){
        manager.removeMemberFromTeam(user, this);
    }

    public List<MeetingChannel> getMeetingChannels(){
        return manager.getTeamChannels(this);
    }

    @Override
    public Hashtable<String, Integer> getDistinctNumbers() {
        int studentNumber = 0;
        int instructorNumber = 0;
        int teachingAssistantNumber = 0;

        for(IUser user : this.getMembers()){
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
        StringBuilder builder = new StringBuilder();
        builder.append(this.name).append(",");
        builder.append(this.id).append(",");
        for (MeetingChannel channel : manager.getTeamChannels(this)){
            if (!channel.isPrivate()){
                builder.append(channel.getChannelName()).append(",");
                builder.append(channel.getMeeting().getDateTime());
            }
            else{
                builder.append(",");
                builder.append(channel.getChannelName() == null ? "" : channel.getChannelName()).append(",");
                builder.append(channel.getMeeting().getDateTime() == null ? "" : channel.getMeeting().getDateTime()).append(",");
                List<IUser> channelParticipants = manager.getChannelParticipants(channel);
                if (channelParticipants != null) {
                    List<String> participantIds = new ArrayList<String>();
                    for (IUser user : channelParticipants)
                        participantIds.add(Integer.toString(user.getId()));
                    String participantIdString = String.join(", ", participantIds);
                    char ch = '"';
                    participantIdString = ch + participantIdString + ch;
                    builder.append(participantIdString);
                }
//            else{
//                builder.append(",");
//            }
            }



        }
        return builder.toString();

    }
}
