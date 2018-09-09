package sultaani.com.taskmanager.Adapter;


import sultaani.com.taskmanager.Model.FileModel;
import sultaani.com.taskmanager.Model.User;

/**
 * Created by Alessandro Barreto on 17/06/2016.
 */
public class ChatModel {

    private String id;
    private User userModel;

    private String message;
    private String timeStamp;
    private FileModel file;


    public ChatModel() {
    }

    public ChatModel(User userModel, String message, String timeStamp, FileModel file) {
        this.userModel = userModel;
        this.message = message;
        this.timeStamp = timeStamp;
        this.file = file;
    }

    public ChatModel(User userModel, String timeStamp) {
        this.userModel = userModel;
        this.timeStamp = timeStamp;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUserModel() {
        return userModel;
    }

    public void setUserModel(User userModel) {
        this.userModel = userModel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public FileModel getFile() {
        return file;
    }

    public void setFile(FileModel file) {
        this.file = file;
    }


    @Override
    public String toString() {
        return "ChatModel{" +
                ", file=" + file +
                ", timeStamp='" + timeStamp + '\'' +
                ", message='" + message + '\'' +
                ", userModel=" + userModel +
                '}';
    }
}
