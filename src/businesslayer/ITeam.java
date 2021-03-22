package businesslayer;

import java.util.Hashtable;
import java.util.List;

public interface ITeam {

    void addMeetingChannel(MeetingChannel meetingChannel);

    void removeMeetingChannel(MeetingChannel meetingChannel);

    void addMember(IUser user);

    List<IUser> getMembers();

    String getName();

    void removeMember(IUser user);

    List<IUser> getTeamOwners();

    void addTeamOwner(IUser user);

    List<MeetingChannel> getMeetingChannels();

    void remove();

    Hashtable<String, Integer> getDistinctNumbers();

    MeetingChannel getDefaultChannel();

    String getId();

    String toCSV();
}
