package entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by fan on 7/18/2016.
 */
public class ReadingProgress {
    private Long id;

    @JSONField(name = "user_id")
    private String userId;

    @JSONField(name = "book_id")
    private String bookId;

    private Integer total;

    private Integer current;

    private String date;

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

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
