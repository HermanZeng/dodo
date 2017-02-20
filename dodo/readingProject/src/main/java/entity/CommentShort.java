package entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by fan on 9/11/2016.
 */
public class CommentShort {
    private Long id;

    @JSONField(name = "review_id")
    private Long reviewId;

    @JSONField(name = "user")
    private String userId;

    private String content;

    @JSONField(name = "like_cnt")
    private Integer likeCnt;

    private String date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getLikeCnt() {
        return likeCnt;
    }

    public void setLikeCnt(Integer likeCnt) {
        this.likeCnt = likeCnt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", reviewId=" + reviewId +
                ", userId='" + userId + '\'' +
                ", content='" + content + '\'' +
                ", likeCnt=" + likeCnt +
                ", date='" + date + '\'' +
                '}';
    }
}
