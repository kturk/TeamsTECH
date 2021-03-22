package businesslayer;

public class Student extends User {

    private static final String  DOMAIN = "@std.iyte.edu.tr";

    public Student(int id, String name, String department) {
        super(id, name, department);
        super.initializeEmail(DOMAIN);
        super.initializePassword();
    }

    public Student(int id, String name, String password, String department) {
        super(id, name, password, department);
        super.initializeEmail(DOMAIN);
    }

    public static String getDOMAIN() {
        return DOMAIN;
    }

    @Override
    public String getClassType() {
        return "Student";
    }

    @Override
    public String getClassName() {
        return "Student";
    }
}
