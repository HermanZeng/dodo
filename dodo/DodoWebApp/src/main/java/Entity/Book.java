package entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Created by fan on 7/13/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {

    private String id;

    private String wid;

    private String title;

    private String introduction;

    private String publisher;

    private Integer pages;

    private String isbn13;

    private String isbn10;

    private String image;

    private Double rate;

    private List<Category> category;

    private List<Author> authors;

    private List<Translator> translators;

    public static boolean isValid(Book book) {
        return validWid(book.getWid()) && validTitle(book.getTitle()) && validIsbn10(book.getIsbn10()) && validIsbn13(book.getIsbn13());
    }

    public static boolean validWid(String wid) {
        if (wid == null) {
            return false;
        }
        // TODO: check
        return true;
    }

    public static boolean validTitle(String title) {
        if (title == null) {
            return false;
        }
        // TODO: check
        return true;
    }

    public static boolean validIsbn13(String isbn13) {
        if (isbn13 == null) {
            return false;
        }
        // TODO: check
        return true;
    }

    public static boolean validIsbn10(String isbn10) {
        if (isbn10 == null) {
            return false;
        }
        // TODO: check
        return true;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Translator> getTranslators() {
        return translators;
    }

    public void setTranslators(List<Translator> translators) {
        this.translators = translators;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", wid='" + wid + '\'' +
                ", title='" + title + '\'' +
                ", introduction='" + introduction + '\'' +
                ", publisher='" + publisher + '\'' +
                ", pages=" + pages +
                ", isbn13='" + isbn13 + '\'' +
                ", isbn10='" + isbn10 + '\'' +
                ", image='" + image + '\'' +
                ", rate=" + rate +
                ", category=" + category +
                ", authors=" + authors +
                ", translators=" + translators +
                '}';
    }
}
