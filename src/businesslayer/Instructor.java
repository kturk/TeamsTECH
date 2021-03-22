package businesslayer;

public class Instructor extends Academician {
    public Instructor(int id, String name, String department, TeamManager manager) {
        super(id, name, department, manager);
    }

    public Instructor(int id, String name, String password, String department, TeamManager manager) {
        super(id, name, password, department, manager);
    }

    @Override
    public String getClassName() {
        return "Instructor";
    }
}
