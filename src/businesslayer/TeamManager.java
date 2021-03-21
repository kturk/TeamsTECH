package businesslayer;

import dataaccesslayer.DataHandler;
import presentationlayer.TeamManagerView;

import java.util.ArrayList;
import java.util.List;

public class TeamManager {

    private static final String teamPath = "teamList.csv";
    private static final String userPath = "userList.csv";

    private List<ITeam> teamList;
    private List<User> userList;

    private DataHandler teamDataHandler;
    private DataHandler userDataHandler;

    private TeamManagerView teamManagerView;

    public TeamManager() {
        teamList = new ArrayList<ITeam>();
        userList = new ArrayList<User>();

        teamDataHandler = new DataHandler(teamPath);
        userDataHandler = new DataHandler(userPath);


        initializeTeamData();
        initializeUserData();
        initializeOtherChannelsForTeams();
        initializeTeamOwners();
        writeTeamsToCSV();
        writeUsersToCSV();

        teamManagerView = new TeamManagerView();
    }

    private void initializeTeamData() {

        List<ArrayList<String>> teamData = teamDataHandler.getData();
        List<String> a = teamDataHandler.convertListToString(teamData);

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

    private void initializeOtherChannelsForTeams() {

        List<ArrayList<String>> teamData = teamDataHandler.getData();
        List<String> a = teamDataHandler.convertListToString(teamData);

        for (ArrayList<String> teamLine : teamData) {

            String teamId = teamLine.get(1);
            ITeam currentTeam = getTeamById(teamId);
            List<ArrayList<String>> otherChannels = new ArrayList<ArrayList<String>>();

            for(int i=4; i<teamLine.size(); i+=3){
                ArrayList<String> channelDetails = new ArrayList<String>();
                channelDetails.add(teamLine.get(i).equals("") ? null : teamLine.get(i));
                channelDetails.add(teamLine.get(i+1).equals("") ? null : teamLine.get(i+1));
                String participants = teamLine.get(i+2);
                if(participants.equals("")){
                    channelDetails.add(null);
                }
                else{
                    participants = participants.substring(1, participants.length()-1);
                    channelDetails.add(participants);
                }

                otherChannels.add(channelDetails);
            }

            for (ArrayList<String> channel : otherChannels){
                MeetingChannel temp = new MeetingChannel(channel.get(0), true, channel.get(1));
                List<User> participants = getUserListByIds(channel.get(2));
                temp.setParticipants(participants);
                currentTeam.addMeetingChannel(temp);
            }
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
                    ITeam currentTeam = getTeamById(userTeam);
                    temp.addTeam(currentTeam);
                    currentTeam.addMember(temp);
                }
            }


            this.userList.add(temp);
        }
    }

    private void initializeTeamOwners(){
        for (User user : userList){
            if (user.getClass().getSuperclass().getName().equals("businesslayer.Academician")){
                List<ITeam> userTeams = user.getTeams();
                for (ITeam team : userTeams){
                    team.addTeamOwner((Academician) user);
                }
            }
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
        for (ITeam team : teamList) {
            if (team.getId().equals(id))
                return team;
        }
        return null;
    }

    private ArrayList<User> getUserListByIds(String ids){

        ArrayList<User> userList = new ArrayList<User>();
        if (ids == null) return null;
        String[] userIds = ids.split(",",-1);
        for(String id : userIds){
            int intId = Integer.parseInt(id.trim());
            User currentUser = getUserById(intId);
            userList.add(currentUser);
        }

        return userList;
    }

    private User getUserById(int id) {
        for(User user: userList){
            if(user.getId() == id){
                return user;
            }
        }
        return null;
    }

    private void writeTeamsToCSV(){
        List<String> teams = new ArrayList<String>();
        teams.add("Team Name,Team ID,Default Channel,Default Meeting Day and Time,Meeting Channel,Meeting Day and Time,Participant ID");
        for (ITeam team : teamList){
            teams.add(team.toCSV());
        }
        teamDataHandler.writeData(teams);
    }

    private void writeUsersToCSV(){
        List<String> users = new ArrayList<String>();
        users.add("User Type,User Name,User ID,Email,Password,Team ID,");
        for (User user : userList){
            users.add(user.toCSV());
        }
        userDataHandler.writeData(users);

    }

    // View Connection

    public void start(){
        teamManagerView.printWelcome();
        while(true){
            credentialsCheck();
        }
    }

    public void credentialsCheck(){
        teamManagerView.getEmail();
        String email = teamManagerView.getStringInput();
        teamManagerView.getPassword();
        String password = teamManagerView.getStringInput();

        System.out.println("email" + email);
        System.out.println("pass" + password);
    }

}
