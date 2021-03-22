package businesslayer;

public class TeachingAssistant extends Academician {

    public TeachingAssistant(int id, String name, String department) {
        super(id, name, department);
    }

    public TeachingAssistant(int id, String name, String password, String department) {
        super(id, name, password, department);
    }

    @Override
    public String getClassName() {
        return "Teaching Assistant";
    }
}
