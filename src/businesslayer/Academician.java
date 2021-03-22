package businesslayer;

public abstract class Academician extends User {

    private static final String  DOMAIN = "@iyte.edu.tr";

    public Academician(int id, String name, String department) {
        super(id, name, department);
        super.initializeEmail(DOMAIN);
        super.initializePassword();
    }

    public Academician(int id, String name, String password, String department) {
        super(id, name, password, department);
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
