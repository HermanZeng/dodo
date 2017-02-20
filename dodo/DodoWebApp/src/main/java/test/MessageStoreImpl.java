package test;

/**
 * Created by fan on 7/1/2016.
 */
public class MessageStoreImpl implements MessageStore{

    private String message = "MessageStoreImpl 1";;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
