package businesslayer;

public class Meeting {

    private static int count = 0;
    private int id;
//    private String date;
//    private String time;
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



//    public String getDate() {
//        return date;
//    }
//
//    public void setDate(String date) {
//        this.date = date;
//    }
//
//    public String getTime() {
//        return time;
//    }
//
//    public void setTime(String time) {
//        this.time = time;
//    }

//    public void setDateAndTime(String meetingDate){
//        if (meetingDate == null){
//            this.date = "";
//            this.time = "";
//        }
//        else {
//            String[] tokens = meetingDate.split(" ", -1);
//            this.date = tokens[0];
//            this.time = tokens[1] + " " + tokens[2];
//        }
//    }

//    @Override
//    public String toString() {
//        return "Meeting{" +
//                "date='" + date + '\'' +
//                ", time='" + time + '\'' +
//                '}';
//    }
}
