package businesslayer;

public class Meeting {

    // TODO should these be id or idCount?
    private static int id;
    private String date;
    private String time;

    // TODO check this.id or not
    public Meeting() {
        id++;
    }

    public Meeting(String meetingDate) {
        // TODO check this.id or not
        id++;
        setDateAndTime(meetingDate);
    }

    public int getId() {
        return id;
    }

    // TODO check this.id or not
    public void setId(int id) {
        Meeting.id = id;
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
        if (meetingDate == null){
            this.date = "";
            this.time = "";
        }
        else {
            String[] tokens = meetingDate.split(" ", -1);
            this.date = tokens[0];
            this.time = tokens[1] + " " + tokens[2];
        }
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
