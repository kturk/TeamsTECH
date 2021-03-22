package businesslayer;

import java.util.List;

public interface IUser {
    int getId();

    void setId(int id);

    String getName();

    void setName(String name);

    String getEmail();

    void setEmail(String email);

    String getPassword();

    void setPassword(String password);

    String getDepartment();

    void setDepartment(String department);

    TeamManager getManager();

    List<ITeam> getTeams();

    List<MeetingChannel> getChannels();

    void initializeEmail(String domain);

    void initializePassword();

    String getClassType();

    String getClassName();

    String toCSV();
}
