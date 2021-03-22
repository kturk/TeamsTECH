package businesslayer;

public class TeachingAssistant extends Academician {

    public TeachingAssistant(int id, String name, String department, TeamManager manager) {
        super(id, name, department, manager);
    }

    public TeachingAssistant(int id, String name, String password, String department, TeamManager manager) {
        super(id, name, password, department, manager);
    }

    @Override
    public String getClassName() {
        return "Teaching Assistant";
    }
}
