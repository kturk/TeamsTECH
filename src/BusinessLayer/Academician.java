package BusinessLayer;

public abstract class Academician extends User {

    private static final String  DOMAIN = "@iyte.edu.tr";

    public Academician(int id, String name, String department) {
        super(id, name, department);
        super.initializeEmail(DOMAIN);
        super.initializePassword();
    }

    public static String getDOMAIN() {
        return DOMAIN;
    }
}
