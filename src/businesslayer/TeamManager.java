package businesslayer;

import businesslayer.exceptions.UnauthorizedUserOperationException;
import dataaccesslayer.DataHandler;
import presentationlayer.TeamManagerView;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class TeamManager {

    // TODO input validation check (empty day)
    // TODO writeFile comma deletion when empty
    // TODO success and failure messages
    // TODO UML
    // TODO backing from any screen
    // TODO just to see own teams
    // TODO general refactor

    private static final String teamPath = "teamList.csv";
    private static final String userPath = "userList.csv";

    private List<ITeam> teamList;
    private List<IUser> userList;

    private DataHandler teamDataHandler;
    private DataHandler userDataHandler;

    private TeamManagerView teamManagerView;

    public TeamManager() {
        teamList = new ArrayList<ITeam>();
        userList = new ArrayList<IUser>();

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
                List<IUser> participants = getUserListByIds(channel.get(2));
                temp.setParticipants(participants);
                currentTeam.addMeetingChannel(temp);
            }
        }
    }

    private void initializeUserData() {

        List<ArrayList<String>> userData = userDataHandler.getData();
        List<Integer> userIdList = new ArrayList<Integer>();

        for (ArrayList<String> userLine : userData) {
            IUser temp;
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
        for (IUser user : userList){
            if (user.getClass().getSuperclass().getName().equals("businesslayer.Academician")){ // TODO fix
                List<ITeam> userTeams = user.getTeams();
                for (ITeam team : userTeams){
                    team.addTeamOwner((Academician) user);
                }
            }
        }
    }

    private IUser createInstructor(List<Integer> userIdList, String userName, String userId, String userPassword) {
        IUser temp;
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

    private IUser createTA(List<Integer> userIdList, String userName, String userId, String userPassword) {
        IUser temp;
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

    private IUser createStudent(List<Integer> userIdList, String userName, String userId, String userPassword) {
        IUser temp;
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

    public List<IUser> getUserList() {
        return userList;
    }

    public void setUserList(List<IUser> userList) {
        this.userList = userList;
    }

    private ITeam getTeamById(String id) {
        for (ITeam team : teamList) {
            if (team.getId().equals(id))
                return team;
        }
        return null;
    }

    private List<IUser> getUserListByIds(String ids){

        List<IUser> userList = new ArrayList<IUser>();
        if (ids == null) return null;
        String[] userIds = ids.split(",",-1);
        for(String id : userIds){
            int intId = Integer.parseInt(id.trim());
            IUser currentIUser = getUserById(intId);
            userList.add(currentIUser);
        }

        return userList;
    }

    private IUser getUserById(int id) {
        for(IUser user : userList){
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
        for (IUser user : userList){
            users.add(user.toCSV());
        }
        userDataHandler.writeData(users);

    }

    // View Connection

    public void start(){
        teamManagerView.printWelcome();
        IUser loggedUser = credentialsCheck();
        mainLoop(loggedUser);
    }

    private void mainLoop(IUser loggedUser) {
        while(true){
            teamManagerView.promptMainChoices();
            int userChoice = getUserChoice();
            performUserChoice(loggedUser, userChoice);
        }
    }


    private IUser credentialsCheck(){
        teamManagerView.getEmail();
        String email = teamManagerView.getUserInput();
        teamManagerView.getPassword();
        String password = teamManagerView.getUserInput();
        IUser loggedUser = getUserByCredentials(email, password);

        if (loggedUser != null) {
            teamManagerView.correctCredentials(loggedUser.getName());
        }
        else{
            teamManagerView.wrongCredentials();
            credentialsCheck(); // TODO recursion to while
        }
        return loggedUser;
    }

    private IUser getUserByCredentials(String email, String password) {
        for(IUser user: userList){
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

    private void performUserChoice(IUser loggedUser, int userChoice){
        switch (userChoice){
            case 1:
                try {
                    addTeam(loggedUser);
                    break;
                }
                catch (UnauthorizedUserOperationException ex){
                    System.err.println(ex);
                    break;
                }
            case 2:
                try {
                    removeTeam(loggedUser);
                    break;
                }
                catch (UnauthorizedUserOperationException ex){
                    System.err.println(ex);
                    break;
                }
            case 3:
                updateTeam(loggedUser); break;
            case 0:
                exitApplication(); break;
        }
    }

    private void addTeam(IUser loggedIUser) throws UnauthorizedUserOperationException {
        if (loggedIUser.getClassType().equals("Academician")){
            teamManagerView.getTeamName();
            String teamName = teamManagerView.getUserInput();
            teamManagerView.getTeamId();
            String teamId = teamManagerView.getUserInput();
            teamManagerView.getDefaultChannelName();
            String defaultChannelName = teamManagerView.getUserInput();
            teamManagerView.getDefaultMeetingDayTime(); // TODO Check valid week day
            String meetingDayTime = teamManagerView.getUserInput();

            ITeam newTeam = new Team(teamName, teamId, defaultChannelName, meetingDayTime);
            newTeam.addTeamOwner((Academician) loggedIUser);
            teamList.add(newTeam);
            writeTeamsToCSV();

            // TODO max elems in csv to see all in data mode
        }
        else {
            throw new UnauthorizedUserOperationException("Only instructors can create new teams.");
        }

    }

    private void removeTeam(IUser loggedIUser) throws UnauthorizedUserOperationException{
        showTeamList();
        teamManagerView.getTeamIdToRemove();
        String teamId = teamManagerView.getUserInput();
        ITeam teamToRemove = getTeamById(teamId);
        if (teamToRemove.getTeamOwners().contains(loggedIUser)) {
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
        for (IUser user : userList){
            List<ITeam> userTeams = user.getTeams();
            if (userTeams.contains(teamToRemove))
                userTeams.remove(teamToRemove); // TODO warn user about wrong input (else)
        }
    }

    private void updateTeam(IUser loggedUser) {
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

    private void performUserUpdateChoice(IUser loggedUser, int userUpdateChoice, ITeam selectedTeam){
        switch (userUpdateChoice){
            case 1:
                addMeetingChannel(loggedUser, selectedTeam); break;
            case 2:
                removeMeetingChannel(loggedUser, selectedTeam); break;
            case 3:
                updateMeetingChannel(loggedUser, selectedTeam); break;
            case 4:
                try {
                    addMember(loggedUser, selectedTeam);
                    break;
                }
                catch (UnauthorizedUserOperationException ex){
                    System.err.println(ex);
                    break;
                }
            case 5:
                try{
                    removeMember(loggedUser, selectedTeam); break;
                }
                catch (UnauthorizedUserOperationException ex){
                    System.err.println(ex);
                    break;
                }
            case 6:
                System.out.println("6");
                try {
                    addTeamOwner(loggedUser, selectedTeam); break;
                }
                catch(UnauthorizedUserOperationException ex){
                    System.err.println(ex);
                    break;
                }
            case 7:
                System.out.println("7");
                showMeetingChannelDetails(selectedTeam); break;
            case 8:
                showDistinctNumbers(selectedTeam); break;
            case 0:
                mainLoop(loggedUser); break;

        }
    }

    private void addMeetingChannel(IUser loggedIUser, ITeam selectedTeam){
        teamManagerView.getChannelName();
        String channelName = teamManagerView.getUserInput();
        teamManagerView.getChannelMeetingDayTime();
        String channelMeetingDayTime = teamManagerView.getUserInput();

        MeetingChannel meetingChannel = new MeetingChannel(channelName, true, channelMeetingDayTime);
        meetingChannel.addParticipant(loggedIUser);
        selectedTeam.addMeetingChannel(meetingChannel);
        writeTeamsToCSV();
    }

    private void removeMeetingChannel(IUser loggedUser, ITeam selectedTeam){
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

    private List<MeetingChannel> getUserChannels(IUser loggedUser, ITeam selectedTeam) {
        List<MeetingChannel> channels = selectedTeam.getMeetingChannels();
        List<MeetingChannel> userChannels = new ArrayList<MeetingChannel>();
        for (MeetingChannel channel : channels){
            List<IUser> participants = channel.getParticipants();
            for (IUser participant : participants){
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

    private void updateMeetingChannel(IUser loggedUser, ITeam selectedTeam){
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
                                                IUser loggedUser, ITeam selectedTeam){
        switch (userChoice){
            case 1:
                addParticipant(meetingChannel, loggedUser, selectedTeam); break;
            case 2:
                removeParticipant(meetingChannel); break;
            case 3:
                updateMeetingDayTime(meetingChannel); break;
        }
    }

    private void addParticipant(MeetingChannel meetingChannel, IUser loggedUser, ITeam selectedTeam){
        List<IUser> teamMembers = selectedTeam.getMembers();
        teamMembers.remove(loggedUser);
        showMembers(teamMembers);
        teamManagerView.getUserIdToAdd();
        String userIds = teamManagerView.getUserInput();
        String[] userIdArray = userIds.split(",");
        addUsersToChannel(meetingChannel, userIdArray);
        writeTeamsToCSV();
    }

    private void showMembers(List<IUser> teamMembers){
        for (IUser user : teamMembers){
            System.out.println(user.getId() + " - " + user.getName());
        }
    }

    private void addUsersToChannel(MeetingChannel meetingChannel, String[] userIdArray){
        for (String id : userIdArray){ // TODO check id exists
            meetingChannel.addParticipant(getUserById(Integer.parseInt(id)));
        }
    }

    private void removeParticipant(MeetingChannel meetingChannel){
        List<IUser> channelParticipants = meetingChannel.getParticipants();
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
        teamManagerView.getChannelMeetingDayTime();
        String dateTime = teamManagerView.getUserInput();
        meetingChannel.getMeeting().setDateAndTime(dateTime);
        writeTeamsToCSV();
    }

    private void addMember(IUser loggedUser, ITeam selectedTeam) throws UnauthorizedUserOperationException{
        if (loggedUser.getClassType().equals("Academician")){
            List<IUser> temp = new ArrayList<IUser>(userList);
            temp.removeAll(selectedTeam.getMembers());
            showMembers(temp);
            teamManagerView.getUserIdToAdd();
            String userIds = teamManagerView.getUserInput();
            String[] userIdArray = userIds.split(",");
            addUsersToTeam(selectedTeam, userIdArray);
            writeUsersToCSV();
        }
        else{
            throw new UnauthorizedUserOperationException("Only academicians can add new members.");
        }
    }

    private void addUsersToTeam(ITeam selectedTeam, String[] userIdArray){
        for (String id : userIdArray){ // TODO check id exists
            selectedTeam.addMember(getUserById(Integer.parseInt(id)));
        }
    }

    private void removeMember(IUser loggedUser, ITeam selectedTeam) throws UnauthorizedUserOperationException{
        if (loggedUser.getClassType().equals("Academician")){
            List<IUser> teamMembers = selectedTeam.getMembers();
            showMembers(teamMembers);
            teamManagerView.getUserIdToRemove();
            String userIds = teamManagerView.getUserInput();
            String[] userIdArray = userIds.split(",");
            removeUsersFromTeam(selectedTeam, userIdArray);
            writeUsersToCSV();
        }
        else{
            throw new UnauthorizedUserOperationException("Only academicians can remove members.");
        }
    }

    private void removeUsersFromTeam(ITeam selectedTeam, String[] userIdArray){
        for (String id : userIdArray){ // TODO check id exists
            selectedTeam.removeMember(getUserById(Integer.parseInt(id)));
        }
    }

    private void addTeamOwner(IUser loggedUser, ITeam selectedTeam) throws UnauthorizedUserOperationException{
        List<IUser> currentOwners = selectedTeam.getTeamOwners();
        List<IUser> members = new ArrayList<IUser>(selectedTeam.getMembers());
        members.removeAll(currentOwners);
        if(currentOwners.contains(loggedUser)){
            teamManagerView.getCurrentTeamOwners(selectedTeam.getId());
            showMembers(currentOwners);

            teamManagerView.getMembers(selectedTeam.getId());
            showMembers(members);
            teamManagerView.getUserId();

            IUser newOwner = null;
            boolean isValid = false;
            while (!isValid) {
                String userIdStr = teamManagerView.getUserInput();
                if (isNumeric(userIdStr)){
                    int intInput = Integer.parseInt(userIdStr);

                    IUser tempUser = getUserById(intInput);
                    if(tempUser == null){
                        teamManagerView.wrongInput();
                    }
                    else{
                        if(members.contains(tempUser)){
                            isValid = true;
                            newOwner = tempUser;
                            teamManagerView.teamOwnerSuccess(newOwner.getName());
                        }
                        else{
                            teamManagerView.notAvailableForOwner(tempUser.getName());
                        }
                    }
                }
                else teamManagerView.wrongInput();
            }
            if(newOwner != null) selectedTeam.addTeamOwner(newOwner);
        }
        else{
            throw new UnauthorizedUserOperationException();
        }
    }

    private void showMeetingChannelDetails(ITeam selectedTeam){ //TODO getuserchannels add

        List<MeetingChannel> meetingChannels = selectedTeam.getMeetingChannels();
        meetingChannels.add(0, selectedTeam.getDefaultChannel());
        for(MeetingChannel meetingChannel: meetingChannels){
            System.out.println("Channel Name: " + meetingChannel.getChannelName());
            System.out.println("Meeting Date and Time: " + meetingChannel.getMeeting().getDate() + " " + meetingChannel.getMeeting().getTime());
            System.out.println("Participants:");

            for(IUser user : meetingChannel.getParticipants()){
                System.out.println("\t" + user.getName());
            }
            System.out.println();
        }
    }

    private void showDistinctNumbers(ITeam selectedTeam){
        Hashtable<String, Integer> distinctNumbers = selectedTeam.getDistinctNumbers();

        System.out.println("Number of Students: " + distinctNumbers.get("Student").toString());
        System.out.println("Number of Instructor: " + distinctNumbers.get("Instructor").toString());
        System.out.println("Number of Teaching Assistants: " + distinctNumbers.get("Teaching Assistant").toString());

    }


    private void exitApplication(){
        teamManagerView.exitMessage();
        System.exit(0);
    }
}
