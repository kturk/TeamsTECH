package businesslayer;


import java.util.List;

public class TeamsTechApp {
    public static void main(String[] args) {

        TeamManager teamManager = new TeamManager();
        teamManager.start();
//        teamManager.getString();

//        for(int i = 0; i< teamManager.getUserList().size(); i++) {
//            System.out.println(teamManager.getUserList().get(i).getName());
//            System.out.println(teamManager.getUserList().get(i).getTeams());
//        }

//        System.out.println(teamManager.getUserList().get(3).toCSV());
//        for(int i=0; i<teamManager.getTeamList().size(); i++) {
//            List<Academician> teamOwners = teamManager.getTeamList().get(i).getTeamOwners();
//            System.out.println(teamManager.getTeamList().get(i));
//            for (Academician owner : teamOwners){
//                System.out.println(owner.getName());
//            }
//            System.out.println();
////            System.out.println(teamManager.getTeamList().get(i).getTeamOwners());
//        }


    }
}
