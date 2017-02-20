package exception.service;

/**
 * Created by fan on 6/29/2016.
 */
public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
