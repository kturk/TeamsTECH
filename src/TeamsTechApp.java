import BusinessLayer.Mediator;
import DataAccessLayer.DataHandler;

import java.util.ArrayList;
import java.util.List;

public class TeamsTechApp {
    public static void main(String[] args) {

        Mediator mediator = new Mediator();

        DataHandler dataHandler = new DataHandler("teamList.csv");

        List<ArrayList<String>> data = dataHandler.getData();


        for(int i=0; i<mediator.getUserList().size(); i++) {
            System.out.println(mediator.getUserList().get(i).getName());
            System.out.println(mediator.getUserList().get(i).getId());
        }

//        for(int i=0; i<mediator.getTeamList().size(); i++) {
//            System.out.println(mediator.getTeamList().get(i).getMembers().size());
//        }


    }
}
