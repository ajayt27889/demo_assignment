package com.it.feedassignment.roomdb;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class FeedModel {
    @PrimaryKey(autoGenerate = true)
    private int feed_id;

    @ColumnInfo(name = "title") // column name will be "note_content" instead of "content" in table
    private String title;

    @ColumnInfo(name = "description") // column name will be "note_content" instead of "content" in table
    private String description;

    @ColumnInfo(name = "imageHref") // column name will be "note_content" instead of "content" in table
    private String imageHref;

    public FeedModel(String title, String description, String imageHref) {
        this.title = title;
        this.description = description;
        this.imageHref = imageHref;
    }

    public int getFeed_id() {
        return feed_id;
    }

    public void setFeed_id(int feed_id) {
        this.feed_id = feed_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageHref() {
        return imageHref;
    }

    public void setImageHref(String imageHref) {
        this.imageHref = imageHref;
    }

    @Override
    public int hashCode() {
        int result = feed_id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }
}
