package entity;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by fan on 9/10/2016.
 */
public class BookReviewShort {
    private Long id;

    private String wid;

    private String reviewer;

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

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
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
        return "BookReviewShort{" +
                "id=" + id +
                ", wid='" + wid + '\'' +
                ", reviewer='" + reviewer + '\'' +
                ", content='" + content + '\'' +
                ", likeCnt=" + likeCnt +
                ", date='" + date + '\'' +
                '}';
    }
}
