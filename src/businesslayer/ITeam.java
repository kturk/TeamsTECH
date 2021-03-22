package businesslayer;

import java.util.Hashtable;
import java.util.List;

public interface ITeam {

    public void addMeetingChannel(MeetingChannel meetingChannel);

    public void removeMeetingChannel(MeetingChannel meetingChannel);

    public void addParticipantToMeetingChannel(IUser user, MeetingChannel meetingChannel);

    public void removeParticipantToMeetingChannel(IUser user, MeetingChannel meetingChannel);

    public void updateMeetingDayOfMeetingChannel(String localDate, MeetingChannel meetingChannel);

    public void updateMeetingTimeOfMeetingChannel(String localTime, MeetingChannel meetingChannel);

    public void addMember(IUser user);

    public List<IUser> getMembers();

    public String getName();

    public void removeMember(IUser user);

    public List<IUser> getTeamOwners();

    public void addTeamOwner(IUser user);

    public List<MeetingChannel> getMeetingChannels();

    public void remove();

    public String getMeetingTimeOfMeetingChannel(MeetingChannel meetingChannel);

    public List<IUser> getParticipantsOfMeetingChannel(MeetingChannel meetingChannel);

    public Hashtable<String, Integer> getDistinctNumbers();

    public MeetingChannel getDefaultChannel();

    public String getId();

    public String toCSV();
}
