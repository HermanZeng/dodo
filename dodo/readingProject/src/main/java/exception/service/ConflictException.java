package exception.service;

/**
 * Created by fan on 6/29/2016.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
