package businesslayer;

import java.util.Hashtable;
import java.util.List;

public interface ITeam {

    public void addMeetingChannel(MeetingChannel meetingChannel);

    public void removeMeetingChannel(MeetingChannel meetingChannel);

    public void addParticipantToMeetingChannel(User user, MeetingChannel meetingChannel);

    public void removeParticipantToMeetingChannel(User user, MeetingChannel meetingChannel);

    public void updateMeetingDayOfMeetingChannel(String localDate, MeetingChannel meetingChannel);

    public void updateMeetingTimeOfMeetingChannel(String localTime, MeetingChannel meetingChannel);

    public void addMember(User user);

    public List<User> getMembers();

    public String getName();

    public void removeMember(User user);

    public List<Academician> getTeamOwners();

    public void addTeamOwner(Academician academician);

    public List<MeetingChannel> getMeetingChannels();

    public String getMeetingTimeOfMeetingChannel(MeetingChannel meetingChannel);

    public List<User> getParticipantsOfMeetingChannel(MeetingChannel meetingChannel);

    public Hashtable<String, Integer> getDistinctNumbers();

    public String getId();

    public String toCSV();
}
