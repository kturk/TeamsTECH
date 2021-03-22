package businesslayer;

public class Meeting {

    private static int count = 0;
    private int id;
    private String dateTime;

    public Meeting() {
        this.setId(++count);
    }

    public Meeting(String meetingDateTime) {
        this.setId(++count);
        setDateTime(meetingDateTime);
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
