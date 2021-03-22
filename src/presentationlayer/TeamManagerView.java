package presentationlayer;

import java.util.Scanner;

public class TeamManagerView {
    private Scanner keyboard;

    public TeamManagerView() {
        keyboard = new Scanner(System.in);
    }

    public void printWelcome(){
        System.out.println("Welcome to the TeamsTECH Application.");
    }

    public void getEmail(){
        System.out.println("Please enter your email: ");
    }

    public void getPassword(){
        System.out.println("Please enter your password: ");
    }

    public void correctCredentials(String name) {
        System.out.println("Hello, " + name);
    }

    public void wrongCredentials() {
        System.out.println("Your email or password is not correct!\n" +
                "Please try again.");
    }

    public void promptMainChoices(){
        System.out.println("What would you like to do? \n"
                +                                 "1 -> Add a team \n"
                + 								  "2 -> Remove a team \n"
                + 								  "3 -> Update a team \n"
                +  								  "0 -> Exit from the application \n"
                +                                 "Please enter a number between 0-3: ");
    }

    public void wrongInput(){
        System.out.println("Please enter a valid input.: ");
    }

    public void getTeamName(){
        System.out.println("Please enter a name for the team (Ex. Programming Fundamentals): ");
    }

    public void getTeamId(){
        System.out.println("Please enter an id for the team (Ex. CENG211): ");
    }

    public void getDefaultChannelName(){
        System.out.println("Please enter a name for default meeting channel (Ex. General): ");
    }

    public void getDefaultMeetingDayTime(){
        System.out.println("Please enter a day of week and a time for default meeting channel \n" +
                "(Ex. Monday 10.45 AM): \n" +
                "(You can leave this field empty.)");
    }

    public void getTeamIdToRemove(){
        System.out.println("Please enter the id of a team that you would like to remove (Ex. CENG431): ");
    }

    public void getTeamIdToUpdate(){
        System.out.println("Please enter the id of a team that you would like to update (Ex. CENG431): ");
    }

    public void promptUpdateTeamChoices(){
        System.out.println("What would you like to do to update a team? \n"
                +                                 "1 -> Add a meeting channel \n"
                + 								  "2 -> Remove a meeting channel \n"
                + 								  "3 -> Update a meeting channel \n"
                +  								  "4 -> Add a member \n"
                +  								  "5 -> Remove a member \n"
                +  								  "6 -> Add a team owner \n"
                +  								  "7 -> Show meeting channels and their meetings \n" // TODO check private
                +  								  "8 -> Show distinct number of members \n"
                +  								  "0 -> Back \n"
                +                                 "Please enter a number between 0-8: ");
    }

    public void getChannelName(){
        System.out.println("Please enter name of the channel.: ");
    }

    public void getChannelNameToRemove(){
        System.out.println("Please enter name of the channel that you would like to remove.: ");
    }

    public void getChannelNameToUpdate(){
        System.out.println("Please enter name of the channel that you would like to update.: ");
    }

    public void promptUpdateMeetingChannelChoices(){
        System.out.println("What would you like to do? \n"
                +                                 "1 -> Add a participant \n"
                + 								  "2 -> Remove a participant \n"
                + 								  "3 -> Update meeting day and time \n"
                +  								  "0 -> Back \n"
                +                                 "Please enter a number between 0-3: ");
    }
    public void getUserId(){
        System.out.println("Please enter the id of the user: ");
    }

//    public void getIsChannelPrivate(){
//        System.out.println("Will this channel be private one? (Y/n): ");
//    }

    public void getChannelMeetingDayTime(){
        System.out.println("Please enter a day of week and a time for meeting channel \n" +
                "(Ex. Monday 10.45 AM): \n" +
                "(You can leave this field empty.)");
    }

    public void getUserIdToAdd(){
        System.out.println("Please enter user ids to add. (Ex. 45,97,33): ");
    }

    public void getUserIdToRemove(){
        System.out.println("Please enter user ids to remove. (Ex. 45,97,33): ");
    }

    public String getUserInput(){
        return keyboard.nextLine();
    } // TODO boolean isRequired attribute

    public void exitMessage() {
        System.out.println("Thank you for using TeamsTECH application.");
    }
}
