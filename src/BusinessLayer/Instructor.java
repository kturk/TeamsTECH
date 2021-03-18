package BusinessLayer;

public class Instructor extends Academician {
    public Instructor(String name, String department) {
        super(name, department);
    }

    public Instructor(int id, String name, String password, String department) {
        super(id, name, password, department);
    }
}
