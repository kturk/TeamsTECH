import BusinessLayer.Mediator;
import DataAccessLayer.DataHandler;
import DataAccessLayer.FileLineReader;

import java.util.ArrayList;
import java.util.List;

public class TeamsTechApp {
    public static void main(String[] args) {

//        Mediator mediator = new Mediator();

        DataHandler dataHandler = new DataHandler("userList.csv");

        List<List<String>> data = dataHandler.getData();

        for(int i=0; i<data.size();i++){
            System.out.println(data.get(i).toString());
        }
    }
}
