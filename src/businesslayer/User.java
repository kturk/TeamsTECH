package businesslayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class User implements IUser {

    private int id;
    private String name;
    private String email;
    private String password;
    private String department; // CSV does not have this column. Assumed as "Computer Engineering"


    public User() {    }

    public User(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public User(int id, String name, String password, String department) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.department = department;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getDepartment() {
        return department;
    }

    @Override
    public void setDepartment(String department) {
        this.department = department;
    }

//    @Override
//    public List<ITeam> getTeams() {
//        return teams;
//    }
//
//    @Override
//    public void setTeams(List<ITeam> teams) {
//        this.teams = teams;
//    }
//
//    @Override
//    public void addTeam(ITeam team){
//        this.teams.add(team);
//    }

    @Override
    public void initializeEmail(String domain) {
        String tempName = this.name.replaceAll("\\s","").toLowerCase();
        this.email = tempName + domain;
    }

    @Override
    public void initializePassword(){
        String password = "";
        Random rand = new Random();
        String alphabet = "abcdefghijklmnopqrstuvwxyz0123456789";

        for(int i=0; i < 4; i++){
            int randomIndex = rand.nextInt(alphabet.length());
            password = password + alphabet.charAt(randomIndex);
        }
        this.password = password;
    }

    @Override
    public String toCSV() {
        StringBuilder builder = new StringBuilder();
        String classType = this.getClass().getName();
        if (classType.equals("businesslayer.Instructor")) // TODO fix
            builder.append("Instructor,");
        else if (classType.equals("businesslayer.TeachingAssistant")) // TODO fix
            builder.append("Teaching Assistant,");
        else
            builder.append("Student,");

        builder.append(this.name + ",");
        builder.append(this.id + ",");
        builder.append(this.email + ",");
        builder.append(this.password);

        for (ITeam team : teams)
            builder.append("," + team.getId());
        return builder.toString();
    }
}
