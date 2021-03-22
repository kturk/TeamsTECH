package businesslayer;

import businesslayer.exceptions.UnauthorizedUserOperationException;
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
            if (user.getClass().getSuperclass().getName().equals("businesslayer.Academician")){ // TODO fix
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
        User loggedUser = credentialsCheck();
        mainLoop(loggedUser);
    }

    private void mainLoop(User loggedUser) {
        while(true){
            teamManagerView.promptMainChoices();
            int userChoice = getUserChoice();
            performUserChoice(loggedUser, userChoice);
        }
    }


    private User credentialsCheck(){
        teamManagerView.getEmail();
        String email = teamManagerView.getUserInput();
        teamManagerView.getPassword();
        String password = teamManagerView.getUserInput();
        User loggedUser = getUserByCredentials(email, password);

        if (loggedUser != null) {
            teamManagerView.correctCredentials(loggedUser.getName());
        }
        else{
            teamManagerView.wrongCredentials();
            credentialsCheck(); // TODO recursion to while
        }
        return loggedUser;
    }

    private User getUserByCredentials(String email, String password) {
        for(User user: userList){
            if(user.getEmail().equals(email) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }

    private int getUserChoice() {
        String strInput = teamManagerView.getUserInput();
        while (true) {
            if (isNumeric(strInput)) {
                int intInput = Integer.parseInt(strInput);
                if (intInput >= 0 && intInput <= 3) {
                    return intInput;
                }
            }
            teamManagerView.wrongInput();
            strInput = teamManagerView.getUserInput();
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private void performUserChoice(User loggedUser, int userChoice){
        switch (userChoice){
            case 1:
                try {
                    addTeam(loggedUser);
                    break;
                }
                catch (UnauthorizedUserOperationException ex){
                    System.err.println(ex);
                }
            case 2:
                try {
                    removeTeam(loggedUser);
                    break;
                }
                catch (UnauthorizedUserOperationException ex){
                    System.err.println(ex);
                }
            case 3:
                updateTeam(loggedUser); break;
            case 0:
                exitApplication(); break;
        }
    }

    private void addTeam(User loggedUser) throws UnauthorizedUserOperationException {
        if (loggedUser.getClassType().equals("Academician")){
            teamManagerView.getTeamName();
            String teamName = teamManagerView.getUserInput();
            teamManagerView.getTeamId();
            String teamId = teamManagerView.getUserInput();
            teamManagerView.getDefaultChannelName();
            String defaultChannelName = teamManagerView.getUserInput();
            teamManagerView.getDefaultMeetingDayTime(); // TODO Check valid week day
            String meetingDayTime = teamManagerView.getUserInput();

            ITeam newTeam = new Team(teamName, teamId, defaultChannelName, meetingDayTime);
            newTeam.addTeamOwner((Academician) loggedUser);
            teamList.add(newTeam);
            writeTeamsToCSV();

            // TODO max elems in csv to see all in data mode
        }
        else {
            throw new UnauthorizedUserOperationException("Only instructors can create new teams.");
        }

    }

    private void removeTeam(User loggedUser) throws UnauthorizedUserOperationException{
        showTeamList();
        teamManagerView.getTeamIdToRemove();
        String teamId = teamManagerView.getUserInput();
        ITeam teamToRemove = getTeamById(teamId);
        if (teamToRemove.getTeamOwners().contains(loggedUser)) {
            removeTeamFromUsers(teamToRemove);
            this.teamList.remove(teamToRemove);
            writeTeamsToCSV();
            writeUsersToCSV();
        }
        else {
            throw new UnauthorizedUserOperationException("Only team owners can remove a team.");
        }
    }

    private void showTeamList(){ // TODO should this be in view?
        System.out.println("ID - TEAM NAME");
        for (ITeam team : teamList){
            System.out.println(team.getId() + " - " + team.getName());
        }
    }

    private void removeTeamFromUsers(ITeam teamToRemove){
        for (User user : userList){
            List<ITeam> userTeams = user.getTeams();
            if (userTeams.contains(teamToRemove))
                userTeams.remove(teamToRemove); // TODO warn user about wrong input (else)
        }
    }

    private void updateTeam(User loggedUser) {
        ITeam selectedTeam;

        teamManagerView.getTeamIdToUpdate();
        showTeamList();
        while(true){
            String teamId = teamManagerView.getUserInput(); // TODO check if exist
            selectedTeam = getTeamById(teamId);
            if (selectedTeam != null)
                break;
            teamManagerView.wrongInput(); // TODO exit without getting id
        }

        teamManagerView.promptUpdateTeamChoices();
        int userUpdateChoice = getUserUpdateChoice();
        performUserUpdateChoice(loggedUser, userUpdateChoice, selectedTeam);
    }

    private int getUserUpdateChoice() {
        String strInput = teamManagerView.getUserInput();
        while (true) {
            if (isNumeric(strInput)) {
                int intInput = Integer.parseInt(strInput);
                if (intInput >= 0 && intInput <= 8) {
                    return intInput;
                }
            }
        }
    }

    private void performUserUpdateChoice(User loggedUser, int userUpdateChoice, ITeam selectedTeam){
        switch (userUpdateChoice){
            case 1:
                addMeetingChannel(loggedUser, selectedTeam); break;
            case 2:
                removeMeetingChannel(loggedUser, selectedTeam); break;
            case 3:
                updateMeetingChannel(loggedUser, selectedTeam); break;
            case 4:
                addMember(); break;
            case 5:
                removeMember(); break;
            case 6:
                addTeamOwner(); break;
            case 7:
                showMeetingChannelDetails(); break;
            case 8:
                showDistinctNumbers(); break;
            case 0:
                mainLoop(loggedUser); break;

        }
    }

    private void addMeetingChannel(User loggedUser, ITeam selectedTeam){
        teamManagerView.getChannelName();
        String channelName = teamManagerView.getUserInput();
        teamManagerView.getChannelMeetingDayTime();
        String channelMeetingDayTime = teamManagerView.getUserInput();

        MeetingChannel meetingChannel = new MeetingChannel(channelName, true, channelMeetingDayTime);
        meetingChannel.addParticipant(loggedUser);
        selectedTeam.addMeetingChannel(meetingChannel);
    }

    private void removeMeetingChannel(User loggedUser, ITeam selectedTeam){
        List<MeetingChannel> userChannels = getUserChannels(loggedUser, selectedTeam);
        for (MeetingChannel channel : userChannels){
            System.out.println(channel.getChannelName());
        }
        teamManagerView.getChannelNameToRemove();
        String channelName = teamManagerView.getUserInput(); // TODO check if exist
        MeetingChannel meetingChannel = getMeetingChannelByName(channelName, userChannels);
        selectedTeam.removeMeetingChannel(meetingChannel);
        writeTeamsToCSV();
    }

    private List<MeetingChannel> getUserChannels(User loggedUser, ITeam selectedTeam) {
        List<MeetingChannel> channels = selectedTeam.getMeetingChannels();
        List<MeetingChannel> userChannels = new ArrayList<MeetingChannel>();
        for (MeetingChannel channel : channels){
            List<User> participants = channel.getParticipants();
            for (User participant : participants){
                if (loggedUser.equals(participant)){
                    userChannels.add(channel);
                }
            }
        }
        return userChannels;
    }

    private MeetingChannel getMeetingChannelByName(String channelName, List<MeetingChannel> userChannels){
        for (MeetingChannel channel : userChannels){
            if (channel.getChannelName().equals(channelName))
                return channel;
        }
        return null;
    }

    private void updateMeetingChannel(User loggedUser, ITeam selectedTeam){
        List<MeetingChannel> userChannels = getUserChannels(loggedUser, selectedTeam);
        for (MeetingChannel channel : userChannels){
            System.out.println(channel.getChannelName());
        }
        teamManagerView.getChannelNameToUpdate();
        String channelName = teamManagerView.getUserInput(); // TODO check if exist
        MeetingChannel meetingChannel = getMeetingChannelByName(channelName, userChannels);
        teamManagerView.promptUpdateMeetingChannelChoices();
        int userUpdateMeetingChannelChoice = getUserUpdateMeetingChannelChoice();
        performUserUpdateMeetingChoice(userUpdateMeetingChannelChoice, meetingChannel, loggedUser, selectedTeam);
    }

    private int getUserUpdateMeetingChannelChoice() {
        String strInput = teamManagerView.getUserInput();
        while (true) {
            if (isNumeric(strInput)) {
                int intInput = Integer.parseInt(strInput);
                if (intInput >= 0 && intInput <= 3) {
                    return intInput;
                }
            }
        }
    }

    private void performUserUpdateMeetingChoice(int userChoice, MeetingChannel meetingChannel,
                                                User loggedUser, ITeam selectedTeam){
        switch (userChoice){
            case 1:
                addParticipant(meetingChannel, loggedUser, selectedTeam); break;
            case 2:
                removeParticipant(meetingChannel); break;
            case 3:
                updateMeetingDayTime(meetingChannel); break;
        }
    }

    private void addParticipant(MeetingChannel meetingChannel, User loggedUser, ITeam selectedTeam){
        List<User> teamMembers = selectedTeam.getMembers();
        teamMembers.remove(loggedUser);
        showMembers(teamMembers);
        teamManagerView.getUserIdToAdd();
        String userIds = teamManagerView.getUserInput();
        String[] userIdArray = userIds.split(",");
        addUsersToChannel(meetingChannel, userIdArray);
    }

    private void showMembers(List<User> teamMembers){
        for (User user : teamMembers){
            System.out.println(user.getId() + " - " + user.getName());
        }
    }

    private void addUsersToChannel(MeetingChannel meetingChannel, String[] userIdArray){
        for (String id : userIdArray){ // TODO check id exists
            meetingChannel.addParticipant(getUserById(Integer.parseInt(id)));
        }
    }

    private void removeParticipant(MeetingChannel meetingChannel){
        List<User> channelParticipants = meetingChannel.getParticipants();
        showMembers(channelParticipants);
        teamManagerView.getUserIdToRemove();
        String userIds = teamManagerView.getUserInput();
        String[] userIdArray = userIds.split(",");
        removeUsersToChannel(meetingChannel, userIdArray);
        writeTeamsToCSV();
    }

    private void removeUsersToChannel(MeetingChannel meetingChannel, String[] userIdArray){
        for (String id : userIdArray){ // TODO check id exists
            meetingChannel.removeParticipant(getUserById(Integer.parseInt(id)));
        }
    }

    private void updateMeetingDayTime(MeetingChannel meetingChannel){
        System.out.println();
    }

    private void addMember(){
        System.out.println();
    }

    private void removeMember(){
        System.out.println();
    }

    private void addTeamOwner(){
        System.out.println();
    }

    private void showMeetingChannelDetails(){
        System.out.println();
    }

    private void showDistinctNumbers(){
        System.out.println();
    }

    private void exitApplication(){
        teamManagerView.exitMessage();
        System.exit(0);
    }
}
