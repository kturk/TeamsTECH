package businesslayer;

import businesslayer.exceptions.UnauthorizedUserOperationException;
import dataaccesslayer.DataHandler;
import presentationlayer.TeamManagerView;

import java.util.*;

public class TeamManager {


    private static final String teamPath = "teamList.csv";
    private static final String userPath = "userList.csv";

    private List<ITeam> teamList;
    private List<IUser> userList;

    private DataHandler teamDataHandler;
    private DataHandler userDataHandler;

    private TeamManagerView teamManagerView;

    private Map<ITeam, List<IUser>> teamUserMap = new HashMap<ITeam, List<IUser>>();
    private Map<ITeam, List<MeetingChannel>> teamChannelMap = new HashMap<ITeam, List<MeetingChannel>>();
    private Map<MeetingChannel, List<IUser>> channelUserMap = new HashMap<MeetingChannel, List<IUser>>();
    private Map<ITeam, List<IUser>> teamOwnerMap = new HashMap<ITeam, List<IUser>>();

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
            ITeam tempTeam;
            String teamName = teamLine.get(0);
            String teamId = teamLine.get(1);
            tempTeam = new Team(teamName, teamId, this);
            this.teamList.add(tempTeam);

            String defaultChannelName = teamLine.get(2);
            String defaultChannelMeetingDateTime = teamLine.get(3);
            MeetingChannel defaultChannel = new MeetingChannel(
                    defaultChannelName,
                    false,
                    defaultChannelMeetingDateTime,
                    this);
            ArrayList<MeetingChannel> channelList = new ArrayList<MeetingChannel>();
            channelList.add(defaultChannel);
            teamChannelMap.put(tempTeam, channelList);
            teamUserMap.put(tempTeam, new ArrayList<IUser>());
            channelUserMap.put(defaultChannel, new ArrayList<IUser>());
            teamOwnerMap.put(tempTeam, new ArrayList<IUser>());

        }
    }

    private void initializeUserData() {

        List<ArrayList<String>> userData = userDataHandler.getData();
        List<Integer> userIdList = new ArrayList<Integer>();

        for (ArrayList<String> userLine : userData) {
            IUser tempUser;
            String userType = userLine.get(0);
            String userName = userLine.get(1);
            String userId = userLine.get(2);
            String userPassword = userLine.get(4);


            switch (userType) {
                case "Instructor":
                    tempUser = createInstructor(userIdList, userName, userId, userPassword); break;
                case "Teaching Assistant":
                    tempUser = createTA(userIdList, userName, userId, userPassword); break;
                case "Student":
                    tempUser = createStudent(userIdList, userName, userId, userPassword); break;
                default:
                    tempUser = null; break;
            }
            this.userList.add(tempUser);

            ArrayList<String> userTeams = new ArrayList<String>(userLine.subList(5, userLine.size()));
            for (String userTeam : userTeams) {
                if (!userTeam.equals("")) {
                    ITeam currentTeam = getTeamById(userTeam);
                    teamUserMap.get(currentTeam).add(tempUser);
                    List<MeetingChannel> channels = teamChannelMap.get(currentTeam);
                    MeetingChannel defaultChannel = getDefaultChannel(channels);
                    channelUserMap.get(defaultChannel).add(tempUser);
                }
            }
        }
    }

    private MeetingChannel getDefaultChannel(List<MeetingChannel> channels){
        for (MeetingChannel channel : channels){
            if (!channel.isPrivate())
                return channel;
        }
        return null;
    }

    private void initializeOtherChannelsForTeams() {

        List<ArrayList<String>> teamData = teamDataHandler.getData();
        for (ArrayList<String> teamLine : teamData) {
            String teamId = teamLine.get(1);
            ITeam team = getTeamById(teamId);
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
                MeetingChannel tempChannel = new MeetingChannel(
                        channelDetails.get(0),
                        true,
                        channelDetails.get(1),
                        this);
                List<IUser> members = getUserListByIds(channelDetails.get(2));
                channelUserMap.put(tempChannel, members);
                teamChannelMap.get(team).add(tempChannel);
            }
        }
    }


    private void initializeTeamOwners(){
        for (Map.Entry<ITeam, List<IUser>> entry : teamUserMap.entrySet()) {
            ITeam team = entry.getKey();
            for (IUser user : entry.getValue()){
                if (user.getClassType().equals("Academician"))
                    teamOwnerMap.get(team).add(user);
            }
        }
    }

    private IUser createInstructor(List<Integer> userIdList, String userName, String userId, String userPassword) {
        IUser temp;
        if (!userId.equals("")) {
            temp = new Instructor(Integer.parseInt(userId), userName, userPassword, "Computer Engineering", this);
            userIdList.add(Integer.parseInt(userId));
        } else {
            int id = getUniqueId(userIdList);
            temp = new Instructor(id, userName, "Computer Engineering", this);
            userIdList.add(id);
        }
        return temp;
    }

    private IUser createTA(List<Integer> userIdList, String userName, String userId, String userPassword) {
        IUser temp;
        if (!userId.equals("")) {
            temp = new TeachingAssistant(Integer.parseInt(userId), userName, userPassword, "Computer Engineering", this);
            userIdList.add(Integer.parseInt(userId));
        } else {
            int id = getUniqueId(userIdList);
            temp = new TeachingAssistant(id, userName, "Computer Engineering", this);
            userIdList.add(id);
        }
        return temp;
    }

    private IUser createStudent(List<Integer> userIdList, String userName, String userId, String userPassword) {
        IUser temp;
        if (!userId.equals("")) {
            temp = new Student(Integer.parseInt(userId), userName, userPassword, "Computer Engineering", this);
            userIdList.add(Integer.parseInt(userId));
        } else {
            int id = getUniqueId(userIdList);
            temp = new Student(id, userName, "Computer Engineering", this);
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

    public List<ITeam> getUserTeams(IUser user){
        List<ITeam> teams = new ArrayList<ITeam>();
        for (Map.Entry<ITeam, List<IUser>> entry : teamUserMap.entrySet()){
            for (IUser currentUser : entry.getValue()){
                if (currentUser.equals(user))
                    teams.add(entry.getKey());
            }
        }
        return teams;
    }

    public List<MeetingChannel> getUserChannels(IUser user){
        List<MeetingChannel> channels = new ArrayList<MeetingChannel>();
        for (Map.Entry<MeetingChannel, List<IUser>> entry : channelUserMap.entrySet()){
            if (entry.getValue() != null) {
                for (IUser currentUser : entry.getValue()) {
                    if (currentUser.equals(user))
                        channels.add(entry.getKey());
                }
            }
        }
        return channels;
    }

    public List<MeetingChannel> getTeamChannels(ITeam team){
        for (Map.Entry<ITeam, List<MeetingChannel>> entry : teamChannelMap.entrySet()){
            if (entry.getKey().equals(team))
                return entry.getValue();
        }
        return null;
    }

    public List<IUser> getChannelParticipants(MeetingChannel channel){
        for (Map.Entry<MeetingChannel, List<IUser>> entry : channelUserMap.entrySet()){
            if (entry.getKey().equals(channel))
                return entry.getValue();
        }
        return null;
    }

    public List<IUser> getTeamOwners(ITeam team){
        for (Map.Entry<ITeam, List<IUser>> entry : teamOwnerMap.entrySet()){
            if (entry.getKey().equals(team))
                return entry.getValue();
        }
        return null;
    }

    public void removeTeam(ITeam team){
        List<MeetingChannel> channels = getTeamChannels(team);
        for (MeetingChannel channel : channels){
            removeChannel(channel);
        }
        teamUserMap.remove(team); teamChannelMap.remove(team); teamOwnerMap.remove(team);
        teamList.remove(team);

    }

    public void removeChannel(MeetingChannel channel) {
        channelUserMap.remove(channel);
    }

    public void removeChannelFromTeam(ITeam team, MeetingChannel channel){
        for (Map.Entry<ITeam, List<MeetingChannel>> entry : teamChannelMap.entrySet()){
            if (entry.getKey().equals(team))
                entry.getValue().remove(channel);
        }
    }

    public List<IUser> getTeamMembers(ITeam team){
        return teamUserMap.get(team);
    }

    public void removeUserFromChannel(IUser user, MeetingChannel channel){
        channelUserMap.get(channel).remove(user);
    }

    public void addUserToTeam(IUser loggedUser, ITeam team){
        teamUserMap.get(team).add(loggedUser);
    }

    public void addOwnerToTeam(IUser loggedUser, ITeam team){
        teamOwnerMap.get(team).add(loggedUser);
    }

    public void addUserToChannel(IUser loggedUser, MeetingChannel channel){
        List<IUser> members = new ArrayList<IUser>();
        members.add(loggedUser);
        channelUserMap.put(channel, members);
    }

    public void addChannelToTeam(MeetingChannel channel, ITeam team){
        teamChannelMap.get(team).add(channel);
    }

    public void removeMemberFromTeam(IUser user, ITeam team){
        teamUserMap.get(team).remove(user);
    }

    // ---------------------------------View Connection---------------------------------//

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
        while (true){
            teamManagerView.getEmail();
            String email = teamManagerView.getUserInput();
            teamManagerView.getPassword();
            String password = teamManagerView.getUserInput();
            IUser loggedUser = getUserByCredentials(email, password);

            if (loggedUser != null) {
                teamManagerView.correctCredentials(loggedUser.getName());
                return loggedUser;
            }
            else {
                teamManagerView.wrongCredentials();
            }
        }
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
            teamManagerView.getTeamName(); String teamName = teamManagerView.getUserInput();
            teamManagerView.getTeamId(); String teamId = teamManagerView.getUserInput();
            teamManagerView.getDefaultChannelName(); String defaultChannelName = teamManagerView.getUserInput();
            teamManagerView.getDefaultMeetingDayTime(); String meetingDayTime = teamManagerView.getUserInput();
            if (meetingDayTime.equals(""))
                meetingDayTime = null;
            ITeam newTeam = new Team(teamName, teamId, this);
            teamList.add(newTeam);
            this.teamUserMap.put(newTeam, new ArrayList<IUser>());
            this.teamChannelMap.put(newTeam, new ArrayList<MeetingChannel>());
            this.teamOwnerMap.put(newTeam, new ArrayList<IUser>());
            newTeam.addMember(loggedIUser);
            newTeam.addTeamOwner(loggedIUser);

            MeetingChannel channel = new MeetingChannel(defaultChannelName, true, meetingDayTime, this);
            this.channelUserMap.put(channel, new ArrayList<IUser>());
            channel.addUser(loggedIUser);

            newTeam.addMeetingChannel(channel);

            writeTeamsToCSV();
        }
        else {
            throw new UnauthorizedUserOperationException("Only instructors can create new teams.");
        }

    }

    private void removeTeam(IUser loggedUser) throws UnauthorizedUserOperationException{
        ITeam teamToRemove;
        do {
            showTeamList(loggedUser);
            teamManagerView.getTeamIdToRemove();
            String teamId = teamManagerView.getUserInput();
            teamToRemove = getTeamById(teamId);
        } while (!loggedUser.getTeams().contains(teamToRemove));

        if (teamToRemove.getTeamOwners().contains(loggedUser)) {
            teamToRemove.remove();
            writeTeamsToCSV();
            writeUsersToCSV();
        }
        else {
            throw new UnauthorizedUserOperationException("Only team owners can remove a team.");
        }
    }


    private void showTeamList(IUser loggedUser){
        System.out.println("ID - TEAM NAME");
        for (ITeam team : loggedUser.getTeams()){
            System.out.println(team.getId() + " - " + team.getName());
        }
    }

    private void removeTeamFromUsers(ITeam teamToRemove){
        for (IUser user : userList){
            List<ITeam> userTeams = user.getTeams();
            if (userTeams.contains(teamToRemove))
                userTeams.remove(teamToRemove);
        }
    }

    private void updateTeam(IUser loggedUser) {
        ITeam selectedTeam;

        teamManagerView.getTeamIdToUpdate();
        showTeamList(loggedUser);
        while(true){
            String teamId = teamManagerView.getUserInput();
            selectedTeam = getTeamById(teamId);
            if (selectedTeam != null)
                break;
            teamManagerView.wrongInput();
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
                try {
                    addTeamOwner(loggedUser, selectedTeam); break;
                }
                catch(UnauthorizedUserOperationException ex){
                    System.err.println(ex);
                    break;
                }
            case 7:
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
        if (channelMeetingDayTime.equals(""))
            channelMeetingDayTime = null;

        MeetingChannel meetingChannel = new MeetingChannel(channelName, true, channelMeetingDayTime, this);
        selectedTeam.addMeetingChannel(meetingChannel);
        meetingChannel.addUser(loggedIUser);
        writeTeamsToCSV();
    }

    private void removeMeetingChannel(IUser loggedUser, ITeam selectedTeam){
        List<MeetingChannel> userChannels = getUserChannels(loggedUser, selectedTeam);
        for (MeetingChannel channel : userChannels){
            if (!channel.isPrivate())
                continue;
            System.out.println(channel.getChannelName());
        }
        teamManagerView.getChannelNameToRemove();
        String channelName = teamManagerView.getUserInput();
        MeetingChannel meetingChannel = getMeetingChannelByName(channelName, userChannels);
        selectedTeam.removeMeetingChannel(meetingChannel);
        meetingChannel.remove();
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
        String channelName = teamManagerView.getUserInput();
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
                if (!meetingChannel.isPrivate())
                    teamManagerView.defaultChannelHasAll();
                else
                    addParticipant(meetingChannel, selectedTeam); break;
            case 2:
                removeParticipant(meetingChannel); break;
            case 3:
                updateMeetingDayTime(meetingChannel); break;
        }
    }

    private void addParticipant(MeetingChannel meetingChannel, ITeam selectedTeam){
        List<IUser> teamMembers = selectedTeam.getMembers();
        teamMembers.removeAll(meetingChannel.getParticipants());
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
        for (String id : userIdArray){
            meetingChannel.addUser(getUserById(Integer.parseInt(id)));
        }
    }

    private void removeParticipant(MeetingChannel meetingChannel){
        List<IUser> channelParticipants = meetingChannel.getParticipants();
        showMembers(channelParticipants);
        teamManagerView.getUserIdToRemove();
        teamManagerView.getUserIdToRemove();
        String userIds = teamManagerView.getUserInput();
        String[] userIdArray = userIds.split(",");
        removeUsersFromChannel(meetingChannel, userIdArray);
        writeTeamsToCSV();
    }

    private void removeUsersFromChannel(MeetingChannel meetingChannel, String[] userIdArray){
        for (String id : userIdArray){
            meetingChannel.removeParticipant(getUserById(Integer.parseInt(id)));
        }
    }

    private void updateMeetingDayTime(MeetingChannel meetingChannel){
        teamManagerView.getChannelMeetingDayTime();
        String dateTime = teamManagerView.getUserInput();
        if (dateTime.equals(""))
            dateTime = null;
        meetingChannel.getMeeting().setDateTime(dateTime);
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
            addUsersToChannel(selectedTeam.getDefaultChannel(), userIdArray);

            writeUsersToCSV();
        }
        else{
            throw new UnauthorizedUserOperationException("Only academicians can add new members.");
        }
    }

    private void addUsersToTeam(ITeam selectedTeam, String[] userIdArray){
        for (String id : userIdArray){
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
        for (String id : userIdArray){
            IUser user = getUserById(Integer.parseInt(id));
            selectedTeam.removeMember(user);
            List<MeetingChannel> channels = user.getChannels();
            for (MeetingChannel channel : channels)
                channel.removeParticipant(user);
            List<IUser> teamOwners = teamOwnerMap.get(selectedTeam);
            if (teamOwners.contains(user))
                teamOwners.remove(user);

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
            selectedTeam.addTeamOwner(newOwner);
        }
        else{
            throw new UnauthorizedUserOperationException();
        }
    }

    private void showMeetingChannelDetails(ITeam selectedTeam){

        List<MeetingChannel> meetingChannels = selectedTeam.getMeetingChannels();
        meetingChannels.add(0, selectedTeam.getDefaultChannel());
        for(MeetingChannel meetingChannel: meetingChannels){
            System.out.println("Channel Name: " + meetingChannel.getChannelName());
            System.out.println("Meeting Date and Time: " + meetingChannel.getMeeting().getDateTime());
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
