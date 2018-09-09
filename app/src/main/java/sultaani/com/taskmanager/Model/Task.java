package sultaani.com.taskmanager.Model;

/**
 * Created by CH_M_USMAN on 02-Jan-18.
 */

public class Task {
    private String taskid,name;
    private boolean status;

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
