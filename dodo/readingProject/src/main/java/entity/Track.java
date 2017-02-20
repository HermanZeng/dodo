package entity;


import com.alibaba.fastjson.annotation.JSONField;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;
import org.mongodb.morphia.utils.IndexDirection;
import org.mongodb.morphia.utils.IndexType;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by heming on 7/15/2016.
 */

@Indexes(@Index(fields = @Field(value = "$**", type = IndexType.TEXT)))
@Entity("tracks")
public class Track implements Comparable<Track> {

    @Id
    @JSONField(deserialize = false)
    private ObjectId id;

    @JSONField(name = "origin_id")
    private String originId;

    private String title;

    private String image;

    @JSONField(name = "initiator")
    private String initiatorId;

    @JSONField(name = "modifier")
    private String modifierId;

    @JSONField(name = "create_date")
    private String createDate;

    @JSONField(name = "fork_cnt")
    @Indexed(value = IndexDirection.DESC)
    private Integer forkCnt;

    @JSONField(name = "star_cnt")
    @Indexed(value = IndexDirection.DESC)
    private Integer starCnt;

    @JSONField(name = "category")
    Set<Integer> categories = new HashSet<Integer>();

    @JSONField(name = "stage")
    Set<Stage> stages = new HashSet<Stage>();

    public String getId() {
        return id.toString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInitiatorId() {
        return initiatorId;
    }

    public void setInitiatorId(String initiatorId) {
        this.initiatorId = initiatorId;
    }

    public String getModifierId() {
        return modifierId;
    }

    public void setModifierId(String modifierId) {
        this.modifierId = modifierId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getForkCnt() {
        return forkCnt;
    }

    public void setForkCnt(Integer forkCnt) {
        this.forkCnt = forkCnt;
    }

    public Integer getStarCnt() {
        return starCnt;
    }

    public void setStarCnt(Integer starCnt) {
        this.starCnt = starCnt;
    }

    public Set<Integer> getCategories() {
        return categories;
    }

    public void setCategories(Set<Integer> categories) {
        this.categories = categories;
    }

    public Set<Stage> getStages() {
        return stages;
    }

    public void setStages(Set<Stage> stages) {
        this.stages = stages;
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", originId=" + originId +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", initiatorId='" + initiatorId + '\'' +
                ", modifierId='" + modifierId + '\'' +
                ", createDate='" + createDate + '\'' +
                ", forkCnt=" + forkCnt +
                ", starCnt=" + starCnt +
                ", categories=" + categories +
                ", stages=" + stages +
                '}';
    }

    @Override
    public int compareTo(Track o) {
        int oForkCnt = o.getForkCnt();
        int oStarCnt = o.getStarCnt();

        final int desc_gt = -1;
        final int desc_lt = 1;

        if (this.forkCnt > oForkCnt) {

            return desc_gt;

        } else if (this.forkCnt < oForkCnt) {

            return desc_lt;

        } else if (this.forkCnt == oForkCnt) {

            if (this.starCnt > oStarCnt) {

                return desc_gt;

            } else if (this.starCnt < oStarCnt) {

                return desc_lt;
            }
        }
        return 0;
    }
}
