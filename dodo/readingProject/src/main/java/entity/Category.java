package entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by heming on 7/8/2016.
 */
public class Category {
    private int id;
    private String  description;
    private int reference;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReference() {
        return reference;
    }

    public void setReference(int reference) {
        this.reference = reference;
    }
}
