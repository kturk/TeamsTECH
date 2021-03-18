package BusinessLayer;

public class Meeting {

    private static int id;
    private String date;
    private String time;

    public Meeting() {
        this.id++;
    }

    public Meeting(String meetingDate) {
        this.id++;
        setDateAndTime(meetingDate);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private void setDateAndTime(String meetingDate){
        String[] tokens = meetingDate.split(" ", -1);
        this.date = tokens[0];
        this.time = tokens[1] + " " + tokens[2];
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
