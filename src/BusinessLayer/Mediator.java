package BusinessLayer;

import DataAccessLayer.DataHandler;

import java.util.ArrayList;
import java.util.List;

public class Mediator {

    private static final String teamPath = "teamList.csv";
    private static final String userPath = "userList.csv";

    private List<ITeam> teamList;
    private List<User> userList;

    private DataHandler teamDataHandler;
    private DataHandler userDataHandler;

    public Mediator() {
        teamList = new ArrayList<ITeam>();
        userList = new ArrayList<User>();

        teamDataHandler = new DataHandler(teamPath);
        userDataHandler = new DataHandler(userPath);


        initializeTeamData();
        initializeUserData();
    }

    private void initializeTeamData() {

        List<ArrayList<String>> teamData = teamDataHandler.getData();

        for(ArrayList<String> teamLine : teamData){
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

        for(ArrayList<String> userLine : userData){
            User temp;
            String userType = userLine.get(0);
            String userName = userLine.get(1);
            String userId = userLine.get(2);
            String userPassword = userLine.get(4);

            ArrayList<String> userTeams = new ArrayList<String>(userLine.subList(5, userLine.size()));
            if(userType.equals("Instructor"))
                if(!userId.equals(""))
                    temp = new Instructor(Integer.parseInt(userId), userName, userPassword , "Computer Engineering");
                else
                    temp = new Instructor(userName, "Computer Engineering");

            else if(userType.equals("Teaching Assistant"))
                if(!userId.equals(""))
                    temp = new TeachingAssistant(Integer.parseInt(userId), userName, userPassword , "Computer Engineering");
                else
                    temp = new TeachingAssistant(userName, "Computer Engineering");

            else if(userType.equals("Student"))
                if(!userId.equals(""))
                    temp = new Student(Integer.parseInt(userId), userName, userPassword , "Computer Engineering");
                else
                    temp = new Student(userName, "Computer Engineering");
            else
                temp = null;


            for(int i=0; i<userTeams.size(); i++){
                if(!userTeams.get(i).equals("")){
                    System.out.println("teams" + userTeams.get(i));
                    System.out.println();
                    ITeam currentTeam = getTeamById(userTeams.get(i));
                    temp.addTeam(currentTeam);
                    currentTeam.addMember(temp);
                }
            }



            this.userList.add(temp);
        }
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
        for(ITeam t : teamList){
            if(t.getId().equals(id))
                return t;
        }
        return null;
    }



}
