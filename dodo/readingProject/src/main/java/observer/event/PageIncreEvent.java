package observer.event;

/**
 * Created by heming on 7/14/2016.
 */
public class PageIncreEvent {
    private String userId;
    private String bookId;
    private Integer page;
    private String trackId;
    private String stageSeq;

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

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public String getStageSeq() {
        return stageSeq;
    }

    public void setStageSeq(String stageSeq) {
        this.stageSeq = stageSeq;
    }
}
