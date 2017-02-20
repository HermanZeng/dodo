package entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by fan on 7/4/2016.
 */
public class Role {

    @JsonProperty("id")
    private Integer roleId;

    private String description;

    private Integer reference;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReference() {
        return reference;
    }

    public void setReference(Integer reference) {
        this.reference = reference;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", description='" + description + '\'' +
                ", reference=" + reference +
                '}';
    }
}
