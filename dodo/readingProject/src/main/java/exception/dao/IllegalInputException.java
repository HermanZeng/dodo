package exception.dao;

/**
 * Created by fan on 6/29/2016.
 */
public class IllegalInputException extends RuntimeException {
    public IllegalInputException(String message) {
        super(message);
    }
}
