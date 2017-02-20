package entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by fan on 7/13/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author {

    private String id;

    private String introduction;

    private String name;

    private String nationality;

    private String image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id='" + id + '\'' +
                ", introduction='" + introduction + '\'' +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
