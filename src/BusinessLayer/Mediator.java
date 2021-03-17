package BusinessLayer;

import java.util.ArrayList;

public class Mediator {

    private ArrayList<ITeam> teamList;

    public Mediator() {
        teamList = new ArrayList<ITeam>();
    }

    public ArrayList<ITeam> getTeamList() {
        return teamList;
    }

    public void setTeamList(ArrayList<ITeam> teamList) {
        this.teamList = teamList;
    }


}
