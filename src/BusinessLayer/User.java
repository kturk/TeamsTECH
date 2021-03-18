package BusinessLayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class User {

    private int id;
    private String name;
    private String email;
    private String password;
    private String department;
    private List<ITeam> teams;

    public User() {
        this.teams = new ArrayList<ITeam>();
    }

    public User(int id, String name, String department) {
        this.id = id;
        this.name = name;
//        this.email = email;
//        this.password = password;
        this.department = department;
        this.teams = new ArrayList<ITeam>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<ITeam> getTeams() {
        return teams;
    }

    public void setTeams(List<ITeam> teams) {
        this.teams = teams;
    }

    public void addTeam(ITeam team){
        this.teams.add(team);
    }

    public void initializeEmail(String domain) {
        String tempName = this.name.replaceAll("\\s","").toLowerCase();
        this.email = tempName + domain;
    }

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
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", department='" + department + '\'' +
                ", teams=" + teams +
                '}';
    }
}
