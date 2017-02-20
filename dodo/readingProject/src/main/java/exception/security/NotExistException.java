package exception.security;

/**
 * Created by heming on 7/1/2016.
 */
public class NotExistException extends RuntimeException {
    public NotExistException(String message) {
        super(message);
    }
}
