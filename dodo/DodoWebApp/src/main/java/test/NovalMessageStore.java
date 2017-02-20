package test;

/**
 * Created by fan on 7/1/2016.
 */
public class NovalMessageStore implements MessageStore {

    private String message = "Noval messageStore.";;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
