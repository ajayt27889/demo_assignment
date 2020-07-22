package com.it.feedassignment.roomdb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface FeedDao {

    @Query("SELECT * FROM FeedModel "+ "Feed_Database")
    List<FeedModel> getAllFeeds();

    /*
     * Insert the object in database
     * @param feed, object to be inserted
     */
    @Insert
    void insert(FeedModel feed);

    /*
     * update the object in database
     * @param feed, object to be updated
     */
    @Update
    void update(FeedModel feed);

    /*
     * delete the object from database
     * @param feed, object to be deleted
     */
    @Delete
    void delete(FeedModel feed);

    /*
     * delete list of objects from database
     * @param feed, array of objects to be deleted
     */
    @Delete
    void delete(FeedModel... feeds);

    @Query("DELETE FROM FeedModel")
    public void deleteAll();

}
