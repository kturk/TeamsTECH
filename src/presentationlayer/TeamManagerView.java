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

    public void promptChoices(){
        System.out.print("What would you like to do? "
                +                                 "1 -> Add a team "
                + 								  "2 -> Remove a team "
                + 								  "3 -> Update a team "
                +  								  "0 -> Exit from the application "
                +                                 "Please enter a number between 0-3: ");
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

    public void getDefaultMeetingDay(){
        System.out.println("Please enter a day of week for default meeting channel (Ex. Monday): ");
    }

    public void getDefaultMeetingTime(){
        System.out.println("Please enter a time for default meeting channel (Ex. 13.30)");
    }

    // This method is for removing a team
    public void showTeams(){
        System.out.println("Please enter the id of a team that you would like to remove (Ex. CENG431): ");
    }

    public void getChannelName(){
        System.out.println("Please enter name of the channel.: ");
    }

    public void getIsChannelPrivate(){
        System.out.println("Will this channel be private one? (Y/n): ");
    }

    public void getChannelMeetingDay(){
        System.out.println("Please enter a day of week for the meeting channel (Ex. Tuesday): ");
    }

    public void getMeetingTime(){
        System.out.println("Please enter a time for the meeting channel (Ex. 15.45)");
    }

    public String getStringInput(){
        return keyboard.nextLine();
    }
}
