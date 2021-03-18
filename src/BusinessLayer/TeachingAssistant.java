package BusinessLayer;

public class TeachingAssistant extends Academician {

    public TeachingAssistant(String name, String department) {
        super(name, department);
    }

    public TeachingAssistant(int id, String name, String password, String department) {
        super(id, name, password, department);
    }
}
