package businesslayer;

public abstract class Academician extends User {

    private static final String  DOMAIN = "@iyte.edu.tr";

    public Academician(int id, String name, String department, TeamManager manager) {
        super(id, name, department, manager);
        super.initializeEmail(DOMAIN);
        super.initializePassword();
    }

    public Academician(int id, String name, String password, String department, TeamManager manager) {
        super(id, name, password, department, manager);
        super.initializeEmail(DOMAIN);
    }

    public static String getDOMAIN() {
        return DOMAIN;
    }

    @Override
    public String getClassType() {
        return "Academician";
    }
}
