import java.util.Date;

public class logEvent {
    private Date time;
    private String request;
    private int status;
    private int duration;

    public logEvent(Date time, String request, int status, int duration) {
        this.time = time;
        this.request = request;
        this.status = status;
        this.duration = duration;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
