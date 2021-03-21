package businesslayer;

import dataaccesslayer.DataHandler;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private static final String teamPath = "teamList.csv";
    private static final String userPath = "userList.csv";

    private List<ITeam> teamList;
    private List<User> userList;

    private DataHandler teamDataHandler;
    private DataHandler userDataHandler;

    public TeamManager() {
        teamList = new ArrayList<ITeam>();
        userList = new ArrayList<User>();

        teamDataHandler = new DataHandler(teamPath);
        userDataHandler = new DataHandler(userPath);


        initializeTeamData();
        initializeUserData();
    }

    private void initializeTeamData() {

        List<ArrayList<String>> teamData = teamDataHandler.getData();

        for (ArrayList<String> teamLine : teamData) {
            ITeam temp;
            String teamName = teamLine.get(0);
            String teamId = teamLine.get(1);
            String defaultChannelName = teamLine.get(2);
            String defaultChannelMeetingDate = teamLine.get(3);


            temp = new Team(teamName, teamId, defaultChannelName, defaultChannelMeetingDate);

            this.teamList.add(temp);
        }
    }

    private void initializeUserData() {

        List<ArrayList<String>> userData = userDataHandler.getData();
        List<Integer> userIdList = new ArrayList<Integer>();

        for (ArrayList<String> userLine : userData) {
            User temp;
            String userType = userLine.get(0);
            String userName = userLine.get(1);
            String userId = userLine.get(2);
            String userPassword = userLine.get(4);


            switch (userType) {
                case "Instructor":
                    temp = createInstructor(userIdList, userName, userId, userPassword); break;
                case "Teaching Assistant":
                    temp = createTA(userIdList, userName, userId, userPassword); break;
                case "Student":
                    temp = createStudent(userIdList, userName, userId, userPassword); break;
                default:
                    temp = null; break;
            }

            ArrayList<String> userTeams = new ArrayList<String>(userLine.subList(5, userLine.size()));
            for (String userTeam : userTeams) {
                if (!userTeam.equals("")) {
                    System.out.println("teams" + userTeam);
                    System.out.println();
                    ITeam currentTeam = getTeamById(userTeam);
                    temp.addTeam(currentTeam);
                    currentTeam.addMember(temp);
                }
            }


            this.userList.add(temp);
        }
    }

    private User createInstructor(List<Integer> userIdList, String userName, String userId, String userPassword) {
        User temp;
        if (!userId.equals("")) {
            temp = new Instructor(Integer.parseInt(userId), userName, userPassword, "Computer Engineering");
            userIdList.add(Integer.parseInt(userId));
        } else {
            int id = getUniqueId(userIdList);
            temp = new Instructor(id, userName, "Computer Engineering");
            userIdList.add(id);
        }
        return temp;
    }

    private User createTA(List<Integer> userIdList, String userName, String userId, String userPassword) {
        User temp;
        if (!userId.equals("")) {
            temp = new TeachingAssistant(Integer.parseInt(userId), userName, userPassword, "Computer Engineering");
            userIdList.add(Integer.parseInt(userId));
        } else {
            int id = getUniqueId(userIdList);
            temp = new TeachingAssistant(id, userName, "Computer Engineering");
            userIdList.add(id);
        }
        return temp;
    }

    private User createStudent(List<Integer> userIdList, String userName, String userId, String userPassword) {
        User temp;
        if (!userId.equals("")) {
            temp = new Student(Integer.parseInt(userId), userName, userPassword, "Computer Engineering");
            userIdList.add(Integer.parseInt(userId));
        } else {
            int id = getUniqueId(userIdList);
            temp = new Student(id, userName, "Computer Engineering");
            userIdList.add(id);
        }
        return temp;
    }

    private int getUniqueId(List<Integer> userIdList) {
        boolean isUnique = false;
        int randomId = -1;
        while (!isUnique) {
            randomId = (int) (Math.random() * 1000 + 1);
            if (!userIdList.contains(randomId))
                isUnique = true;
        }
        return randomId;
    }


    public List<ITeam> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<ITeam> teamList) {
        this.teamList = teamList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    private ITeam getTeamById(String id) {
        for (ITeam t : teamList) {
            if (t.getId().equals(id))
                return t;
        }
        return null;
    }


}
