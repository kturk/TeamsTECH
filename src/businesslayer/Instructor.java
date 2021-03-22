package businesslayer;

public class Instructor extends Academician {
    public Instructor(int id, String name, String department) {
        super(id, name, department);
    }

    public Instructor(int id, String name, String password, String department) {
        super(id, name, password, department);
    }

    @Override
    public String getClassName() {
        return "Instructor";
    }
}
