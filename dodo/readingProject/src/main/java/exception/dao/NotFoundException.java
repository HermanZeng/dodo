package exception.dao;

/**
 * Created by fan on 6/29/2016.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
