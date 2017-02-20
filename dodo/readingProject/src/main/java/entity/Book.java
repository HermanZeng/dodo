package entity;

import java.util.Set;

/**
 * Created by heming on 7/8/2016.
 */
public class Book {
    private String id;
    private int pages;
    private String wid;
    private String title;
    private String publisher;
    private String introduction;
    private double rate;
    private String image;
    private String isbn10;
    private String isbn13;
    private Set<Author> authors;
    private Set<Translator> translators;
    private Set<Category> categories;

    //TODO: category need to be implemented


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getWid() {
        return wid;
    }

    public void setWid(String wid) {
        this.wid = wid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Translator> getTranslators() {
        return translators;
    }

    public void setTranslators(Set<Translator> translators) {
        this.translators = translators;
    }

    public String getIsbn10() {
        return isbn10;
    }

    public void setIsbn10(String isbn10) {
        this.isbn10 = isbn10;
    }

    public String getIsbn13() {
        return isbn13;
    }

    public void setIsbn13(String isbn13) {
        this.isbn13 = isbn13;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", pages=" + pages +
                ", wid='" + wid + '\'' +
                ", title='" + title + '\'' +
                ", publisher='" + publisher + '\'' +
                ", introduction='" + introduction + '\'' +
                ", rate=" + rate +
                ", image='" + image + '\'' +
                ", authors=" + authors +
                ", translators=" + translators +
                '}';
    }
}
