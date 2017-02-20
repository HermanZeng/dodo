package observer.event;

/**
 * Created by heming on 9/1/2016.
 */
public class BookFinishedEvent {
    private String userId;
    private String bookId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

}
