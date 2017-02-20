package entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Set;

/**
 * Created by heming on 7/15/2016.
 */
public class Stage {

    @JSONField(name = "honor_title")
    private String honorTitle;

    private int seq;

    private Set<String> books;

    public String getHonorTitle() {
        return honorTitle;
    }

    public void setHonorTitle(String honorTitle) {
        this.honorTitle = honorTitle;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public Set<String> getBooks() {
        return books;
    }

    public void setBooks(Set<String> books) {
        this.books = books;
    }
}
