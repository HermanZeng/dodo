package entity;

/**
 * Created by heming on 9/9/2016.
 */
public class Honor {
    Long id;
    String userId;
    String trackId;
    String bookId;
    int stageSeq;
    String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public int getStageSeq() {
        return stageSeq;
    }

    public void setStageSeq(int stageSeq) {
        this.stageSeq = stageSeq;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
